package db.migration

import io.octatec.horext.api.domain.AcademicPeriodOrganizationUnits
import io.octatec.horext.api.domain.AcademicPeriods
import io.octatec.horext.api.domain.ClassSessionTypes
import io.octatec.horext.api.domain.ClassSessions
import io.octatec.horext.api.domain.Classrooms
import io.octatec.horext.api.domain.Courses
import io.octatec.horext.api.domain.HourlyLoads
import io.octatec.horext.api.domain.OrganizationUnits
import io.octatec.horext.api.domain.ScheduleSubjects
import io.octatec.horext.api.domain.Schedules
import io.octatec.horext.api.domain.Sections
import io.octatec.horext.api.domain.StudyPlans
import io.octatec.horext.api.domain.Subjects
import io.octatec.horext.api.domain.Teachers
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.v1.core.IntegerColumnType
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.LongColumnType
import org.jetbrains.exposed.v1.core.VarCharColumnType
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class R__200_GenerateHourlyLoad : BaseCsvMigration() {
    companion object {
        private const val COL_FACULTY_CODE = "codigo_facultad"
        private const val COL_COURSE = "codigo_curso"
        private const val COL_SECTION = "seccion"
        private const val COL_VACANCIES = "vacantes"
        private const val COL_UPDATED_AT = "updated_at"
        private const val COL_DELETED_AT = "deleted_at"
        private const val COL_START_TIME = "inicio"
        private const val COL_END_TIME = "fin"
        private const val COL_CLASSROOM = "aula"
        private const val COL_DNI = "dni_docente"
        private const val COL_TEACHER = "nombre_docente"
        private const val COL_TYPE = "tipo"
        private const val COL_DAY = "dia"
    }

    data class CsvMetadata(
        val hourlyLoadName: String,
        val academicCode: String,
        val defaultFacultyCode: String,
        val fileLastModified: Instant?,
    )

    data class ScheduleResume(
        val facultyCode: String,
        val course: String,
        val section: String,
        val vacancies: Int,
        val updatedAt: LocalDateTime,
        val deletedAt: LocalDateTime?,
        val startTime: String,
        val endTime: String,
        val classroom: String,
        val teacherDni: String?,
        val teacherName: String,
        val sessionType: String,
        val day: String,
    )

    override fun getChecksum(): Int = buildChecksum(prefix = "hl_")

    override fun migrate(context: Context) {
        val entries = listCsvFiles()
        if (entries.isEmpty()) {
            log.info("R__200_GenerateHourlyLoad: no CSV files found, skipping")
            return
        }
        log.info("R__200_GenerateHourlyLoad: processing {} file(s)", entries.size)
        val db = Database.connect(SingleConnectionDataSource(context.connection, true))
        transaction(db) {
            entries.forEach { (meta, rows) -> processFile(meta, rows) }
        }
        log.info("R__200_GenerateHourlyLoad: done")
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.processFile(
        meta: CsvMetadata,
        rows: List<ScheduleResume>,
    ) {
        val rowsByFaculty = rows.groupBy { it.facultyCode }

        for ((facultyCode, facultyRows) in rowsByFaculty) {
            log.info("R__200: file='{}' faculty='{}' rows={}", meta.hourlyLoadName, facultyCode, facultyRows.size)
            val faculty =
                OrganizationUnits
                    .selectAll()
                    .where { OrganizationUnits.code eq facultyCode }
                    .firstOrNull() ?: error("Faculty not found with code: $facultyCode")
            val facultyId = faculty[OrganizationUnits.id].value
            validateFaculty(facultyCode, facultyId, facultyRows)
            val apouId = setupFaculty(meta, facultyId, facultyRows)
            processHourlyLoad(meta, facultyRows, facultyId, apouId)
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.validateFaculty(
        facultyCode: String,
        facultyId: Long,
        rows: List<ScheduleResume>,
    ) {
        val errors = mutableListOf<String>()
        val activeRows = rows.filter { it.deletedAt == null }
        val csvCourses = activeRows.map { it.course }.distinct()
        if (csvCourses.isNotEmpty()) {
            val knownCourses =
                Subjects
                    .join(StudyPlans, JoinType.INNER, Subjects.studyPlanId, StudyPlans.id)
                    .join(OrganizationUnits, JoinType.INNER, StudyPlans.organizationUnitId, OrganizationUnits.id)
                    .select(Subjects.courseId)
                    .where { OrganizationUnits.parentOrganizationId eq facultyId }
                    .map { it[Subjects.courseId].value }
                    .toSet()
            val missing = csvCourses.filter { it !in knownCourses }
            if (missing.isNotEmpty()) {
                errors.add(
                    "Courses not found in any study plan of faculty '$facultyCode': ${missing.joinToString()}",
                )
            }
        }

        val teacherPairs =
            activeRows
                .mapNotNull { r -> r.teacherDni?.takeIf { it.isNotBlank() }?.let { it to r.teacherName.trim() } }
                .distinct()

        val dniToNamesInCsv = teacherPairs.groupBy({ it.first }, { it.second })
        dniToNamesInCsv.forEach { (dni, names) ->
            if (names.distinct().size > 1) {
                errors.add(
                    "DNI '$dni' maps to multiple teacher names in CSV: ${names.distinct().joinToString()}",
                )
            }
        }

        dniToNamesInCsv.forEach { (dni, csvNames) ->
            val dbRows = Teachers.selectAll().where { Teachers.code eq dni }.toList()
            when {
                dbRows.size > 1 -> {
                    val dbNames = dbRows.mapNotNull { it[Teachers.fullName] }
                    errors.add("DNI '$dni' matches multiple teachers in DB: ${dbNames.joinToString()}")
                }

                dbRows.size == 1 -> {
                    val dbName = dbRows.first()[Teachers.fullName].trim()
                    val csvName = csvNames.first()
                    if (dbName != csvName) {
                        errors.add("DNI '$dni': DB teacher name is '$dbName' but CSV has '$csvName'")
                    }
                }
            }
        }

        if (errors.isNotEmpty()) {
            throw IllegalStateException(
                "Migration R__200_GenerateHourlyLoad validation errors for faculty '$facultyCode':\n" +
                    errors.joinToString("\n") { "  - $it" },
            )
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.setupFaculty(
        meta: CsvMetadata,
        facultyId: Long,
        rows: List<ScheduleResume>,
    ): Long {
        val activeRows = rows.filter { it.deletedAt == null }
        val allSectionIds = activeRows.map { it.section.trim() }.distinct()
        val existingSectionIds =
            if (allSectionIds.isNotEmpty()) {
                Sections
                    .select(Sections.id)
                    .where { Sections.id inList allSectionIds }
                    .map { it[Sections.id].value }
                    .toSet()
            } else {
                emptySet()
            }
        Sections.batchInsert(allSectionIds.filter { it !in existingSectionIds }) { section ->
            this[Sections.id] = EntityID(section, Sections)
            this[Sections.code] = section
        }

        val allClassroomCodes = activeRows.map { it.classroom.trim() }.filter { it.isNotBlank() }.distinct()
        val existingClassroomCodes =
            if (allClassroomCodes.isNotEmpty()) {
                Classrooms
                    .select(Classrooms.code)
                    .where { Classrooms.code inList allClassroomCodes }
                    .map { it[Classrooms.code] }
                    .toSet()
            } else {
                emptySet()
            }
        Classrooms.batchInsert(allClassroomCodes.filter { it !in existingClassroomCodes }) { classroom ->
            this[Classrooms.code] = classroom
            this[Classrooms.name] = classroom
        }

        val apId =
            AcademicPeriods
                .selectAll()
                .where { AcademicPeriods.code eq meta.academicCode }
                .firstOrNull()
                ?.get(AcademicPeriods.id)
                ?.value
                ?: error("Academic period '${meta.academicCode}' not found — run R__UpdateAcademicPeriods first")

        val apouId =
            AcademicPeriodOrganizationUnits
                .selectAll()
                .where {
                    (AcademicPeriodOrganizationUnits.academicPeriodId eq apId) and
                        (AcademicPeriodOrganizationUnits.organizationUnitId eq facultyId)
                }.firstOrNull()
                ?.get(AcademicPeriodOrganizationUnits.id)
                ?.value
                ?: error("APOU for period '${meta.academicCode}' + faculty not found — run R__UpdateAcademicPeriods first")

        val teacherPairs =
            activeRows
                .map { r -> r.teacherDni?.takeIf { it.isNotBlank() } to r.teacherName.trim() }
                .filter { (_, name) -> name.isNotBlank() }
                .distinctBy { (dni, name) -> dni ?: name }

        val dniPairs = teacherPairs.filter { (dni, _) -> dni != null }
        val namePairs = teacherPairs.filter { (dni, _) -> dni == null }

        val existingDnis =
            if (dniPairs.isNotEmpty()) {
                Teachers
                    .select(Teachers.code)
                    .where { Teachers.code inList dniPairs.map { it.first!! } }
                    .mapNotNull { it[Teachers.code] }
                    .toSet()
            } else {
                emptySet()
            }

        val existingNames =
            if (namePairs.isNotEmpty()) {
                Teachers
                    .select(Teachers.fullName)
                    .where { Teachers.fullName inList namePairs.map { it.second } }
                    .map { it[Teachers.fullName] }
                    .toSet()
            } else {
                emptySet()
            }

        val toInsert = teacherPairs.filter { (dni, name) -> if (dni != null) dni !in existingDnis else name !in existingNames }
        Teachers.batchInsert(toInsert) { (dni, name) ->
            this[Teachers.code] = dni
            this[Teachers.fullName] = name
        }

        return apouId
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.processHourlyLoad(
        meta: CsvMetadata,
        facultyRows: List<ScheduleResume>,
        facultyId: Long,
        apouId: Long,
    ) {
        val activeRows = facultyRows.filter { it.deletedAt == null }
        val lastUpdate =
            activeRows.maxOfOrNull { it.updatedAt } ?: run {
                log.info("R__200: apouId={} — no active rows, skipping", apouId)
                return
            }
        val lastUpdateInstant = lastUpdate.toInstant(ZoneOffset.UTC)

        if (meta.fileLastModified != null) {
            val existingCheckedAt =
                HourlyLoads
                    .select(HourlyLoads.checkedAt)
                    .where { HourlyLoads.academicPeriodOrganizationUnitId eq apouId }
                    .firstOrNull()
                    ?.get(HourlyLoads.checkedAt)
            if (existingCheckedAt != null && !meta.fileLastModified.isAfter(existingCheckedAt)) {
                log.info("R__200: apouId={} — file not newer than checkedAt={}, skipping", apouId, existingCheckedAt)
                return
            }
        }

        val checkedAt = meta.fileLastModified ?: Instant.now()

        HourlyLoads.upsert(
            HourlyLoads.academicPeriodOrganizationUnitId,
            onUpdate = { stmt ->
                stmt[HourlyLoads.checkedAt] = checkedAt
                stmt[HourlyLoads.name] = meta.hourlyLoadName
            },
        ) {
            it[HourlyLoads.academicPeriodOrganizationUnitId] = EntityID(apouId, AcademicPeriodOrganizationUnits)
            it[HourlyLoads.updatedAt] = lastUpdateInstant.minusSeconds(23 * 3600)
            it[HourlyLoads.name] = meta.hourlyLoadName
            it[HourlyLoads.checkedAt] = checkedAt
        }

        val hlRow =
            HourlyLoads
                .select(HourlyLoads.id, HourlyLoads.updatedAt)
                .where { HourlyLoads.academicPeriodOrganizationUnitId eq apouId }
                .first()
        val hourlyLoadId = hlRow[HourlyLoads.id].value
        val updatedAtIn = hlRow[HourlyLoads.updatedAt] ?: Instant.MIN

        val resumes =
            facultyRows
                .filter { it.updatedAt.toInstant(ZoneOffset.UTC) > updatedAtIn && it.deletedAt == null }
                .map { Triple(it.course, it.section.trim(), it.vacancies) }
                .distinctBy { it.first to it.second }

        log.info("R__200: apouId={} — {} course-section(s) to process (updatedAt > {})", apouId, resumes.size, updatedAtIn)
        for ((courseCode, section, vacancies) in resumes) {
            val scheduleExists =
                ScheduleSubjects
                    .join(Schedules, JoinType.INNER, ScheduleSubjects.scheduleId, Schedules.id)
                    .join(Subjects, JoinType.INNER, ScheduleSubjects.subjectId, Subjects.id)
                    .select(ScheduleSubjects.id)
                    .where {
                        (Schedules.sectionId eq EntityID(section, Sections)) and
                            (Subjects.courseId eq EntityID(courseCode, Courses)) and
                            (ScheduleSubjects.hourlyLoadId eq hourlyLoadId)
                    }.limit(1)
                    .any()

            if (!scheduleExists) {
                insertSchedule(courseCode, section, vacancies, hourlyLoadId, facultyId, facultyRows, updatedAtIn)
            } else {
                updateSchedule(courseCode, section, hourlyLoadId, facultyRows, updatedAtIn)
            }
        }

        HourlyLoads.update({ HourlyLoads.id eq hourlyLoadId }) {
            it[HourlyLoads.updatedAt] = lastUpdateInstant
            it[HourlyLoads.publishedAt] = Instant.now()
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.insertSchedule(
        courseCode: String,
        section: String,
        vacancies: Int,
        hourlyLoadId: Long,
        facultyId: Long,
        rows: List<ScheduleResume>,
        updatedAtIn: Instant,
    ) {
        val scheduleId =
            Schedules
                .insertAndGetId {
                    it[Schedules.sectionId] = EntityID(section, Sections)
                    it[Schedules.vacancies] = vacancies
                }.value

        val subjectIds =
            Subjects
                .join(StudyPlans, JoinType.INNER, Subjects.studyPlanId, StudyPlans.id)
                .join(OrganizationUnits, JoinType.INNER, StudyPlans.organizationUnitId, OrganizationUnits.id)
                .select(Subjects.id)
                .where {
                    (Subjects.courseId eq EntityID(courseCode, Courses)) and
                        (OrganizationUnits.parentOrganizationId eq facultyId)
                }.map { it[Subjects.id].value }

        ScheduleSubjects.batchInsert(subjectIds) { subjectId ->
            this[ScheduleSubjects.scheduleId] = EntityID(scheduleId, Schedules)
            this[ScheduleSubjects.subjectId] = EntityID(subjectId, Subjects)
            this[ScheduleSubjects.hourlyLoadId] = EntityID(hourlyLoadId, HourlyLoads)
        }

        val sessions =
            rows.filter {
                it.course == courseCode && it.section.trim() == section &&
                    it.updatedAt.toInstant(ZoneOffset.UTC) > updatedAtIn && it.deletedAt == null
            }
        batchExecInsertClassSessions(sessions, scheduleId, onConflictDoNothing = true)
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.updateSchedule(
        courseCode: String,
        section: String,
        hourlyLoadId: Long,
        rows: List<ScheduleResume>,
        updatedAtIn: Instant,
    ) {
        val scheduleIds =
            ScheduleSubjects
                .join(Schedules, JoinType.INNER, ScheduleSubjects.scheduleId, Schedules.id)
                .join(Subjects, JoinType.INNER, ScheduleSubjects.subjectId, Subjects.id)
                .select(ScheduleSubjects.scheduleId)
                .where {
                    (Schedules.sectionId eq EntityID(section, Sections)) and
                        (Subjects.courseId eq EntityID(courseCode, Courses)) and
                        (ScheduleSubjects.hourlyLoadId eq hourlyLoadId)
                }.map { it[ScheduleSubjects.scheduleId].value }
                .distinct()

        val sessions =
            rows.filter {
                it.course == courseCode && it.section.trim() == section &&
                    it.updatedAt.toInstant(ZoneOffset.UTC) > updatedAtIn && it.deletedAt == null
            }

        for (scheduleId in scheduleIds) {
            ClassSessions.update(
                { (ClassSessions.scheduleId eq scheduleId) and ClassSessions.deletedAt.isNull() },
            ) {
                it[ClassSessions.deletedAt] = Instant.now()
            }

            batchExecInsertClassSessions(sessions, scheduleId, onConflictDoNothing = false)

            Schedules.update({ Schedules.id eq scheduleId }) {
                it[Schedules.updatedAt] = Instant.now()
            }
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.batchExecInsertClassSessions(
        sessions: List<ScheduleResume>,
        scheduleId: Long,
        onConflictDoNothing: Boolean,
    ) {
        if (sessions.isEmpty()) return

        // Bulk-resolve foreign keys (a few SELECTs instead of N×3)
        val classroomCodes = sessions.map { it.classroom.trim() }.distinct()
        val roomIdByCode =
            Classrooms
                .select(Classrooms.id, Classrooms.code)
                .where { Classrooms.code inList classroomCodes }
                .associate { it[Classrooms.code] to it[Classrooms.id].value }

        val typeCodes = sessions.map { it.sessionType }.distinct()
        val typeIdByCode =
            ClassSessionTypes
                .select(ClassSessionTypes.id, ClassSessionTypes.code)
                .where { ClassSessionTypes.code inList typeCodes }
                .associate { it[ClassSessionTypes.code] to it[ClassSessionTypes.id].value }

        val dniList = sessions.mapNotNull { it.teacherDni?.takeIf { d -> d.isNotBlank() } }.distinct()
        val nameList = sessions.filter { it.teacherDni.isNullOrBlank() }.map { it.teacherName.trim() }.distinct()
        val teacherIdByDni: Map<String, Long> =
            if (dniList.isNotEmpty()) {
                Teachers
                    .select(Teachers.id, Teachers.code)
                    .where { Teachers.code inList dniList }
                    .mapNotNull { row -> row[Teachers.code]?.let { code -> code to row[Teachers.id].value } }
                    .toMap()
            } else {
                emptyMap()
            }
        val teacherIdByName: Map<String, Long> =
            if (nameList.isNotEmpty()) {
                Teachers
                    .select(Teachers.id, Teachers.fullName)
                    .where { Teachers.fullName inList nameList }
                    .associate { it[Teachers.fullName] to it[Teachers.id].value }
            } else {
                emptyMap()
            }

        val conflictClause =
            if (onConflictDoNothing) {
                "ON CONFLICT ON CONSTRAINT class_session_pk DO NOTHING"
            } else {
                "ON CONFLICT ON CONSTRAINT class_session_pk DO UPDATE SET deleted_at = NULL"
            }

        val valuePlaceholders = sessions.joinToString(", ") { "(?, ?::time, ?::time, ?, ?, ?, ?)" }
        val args =
            buildList {
                for (r in sessions) {
                    val dayNum = dayNameToNumber(r.day)
                    val typeId = typeIdByCode[r.sessionType]
                    val roomId = roomIdByCode[r.classroom.trim()]
                    val tid =
                        if (r.teacherDni?.isNotBlank() == true) {
                            teacherIdByDni[r.teacherDni]
                        } else {
                            teacherIdByName[r.teacherName.trim()]
                        }
                    add(IntegerColumnType() to dayNum)
                    add(VarCharColumnType() to r.endTime)
                    add(VarCharColumnType() to r.startTime)
                    add(LongColumnType() to typeId)
                    add(LongColumnType() to roomId)
                    add(LongColumnType() to scheduleId)
                    add(LongColumnType() to tid)
                }
            }

        exec(
            """
            INSERT INTO class_session
                (day, end_time, start_time, class_session_type_id, classroom_id, schedule_id, teacher_id)
            VALUES $valuePlaceholders
            $conflictClause
            """.trimIndent(),
            args,
        )
    }

    private fun dayNameToNumber(day: String): Int? {
        val normalized = day.trim().uppercase()
        return when {
            normalized.startsWith("LU") -> 1
            normalized.startsWith("MA") -> 2
            normalized.startsWith("MI") -> 3
            normalized.startsWith("JU") -> 4
            normalized.startsWith("VI") -> 5
            normalized.startsWith("SA") -> 6
            normalized.startsWith("DO") -> 0
            else -> null
        }
    }

    private fun listCsvFiles(): List<Pair<CsvMetadata, List<ScheduleResume>>> {
        return listCsvEntries(prefix = "hl_").mapNotNull { (filename, lastModified) ->
            val name = filename.removePrefix("hl_").removeSuffix(".csv")
            val meta = parseFilename(name, lastModified) ?: return@mapNotNull null
            val rows = loadCsv("db/data/$filename", meta.defaultFacultyCode)
            meta to rows
        }
    }

    private fun parseFilename(
        name: String,
        lastModified: Instant? = null,
    ): CsvMetadata? {
        val lastUnderscore = name.lastIndexOf('_')
        if (lastUnderscore <= 0) return null
        val facultyCode = name.substring(lastUnderscore + 1)
        val beforeFaculty = name.substring(0, lastUnderscore)
        val secondLastUnderscore = beforeFaculty.lastIndexOf('_')
        if (secondLastUnderscore < 0) return null
        return CsvMetadata(
            hourlyLoadName = beforeFaculty.substring(0, secondLastUnderscore),
            academicCode = beforeFaculty.substring(secondLastUnderscore + 1),
            defaultFacultyCode = facultyCode,
            fileLastModified = lastModified,
        )
    }

    private fun loadCsv(
        resourcePath: String,
        defaultFacultyCode: String,
    ): List<ScheduleResume> {
        val stream = openClasspathResource(resourcePath) ?: return emptyList()
        val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val timeFmt = DateTimeFormatter.ofPattern("HH:mm")

        fun parseTime(s: String): String {
            val trimmed = s.trim()
            val hour = trimmed.toIntOrNull()
            return if (hour != null) LocalTime.of(hour, 0).format(timeFmt) else LocalTime.parse(trimmed).format(timeFmt)
        }
        return stream.bufferedReader().useLines { lines ->
            val iter = lines.filter { it.isNotBlank() }.iterator()
            if (!iter.hasNext()) return@useLines emptyList()
            val headerLine = iter.next()
            val delimiter = if (headerLine.contains(';')) ';' else ','
            val header = parseCsvLine(headerLine, delimiter).map { it.trim().lowercase() }

            fun idx(name: String) =
                header.indexOf(name).also {
                    require(it >= 0) { "Column '$name' not found in CSV header of $resourcePath" }
                }

            fun optIdx(name: String) = header.indexOf(name).takeIf { it >= 0 }
            val iCodigoFacultad = optIdx(COL_FACULTY_CODE)
            val iCurso = idx(COL_COURSE)
            val iSeccion = idx(COL_SECTION)
            val iVacantes = idx(COL_VACANCIES)
            val iUpdatedAt = idx(COL_UPDATED_AT)
            val iDeletedAt = idx(COL_DELETED_AT)
            val iInicio = idx(COL_START_TIME)
            val iFin = idx(COL_END_TIME)
            val iAula = idx(COL_CLASSROOM)
            val iDni = optIdx(COL_DNI)
            val iDocente = idx(COL_TEACHER)
            val iTipo = idx(COL_TYPE)
            val iDia = idx(COL_DAY)
            iter
                .asSequence()
                .map { line ->
                    val cols = parseCsvLine(line, delimiter)
                    ScheduleResume(
                        facultyCode = iCodigoFacultad?.let { cols[it].trim().takeIf { v -> v.isNotBlank() } } ?: defaultFacultyCode,
                        course = cols[iCurso].trim(),
                        section = cols[iSeccion].trim(),
                        vacancies = cols[iVacantes].toInt(),
                        updatedAt = LocalDateTime.parse(cols[iUpdatedAt], fmt),
                        deletedAt = cols[iDeletedAt].takeIf { it.isNotBlank() }?.let { LocalDateTime.parse(it, fmt) },
                        startTime = parseTime(cols[iInicio]),
                        endTime = parseTime(cols[iFin]),
                        classroom = cols[iAula].trim().uppercase(),
                        teacherDni = iDni?.let { cols[it].trim().takeIf { v -> v.isNotBlank() } },
                        teacherName = cols[iDocente],
                        sessionType = cols[iTipo].trim().uppercase(),
                        day = cols[iDia],
                    )
                }.toList()
        }
    }
}

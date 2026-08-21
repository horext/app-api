package db.migration

import db.csv.CsvSource
import io.octatec.horext.api.repository.table.Courses
import io.octatec.horext.api.repository.table.OrganizationUnits
import io.octatec.horext.api.repository.table.StudyPlans
import io.octatec.horext.api.repository.table.SubjectRelationships
import io.octatec.horext.api.repository.table.SubjectTypes
import io.octatec.horext.api.repository.table.Subjects
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import java.security.MessageDigest
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.zip.CRC32

@Suppress("ClassName")
class R__050_SeedStudyPlans : BaseCsvMigration() {
    companion object {
        private const val STUDY_PLANS_FILE = "db/data/study_plans.csv"
        private const val SUBJECTS_PREFIX = "study_plan_subjects_"
        private const val RELATIONSHIPS_PREFIX = "study_plan_relationships_"
        private const val CSV_EXT = ".csv"

        private const val COL_STUDY_PLAN_CODE = "code"
        private const val COL_FROM_DATE = "from_date"
        private const val COL_ORGANIZATION_UNIT_CODE = "organization_unit_code"

        private const val COL_COURSE_ID = "course_id"
        private const val COL_COURSE_NAME = "course_name"
        private const val COL_CREDITS = "credits"
        private const val COL_CYCLE = "cycle"
        private const val COL_SUBJECT_TYPE_ID = "subject_type_id"
        private const val COL_EVALUATION_SYSTEM_ID = "evaluation_system_id"
        private const val COL_TOTAL_WEEKLY_HOURS = "total_weekly_hours"
        private const val COL_WEEKLY_THEORY_HOURS = "weekly_theory_hours"
        private const val COL_WEEKLY_PRACTICE_HOURS = "weekly_practice_hours"
        private const val COL_WEEKLY_PRACTICE_LABORATORY_HOURS = "weekly_practice_laboratory_hours"
        private const val COL_WEEKLY_LABORATORY_HOURS = "weekly_laboratory_hours"
        private const val COL_MAX_CYCLE = "max_cycle"
        private const val COL_MIN_CYCLE = "min_cycle"
        private const val COL_NOTE = "note"
        private const val COL_REQUIRED_CREDITS = "required_credits"
        private const val COL_POSITION = "position"

        private const val COL_RELATIONSHIP_FROM = "from_course_id"
        private const val COL_RELATIONSHIP_TO = "to_course_id"
        private const val COL_RELATIONSHIP_TYPE_ID = "relationship_type_id"

        private val TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    private data class CsvTable(
        val path: String,
        val headers: List<String>,
        val rows: List<CsvRow>,
    )

    private data class CsvRow(
        val rowNumber: Int,
        val values: List<String>,
    )

    private class HeaderIndex(
        private val path: String,
        headers: List<String>,
    ) {
        private val normalizedHeaders =
            headers.mapIndexed { index, value ->
                normalizeHeader(
                    if (index == 0) value.removePrefix("\uFEFF") else value,
                )
            }

        private val indexes =
            normalizedHeaders
                .mapIndexed { index, value -> value to index }
                .toMap()

        init {
            val duplicates =
                normalizedHeaders
                    .filter { it.isNotBlank() }
                    .groupingBy { it }
                    .eachCount()
                    .filterValues { it > 1 }
                    .keys

            require(duplicates.isEmpty()) {
                "Duplicate CSV headers in '$path': ${duplicates.sorted().joinToString()}"
            }
        }

        fun required(name: String): Int =
            indexes[normalizeHeader(name)]
                ?: error(
                    "Missing required CSV column '$name' in '$path'. " +
                        "Available columns: ${normalizedHeaders.joinToString()}",
                )

        fun optional(name: String): Int? = indexes[normalizeHeader(name)]

        companion object {
            private fun normalizeHeader(value: String): String = value.trim().lowercase()
        }
    }

    override fun getChecksum(): Int {
        val crc = CRC32()

        val allFiles =
            listOf(STUDY_PLANS_FILE) +
                csvResourcePaths(SUBJECTS_PREFIX) +
                csvResourcePaths(RELATIONSHIPS_PREFIX)

        for (path in allFiles.distinct().sorted()) {
            openClasspathResource(path)?.use { crc.update(it.readBytes()) }
        }

        return crc.value.toInt()
    }

    override fun migrate(context: Context) {
        if (shouldSkip(context)) {
            log.info("R__050_SeedStudyPlans: skipSeeds is true, skipping migration")
            return
        }

        val db =
            Database.connect(
                SingleConnectionDataSource(context.connection, true),
            )

        transaction(db) {
            seedStudyPlans()
            seedAllSubjects()
            seedAllRelationships()
        }

        log.info("R__050_SeedStudyPlans: completed")
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedStudyPlans() {
        val table = readCsv(STUDY_PLANS_FILE)

        if (table == null) {
            log.info("R__050_SeedStudyPlans: file $STUDY_PLANS_FILE not found, skipping")
            return
        }

        log.info("R__050_SeedStudyPlans: seeding $STUDY_PLANS_FILE")

        val header = HeaderIndex(table.path, table.headers)

        val iCode = header.required(COL_STUDY_PLAN_CODE)
        val iFromDate = header.required(COL_FROM_DATE)
        val iOrganizationUnit = header.required(COL_ORGANIZATION_UNIT_CODE)

        val duplicatedCodes =
            table.rows
                .map { row -> requiredValue(row, iCode, COL_STUDY_PLAN_CODE, table.path) }
                .groupingBy { it }
                .eachCount()
                .filterValues { it > 1 }
                .keys

        require(duplicatedCodes.isEmpty()) {
            "Duplicate study plan codes in '${table.path}': ${duplicatedCodes.sorted().joinToString()}"
        }

        for (row in table.rows) {
            val studyPlanCode = requiredValue(row, iCode, COL_STUDY_PLAN_CODE, table.path)
            val organizationUnitCode =
                requiredValue(row, iOrganizationUnit, COL_ORGANIZATION_UNIT_CODE, table.path)
            val fromDate =
                parseOptionalInstant(
                    value = value(row, iFromDate),
                    field = COL_FROM_DATE,
                    path = table.path,
                    rowNumber = row.rowNumber,
                )
            val sourceChecksum =
                calculateStudyPlanChecksum(
                    code = studyPlanCode,
                    fromDate = fromDate,
                    organizationUnitCode = organizationUnitCode,
                )

            val organizationUnitId =
                OrganizationUnits
                    .selectAll()
                    .where { OrganizationUnits.code eq organizationUnitCode }
                    .firstOrNull()
                    ?.get(OrganizationUnits.id)
                    ?.value
                    ?: error(
                        "Organization unit '$organizationUnitCode' not found " +
                            "for study plan '$studyPlanCode' at row ${row.rowNumber} in '${table.path}'",
                    )

            val existingStudyPlan =
                StudyPlans
                    .select(StudyPlans.sourceChecksum)
                    .where { StudyPlans.code eq studyPlanCode }
                    .firstOrNull()

            if (existingStudyPlan?.get(StudyPlans.sourceChecksum) == sourceChecksum) {
                continue
            }

            if (existingStudyPlan == null) {
                StudyPlans.insert {
                    it[StudyPlans.code] = studyPlanCode
                    it[StudyPlans.fromDate] = fromDate
                    it[StudyPlans.organizationUnitId] =
                        EntityID(organizationUnitId, OrganizationUnits)
                    it[StudyPlans.sourceChecksum] = sourceChecksum
                }
            } else {
                StudyPlans.update({ StudyPlans.code eq studyPlanCode }) {
                    it[StudyPlans.fromDate] = fromDate
                    it[StudyPlans.organizationUnitId] = EntityID(organizationUnitId, OrganizationUnits)
                    it[StudyPlans.updatedAt] = Instant.now()
                    it[StudyPlans.sourceChecksum] = sourceChecksum
                }
            }
        }
    }

    private fun calculateStudyPlanChecksum(
        code: String,
        fromDate: Instant?,
        organizationUnitCode: String,
    ): String =
        MessageDigest
            .getInstance("SHA-256")
            .digest(listOf(code, fromDate?.toString().orEmpty(), organizationUnitCode).joinToString("\u0000").toByteArray())
            .joinToString("") { byte -> "%02x".format(byte.toInt() and 0xff) }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedAllSubjects() {
        val allStudyPlans =
            StudyPlans
                .selectAll()
                .associate {
                    it[StudyPlans.code] to it[StudyPlans.id].value
                }

        val allSubjectTypes =
            SubjectTypes
                .selectAll()
                .associate {
                    it[SubjectTypes.id].value to it[SubjectTypes.id]
                }

        for (path in csvResourcePaths(SUBJECTS_PREFIX).sorted()) {
            val studyPlanCode =
                path
                    .removePrefix("db/data/$SUBJECTS_PREFIX")
                    .removeSuffix(CSV_EXT)

            val table = readCsv(path) ?: continue

            val planId =
                allStudyPlans[studyPlanCode]
                    ?: error("Study plan '$studyPlanCode' not found for file '$path'")

            seedSubjects(
                planId = planId,
                studyPlanCode = studyPlanCode,
                table = table,
                allSubjectTypes = allSubjectTypes,
            )
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedSubjects(
        planId: Long,
        studyPlanCode: String,
        table: CsvTable,
        allSubjectTypes: Map<Long, EntityID<Long>>,
    ) {
        log.info("R__050_SeedStudyPlans: seeding ${table.path}")

        val header = HeaderIndex(table.path, table.headers)

        val iCourse = header.required(COL_COURSE_ID)
        val iName = header.required(COL_COURSE_NAME)

        val iCredits = header.optional(COL_CREDITS)
        val iCycle = header.optional(COL_CYCLE)
        val iSubjectType = header.optional(COL_SUBJECT_TYPE_ID)
        val iEvaluationSystem = header.optional(COL_EVALUATION_SYSTEM_ID)
        val iTotalWeeklyHours = header.optional(COL_TOTAL_WEEKLY_HOURS)
        val iWeeklyTheoryHours = header.optional(COL_WEEKLY_THEORY_HOURS)
        val iWeeklyPracticeHours = header.optional(COL_WEEKLY_PRACTICE_HOURS)
        val iWeeklyPracticeLaboratoryHours = header.optional(COL_WEEKLY_PRACTICE_LABORATORY_HOURS)
        val iWeeklyLaboratoryHours = header.optional(COL_WEEKLY_LABORATORY_HOURS)
        val iMaxCycle = header.optional(COL_MAX_CYCLE)
        val iMinCycle = header.optional(COL_MIN_CYCLE)
        val iNote = header.optional(COL_NOTE)
        val iRequiredCredits = header.optional(COL_REQUIRED_CREDITS)
        val iPosition = header.optional(COL_POSITION)

        val duplicatedCourses =
            table.rows
                .map { row -> requiredValue(row, iCourse, COL_COURSE_ID, table.path) }
                .groupingBy { it }
                .eachCount()
                .filterValues { it > 1 }
                .keys

        require(duplicatedCourses.isEmpty()) {
            "Duplicate courses in '${table.path}': ${duplicatedCourses.sorted().joinToString()}"
        }

        for (row in table.rows) {
            val courseCode = requiredValue(row, iCourse, COL_COURSE_ID, table.path)
            val courseName = value(row, iName)

            Courses.upsert {
                it[Courses.id] = EntityID(courseCode, Courses)
                it[Courses.name] = courseName.takeIf { name -> name.isNotBlank() }
                it[Courses.updatedAt] = Instant.now()
            }
        }

        for (row in table.rows) {
            val courseCode = requiredValue(row, iCourse, COL_COURSE_ID, table.path)

            val subjectTypeId =
                parseOptionalLong(
                    value = value(row, iSubjectType),
                    field = COL_SUBJECT_TYPE_ID,
                    path = table.path,
                    rowNumber = row.rowNumber,
                    courseCode = courseCode,
                )?.let { id ->
                    allSubjectTypes[id]
                        ?: error(
                            "Unknown $COL_SUBJECT_TYPE_ID '$id' for course '$courseCode' " +
                                "at row ${row.rowNumber} in '${table.path}'",
                        )
                }

            Subjects.upsert(
                Subjects.courseId,
                Subjects.studyPlanId,
            ) {
                it[Subjects.courseId] = EntityID(courseCode, Courses)
                it[Subjects.studyPlanId] = EntityID(planId, StudyPlans)
                it[Subjects.updatedAt] = Instant.now()

                it[Subjects.credits] =
                    parseOptionalInt(
                        value(row, iCredits),
                        COL_CREDITS,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.cycle] =
                    parseOptionalInt(
                        value(row, iCycle),
                        COL_CYCLE,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.typeId] = subjectTypeId

                it[Subjects.evaluationSystemId] =
                    parseOptionalLong(
                        value(row, iEvaluationSystem),
                        COL_EVALUATION_SYSTEM_ID,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.totalWeeklyHours] =
                    parseOptionalInt(
                        value(row, iTotalWeeklyHours),
                        COL_TOTAL_WEEKLY_HOURS,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.weeklyTheoryHours] =
                    parseOptionalInt(
                        value(row, iWeeklyTheoryHours),
                        COL_WEEKLY_THEORY_HOURS,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.weeklyPracticeHours] =
                    parseOptionalInt(
                        value(row, iWeeklyPracticeHours),
                        COL_WEEKLY_PRACTICE_HOURS,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.weeklyPracticeLaboratoryHours] =
                    parseOptionalInt(
                        value(row, iWeeklyPracticeLaboratoryHours),
                        COL_WEEKLY_PRACTICE_LABORATORY_HOURS,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.weeklyLaboratoryHours] =
                    parseOptionalInt(
                        value(row, iWeeklyLaboratoryHours),
                        COL_WEEKLY_LABORATORY_HOURS,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.maxCycle] =
                    parseOptionalInt(
                        value(row, iMaxCycle),
                        COL_MAX_CYCLE,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.minCycle] =
                    parseOptionalInt(
                        value(row, iMinCycle),
                        COL_MIN_CYCLE,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.note] =
                    value(row, iNote).takeIf { note -> note.isNotBlank() }

                it[Subjects.requiredCredits] =
                    parseOptionalInt(
                        value(row, iRequiredCredits),
                        COL_REQUIRED_CREDITS,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )

                it[Subjects.position] =
                    parseOptionalInt(
                        value(row, iPosition),
                        COL_POSITION,
                        table.path,
                        row.rowNumber,
                        courseCode,
                    )
            }
        }

        log.info(
            "R__050_SeedStudyPlans: seeded {} subjects for study plan {}",
            table.rows.size,
            studyPlanCode,
        )
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedAllRelationships() {
        val allStudyPlans =
            StudyPlans
                .selectAll()
                .associate {
                    it[StudyPlans.code] to it[StudyPlans.id].value
                }

        for (path in csvResourcePaths(RELATIONSHIPS_PREFIX).sorted()) {
            val studyPlanCode =
                path
                    .removePrefix("db/data/$RELATIONSHIPS_PREFIX")
                    .removeSuffix(CSV_EXT)

            val table = readCsv(path) ?: continue

            val planId =
                allStudyPlans[studyPlanCode]
                    ?: error("Study plan '$studyPlanCode' not found for file '$path'")

            seedRelationships(
                planId = planId,
                studyPlanCode = studyPlanCode,
                table = table,
            )
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedRelationships(
        planId: Long,
        studyPlanCode: String,
        table: CsvTable,
    ) {
        log.info("R__050_SeedStudyPlans: seeding ${table.path}")

        val header = HeaderIndex(table.path, table.headers)

        val iFrom = relationshipFromIndex(header)
        val iTo = relationshipToIndex(header)
        val iType = header.required(COL_RELATIONSHIP_TYPE_ID)

        val subjectsByCourse =
            Subjects
                .selectAll()
                .where { Subjects.studyPlanId eq planId }
                .associate {
                    it[Subjects.courseId].value to it[Subjects.id].value
                }

        if (subjectsByCourse.isNotEmpty()) {
            SubjectRelationships.deleteWhere {
                SubjectRelationships.relatedSubjectId inList subjectsByCourse.values.toList()
            }
        }

        val rows =
            table.rows.map { row ->
                val fromCourse =
                    requiredValue(row, iFrom, table.headers[iFrom], table.path)

                val toCourse =
                    requiredValue(row, iTo, table.headers[iTo], table.path)

                val fromId =
                    subjectsByCourse[fromCourse]
                        ?: error(
                            "Relationship source course '$fromCourse' not found in study plan " +
                                "'$studyPlanCode' at row ${row.rowNumber} in '${table.path}'",
                        )

                val toId =
                    subjectsByCourse[toCourse]
                        ?: error(
                            "Relationship target course '$toCourse' not found in study plan " +
                                "'$studyPlanCode' at row ${row.rowNumber} in '${table.path}'",
                        )

                val relationshipTypeId =
                    parseRequiredLong(
                        value = value(row, iType),
                        field = COL_RELATIONSHIP_TYPE_ID,
                        path = table.path,
                        rowNumber = row.rowNumber,
                    )

                Triple(fromId, toId, relationshipTypeId)
            }

        SubjectRelationships.batchInsert(rows) { (fromId, toId, relationshipTypeId) ->
            this[SubjectRelationships.relatedSubjectId] = fromId
            this[SubjectRelationships.subjectId] = toId
            this[SubjectRelationships.relationshipTypeId] = relationshipTypeId
        }

        log.info(
            "R__050_SeedStudyPlans: seeded {} relationships for study plan {}",
            rows.size,
            studyPlanCode,
        )
    }

    /**
     * Keeps compatibility with existing relationship CSVs if they use
     * course_id / related_course_id instead of the preferred explicit names.
     */
    private fun relationshipFromIndex(header: HeaderIndex): Int =
        header.optional(COL_RELATIONSHIP_FROM)
            ?: header.optional("related_course_id")
            ?: header.optional("from_course")
            ?: error(
                "Missing relationship source column. Expected one of: " +
                    "'$COL_RELATIONSHIP_FROM', 'related_course_id', 'from_course'",
            )

    private fun relationshipToIndex(header: HeaderIndex): Int =
        header.optional(COL_RELATIONSHIP_TO)
            ?: header.optional("course_id")
            ?: header.optional("to_course")
            ?: error(
                "Missing relationship target column. Expected one of: " +
                    "'$COL_RELATIONSHIP_TO', 'course_id', 'to_course'",
            )

    private fun readCsv(path: String): CsvTable? {
        val stream = openClasspathResource(path) ?: return null
        var headers = emptyList<String>()
        val rows =
            CsvSource(requireConsistentRecords = false)
                .read(
                    file = path,
                    input = stream,
                    onHeaders = { parsed ->
                        headers =
                            parsed.mapIndexed { index, value ->
                                (if (index == 0) value.removePrefix("\uFEFF") else value).trim()
                            }
                    },
                ) { context ->
                    CsvRow(
                        rowNumber = context.rowNumber.toInt(),
                        values = context.record.toList(),
                    )
                }.filterNot { row -> row.values.all { it.isBlank() } }

        if (headers.isEmpty()) error("CSV file '$path' is empty")
        require(headers.any { it.isNotBlank() }) { "CSV file '$path' has an empty header" }
        return CsvTable(path = path, headers = headers, rows = rows)
    }

    private fun requiredValue(
        row: CsvRow,
        index: Int,
        field: String,
        path: String,
    ): String {
        val result = value(row, index)

        require(result.isNotBlank()) {
            "Required field '$field' is blank at row ${row.rowNumber} in '$path'"
        }

        return result
    }

    private fun value(
        row: CsvRow,
        index: Int?,
    ): String =
        index
            ?.let { row.values.getOrNull(it) }
            ?.trim()
            .orEmpty()

    private fun parseOptionalInt(
        value: String,
        field: String,
        path: String,
        rowNumber: Int,
        courseCode: String,
    ): Int? {
        val trimmed = value.trim()

        if (trimmed.isBlank()) {
            return null
        }

        return trimmed.toIntOrNull()
            ?: error(
                "Invalid integer '$trimmed' for '$field' of course '$courseCode' " +
                    "at row $rowNumber in '$path'",
            )
    }

    private fun parseOptionalLong(
        value: String,
        field: String,
        path: String,
        rowNumber: Int,
        courseCode: String,
    ): Long? {
        val trimmed = value.trim()

        if (trimmed.isBlank()) {
            return null
        }

        return trimmed.toLongOrNull()
            ?: error(
                "Invalid number '$trimmed' for '$field' of course '$courseCode' " +
                    "at row $rowNumber in '$path'",
            )
    }

    private fun parseRequiredLong(
        value: String,
        field: String,
        path: String,
        rowNumber: Int,
    ): Long {
        val trimmed = value.trim()

        require(trimmed.isNotBlank()) {
            "Required field '$field' is blank at row $rowNumber in '$path'"
        }

        return trimmed.toLongOrNull()
            ?: error(
                "Invalid number '$trimmed' for '$field' at row $rowNumber in '$path'",
            )
    }

    private fun parseOptionalInstant(
        value: String,
        field: String,
        path: String,
        rowNumber: Int,
    ): Instant? {
        val trimmed = value.trim()

        if (trimmed.isBlank()) {
            return null
        }

        return try {
            LocalDateTime
                .parse(trimmed, TIMESTAMP_FMT)
                .toInstant(ZoneOffset.UTC)
        } catch (e: Exception) {
            throw IllegalArgumentException(
                "Invalid timestamp '$trimmed' for '$field' at row $rowNumber in '$path'. " +
                    "Expected format: yyyy-MM-dd HH:mm:ss",
                e,
            )
        }
    }
}

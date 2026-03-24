package db.migration

import io.octatec.horext.api.domain.Courses
import io.octatec.horext.api.domain.Contributions
import io.octatec.horext.api.domain.StudyPlanContributions
import io.octatec.horext.api.domain.OrganizationUnits
import io.octatec.horext.api.domain.StudyPlans
import io.octatec.horext.api.domain.SubjectRelationships
import io.octatec.horext.api.domain.SubjectTypes
import io.octatec.horext.api.domain.Subjects
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.javatime.timestamp
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.jdbc.datasource.SingleConnectionDataSource
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
        private val TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.recordContribution(path: String, entityId: Long) {
        getOrCreateContributionId(path)?.let { contribId ->
            StudyPlanContributions.upsert(
                StudyPlanContributions.studyPlanId,
                StudyPlanContributions.contributionId,
            ) {
                it[StudyPlanContributions.studyPlanId] = EntityID(entityId, StudyPlans)
                it[StudyPlanContributions.contributionId] = EntityID(contribId, Contributions)
            }
        }
    }

    override fun getChecksum(): Int {
        val crc = CRC32()
        val allFiles =
            listOf(STUDY_PLANS_FILE) +
                csvResourcePaths(SUBJECTS_PREFIX) +
                csvResourcePaths(RELATIONSHIPS_PREFIX)
        for (path in allFiles) {
            openClasspathResource(path)?.use { crc.update(it.readBytes()) }
        }
        return crc.value.toInt()
    }

    override fun migrate(context: Context) {
        if (shouldSkip(context)) {
            log.info("R__050_SeedStudyPlans: skipSeeds is true, skipping migration")
            return
        }
        val db = Database.connect(SingleConnectionDataSource(context.connection, true))
        transaction(db) {
            seedStudyPlans()
            seedAllSubjects()
            seedAllRelationships()
        }
        log.info("R__050_SeedStudyPlans: completed")
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedStudyPlans() {
        val stream = openClasspathResource(STUDY_PLANS_FILE)
        if (stream == null) {
            log.info("R__050_SeedStudyPlans: file $STUDY_PLANS_FILE not found, skipping")
            return
        }
        log.info("R__050_SeedStudyPlans: seeding $STUDY_PLANS_FILE")

        val lines =
            bomAwareReader(stream).useLines { seq ->
                seq
                    .drop(1)
                    .filter { it.isNotBlank() }
                    .map { parseCsvLine(it) }
                    .filter { it.size >= 3 }
                    .toList()
            }

        for (cols in lines) {
            val orgUnitId =
                OrganizationUnits
                    .selectAll()
                    .where { OrganizationUnits.code eq cols[2] }
                    .firstOrNull()
                    ?.get(OrganizationUnits.id)
                    ?.value
                    ?: error("Organization unit not found: '${cols[2]}'")
            val studyPlanId =
                StudyPlans.upsert(StudyPlans.code) {
                    it[StudyPlans.code] = cols[0]
                    it[StudyPlans.fromDate] = parseInstant(cols[1])
                    it[StudyPlans.organizationUnitId] = EntityID(orgUnitId, OrganizationUnits)
                }[StudyPlans.id].value
            recordContribution(STUDY_PLANS_FILE, studyPlanId)
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedAllSubjects() {
        val allStudyPlans = StudyPlans.selectAll().associate { it[StudyPlans.code] to it[StudyPlans.id].value }
        val allSubjectTypes = SubjectTypes.selectAll().associate { it[SubjectTypes.id].value to it[SubjectTypes.id] }

        for (path in csvResourcePaths(SUBJECTS_PREFIX)) {
            val code = path.removePrefix("db/data/$SUBJECTS_PREFIX").removeSuffix(".csv")
            val stream = openClasspathResource(path) ?: continue
            val planId = allStudyPlans[code] ?: error("Study plan not found for code: $code")
            seedSubjects(planId, code, stream, allSubjectTypes)
            recordContribution(path, planId)
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedSubjects(
        planId: Long,
        studyPlanCode: String,
        stream: java.io.InputStream,
        allSubjectTypes: Map<Long, EntityID<Long>>,
    ) {
        log.info("R__050_SeedStudyPlans: seeding ${SUBJECTS_PREFIX}$studyPlanCode$CSV_EXT")

        val lines =
            bomAwareReader(stream).useLines { seq ->
                seq
                    .drop(1)
                    .filter { it.isNotBlank() }
                    .map { parseCsvLine(it) }
                    .filter { it.size >= 16 }
                    .toList()
            }

        // Batch courses upsert
        for (c in lines) {
            Courses.upsert {
                it[Courses.id] = EntityID(c[0], Courses)
                it[Courses.name] = c[1].takeIf { s -> s.isNotBlank() }
            }
        }

        for (c in lines) {
            Subjects.upsert(Subjects.courseId, Subjects.studyPlanId) {
                it[Subjects.courseId] = EntityID(c[0], Courses)
                it[Subjects.studyPlanId] = EntityID(planId, StudyPlans)
                it[Subjects.credits] = c[2].toIntOrNull()
                it[Subjects.cycle] = c[3].toIntOrNull()
                it[Subjects.typeId] = c[4].toLongOrNull()?.let { id -> allSubjectTypes[id] }
                it[Subjects.evaluationSystemId] = c[5].toLongOrNull()
                it[Subjects.totalWeeklyHours] = c[6].toIntOrNull()
                it[Subjects.weeklyTheoryHours] = c[7].toIntOrNull()
                it[Subjects.weeklyPracticeHours] = c[8].toIntOrNull()
                it[Subjects.weeklyPracticeLaboratoryHours] = c[9].toIntOrNull()
                it[Subjects.weeklyLaboratoryHours] = c[10].toIntOrNull()
                it[Subjects.maxCycle] = c[11].toIntOrNull()
                it[Subjects.minCycle] = c[12].toIntOrNull()
                it[Subjects.note] = c[13].takeIf { s -> s.isNotBlank() }
                it[Subjects.requiredCredits] = c[14].toIntOrNull()
                it[Subjects.position] = c[15].toIntOrNull()
            }
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedAllRelationships() {
        val allStudyPlans = StudyPlans.selectAll().associate { it[StudyPlans.code] to it[StudyPlans.id].value }

        for (path in csvResourcePaths(RELATIONSHIPS_PREFIX)) {
            val code = path.removePrefix("db/data/$RELATIONSHIPS_PREFIX").removeSuffix(".csv")
            val stream = openClasspathResource(path) ?: continue
            val planId = allStudyPlans[code] ?: error("Study plan not found for code: $code")
            seedRelationships(planId, code, stream)
            recordContribution(path, planId)
        }
    }

    private fun org.jetbrains.exposed.v1.jdbc.JdbcTransaction.seedRelationships(
        planId: Long,
        studyPlanCode: String,
        stream: java.io.InputStream,
    ) {
        log.info("R__050_SeedStudyPlans: seeding ${RELATIONSHIPS_PREFIX}$studyPlanCode$CSV_EXT")

        val subjectsByCourse =
            Subjects
                .selectAll()
                .where { Subjects.studyPlanId eq planId }
                .associate { it[Subjects.courseId].value to it[Subjects.id].value }

        if (subjectsByCourse.isNotEmpty()) {
            SubjectRelationships.deleteWhere {
                SubjectRelationships.relatedSubjectId inList subjectsByCourse.values.toList()
            }
        }

        val rows =
            bomAwareReader(stream).useLines { seq ->
                seq
                    .drop(1)
                    .filter { it.isNotBlank() }
                    .mapNotNull { line ->
                        val c = parseCsvLine(line)
                        if (c.size < 3) return@mapNotNull null
                        val fromId = subjectsByCourse[c[0]] ?: return@mapNotNull null
                        val toId = subjectsByCourse[c[1]] ?: return@mapNotNull null
                        val relType = c[2].toLongOrNull() ?: return@mapNotNull null
                        Triple(fromId, toId, relType)
                    }.toList()
            }

        SubjectRelationships.batchInsert(rows) { (fromId, toId, relType) ->
            this[SubjectRelationships.relatedSubjectId] = fromId
            this[SubjectRelationships.subjectId] = toId
            this[SubjectRelationships.relationshipTypeId] = relType
        }
    }

    private fun parseInstant(s: String): Instant? =
        s.trim().takeIf { it.isNotBlank() }?.let {
            LocalDateTime.parse(it, TIMESTAMP_FMT).toInstant(ZoneOffset.UTC)
        }
}

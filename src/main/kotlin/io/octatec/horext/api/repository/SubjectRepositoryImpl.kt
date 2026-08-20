package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.repository.table.Courses
import io.octatec.horext.api.repository.table.OrganizationUnits
import io.octatec.horext.api.repository.table.ScheduleSubjects
import io.octatec.horext.api.repository.table.StudyPlans
import io.octatec.horext.api.repository.table.SubjectRelationships
import io.octatec.horext.api.repository.table.SubjectTypes
import io.octatec.horext.api.repository.table.Subjects
import io.octatec.horext.api.util.ilike
import io.octatec.horext.api.util.unaccent
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.exists
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.select
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class SubjectRepositoryImpl : SubjectRepository {
    override fun getAllByStudyPlanId(studyPlanId: Long): List<Subject> {
        val s = Subjects
        val c = Courses
        val st = SubjectTypes
        val sr = SubjectRelationships
        val subjects =
            s
                .innerJoin(c)
                .leftJoin(st)
                .select(s.entityColumns + c.columns + st.columns)
                .where {
                    (s.studyPlanId eq studyPlanId)
                }.orderBy(c.id to SortOrder.ASC)
                .map { row -> s.createEntity(row) }

        if (subjects.isEmpty()) return emptyList()

        val relationships =
            sr
                .select(sr.columns)
                .where { sr.subjectId inList subjects.map { it.id } }
                .map { row -> sr.createEntity(row) }

        val relationshipsBySubjectId = relationships.groupBy { it.subjectId }
        subjects.forEach { subject ->
            subject.relationships = relationshipsBySubjectId[subject.id].orEmpty()
        }
        return subjects
    }

    override fun getAllBySearchAndSpecialityIdAndHourlyLoad(
        search: String,
        specialityId: Long,
        hourlyLoadId: Long,
    ): List<Subject> {
        val s = Subjects
        val c = Courses
        val sp = StudyPlans
        val ss = ScheduleSubjects
        val st = SubjectTypes
        return s
            .innerJoin(c)
            .innerJoin(sp)
            .leftJoin(st)
            .select(s.entityColumns + c.columns + sp.columns + st.columns)
            .where {
                (sp.organizationUnitId eq specialityId) and
                    sp.isActive() and
                    ss.existsForSubjectAndHourlyLoad(s, hourlyLoadId) and
                    c.matchesSearch(search)
            }.orderByStudyPlanAndCourse(sp, c, s)
            .map { row -> s.createEntity(row) }
    }

    override fun getPageBySearchAndSpecialityIdAndHourlyLoad(
        search: String,
        specialityId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int,
    ): Page<Subject> {
        val s = Subjects
        val c = Courses
        val sp = StudyPlans
        val st = SubjectTypes
        val ss = ScheduleSubjects
        val query =
            s
                .innerJoin(c)
                .innerJoin(sp)
                .leftJoin(st)
                .select(s.entityColumns + c.columns + sp.columns + st.columns)
                .where {
                    (sp.organizationUnitId eq specialityId) and
                        sp.isActive() and
                        ss.existsForSubjectAndHourlyLoad(s, hourlyLoadId) and
                        c.matchesSearch(search)
                }.orderByStudyPlanAndCourse(sp, c, s)
        return query.toSubjectPage(offset, limit)
    }

    override fun getPageBySearchAndFacultyIdAndHourlyLoad(
        search: String,
        facultyId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int,
    ): Page<Subject> {
        val s = Subjects
        val c = Courses
        val sp = StudyPlans
        val st = SubjectTypes
        val ss = ScheduleSubjects
        val ou = OrganizationUnits
        val query =
            s
                .innerJoin(c)
                .innerJoin(sp)
                .innerJoin(ou)
                .leftJoin(st)
                .select(s.entityColumns + c.columns + sp.columns + st.columns + ou.columns)
                .where {
                    (ou.parentOrganizationId eq facultyId) and
                        sp.isActive() and
                        ss.existsForSubjectAndHourlyLoad(s, hourlyLoadId) and
                        c.matchesSearch(search)
                }.orderByStudyPlanAndCourse(sp, c, s)
        return query.toSubjectPage(offset, limit)
    }

    override fun getPageBySearchAndStudyPlanIdAndHourlyLoad(
        search: String,
        studyPlanId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int,
    ): Page<Subject> {
        val s = Subjects
        val c = Courses
        val sp = StudyPlans
        val st = SubjectTypes
        val ss = ScheduleSubjects
        val query =
            s
                .innerJoin(c)
                .innerJoin(sp)
                .leftJoin(st)
                .select(s.entityColumns + c.columns + sp.columns + st.columns)
                .where {
                    (sp.id eq studyPlanId) and
                        sp.isActive() and
                        ss.existsForSubjectAndHourlyLoad(s, hourlyLoadId) and
                        c.matchesSearch(search)
                }.orderByCourse(c, s)
        return query.toSubjectPage(offset, limit)
    }

    override fun getAllByHourlyLoadIdAndStudyPlanIdAndCycle(
        hourlyLoadId: Long,
        studyPlanId: Long,
        cycle: Int,
    ): List<Subject> {
        val s = Subjects
        val c = Courses
        val sp = StudyPlans
        val ss = ScheduleSubjects
        val st = SubjectTypes
        return s
            .innerJoin(c)
            .innerJoin(sp)
            .leftJoin(st)
            .select(s.entityColumns + c.columns + sp.columns + st.columns)
            .where {
                (sp.id eq studyPlanId) and
                    sp.isActive() and
                    (s.cycle eq cycle) and
                    ss.existsForSubjectAndHourlyLoad(s, hourlyLoadId)
            }.orderByCourse(c, s)
            .map { row -> s.createEntity(row) }
    }

    private fun Courses.matchesSearch(search: String) = (name.unaccent() ilike ("%$search%").unaccent()) or (id ilike ("%$search%"))

    private val Subjects.entityColumns: List<Expression<*>>
        get() = listOf(id, courseId, typeId, studyPlanId, credits, cycle)

    private fun StudyPlans.isActive() = (fromDate less Instant.now()) and toDate.isNull()

    private fun ScheduleSubjects.existsForSubjectAndHourlyLoad(
        subjects: Subjects,
        requestedHourlyLoadId: Long,
    ) = exists(
        select(id)
            .where {
                (subjectId eq subjects.id) and
                    (hourlyLoadId eq requestedHourlyLoadId)
            },
    )

    private fun Query.orderByStudyPlanAndCourse(
        studyPlans: StudyPlans,
        courses: Courses,
        subjects: Subjects,
    ) = orderBy(
        studyPlans.fromDate to SortOrder.DESC,
        courses.id to SortOrder.ASC,
        subjects.id to SortOrder.ASC,
    )

    private fun Query.orderByCourse(
        courses: Courses,
        subjects: Subjects,
    ) = orderBy(
        courses.id to SortOrder.ASC,
        subjects.id to SortOrder.ASC,
    )

    private fun Query.toSubjectPage(
        offset: Int,
        limit: Int,
    ): Page<Subject> {
        val total = count().toInt()
        val content =
            limit(limit)
                .offset(offset.toLong())
                .map(Subjects::createEntity)

        return Page(offset, limit, total, content = content)
    }
}

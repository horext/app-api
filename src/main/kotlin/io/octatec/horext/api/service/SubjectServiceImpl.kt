package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.util.ilike
import io.octatec.horext.api.util.unaccent
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.or
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional
class SubjectServiceImpl() : SubjectService {

    override fun getAllByStudyPlanId(studyPlanId: Long): List<Subject> {
        val s = Subjects
        val c = Courses
        val st = SubjectTypes
        val sr = SubjectRelationships
        val subjects = s
            .innerJoin(c)
            .innerJoin(st)
            .select(s.columns + c.columns  + st.columns)
            .where {
                (s.studyPlanId eq studyPlanId)
            }
            .map { row -> s.createEntity(row) }
        val relationships = sr
            .select(sr.columns)
            .where { sr.subjectId inList subjects.map { it.id } }
            .map { row -> sr.createEntity(row) }

        subjects.forEach { subject ->
            subject.relationships = relationships.filter { it.subjectId == subject.id }
        }
        return subjects
    }

    override fun getAllBySpecialityId(specialityId: Long, hourlyLoadId: Long): List<Subject> {
        val s = Subjects
        val c = Courses
        val sp = StudyPlans
        val ss = ScheduleSubjects
        val st = SubjectTypes
        return s
            .innerJoin(c)
            .innerJoin(sp)
            .select(s.columns + c.columns + sp.columns + st.columns)
            .where {
                (sp.organizationUnitId eq specialityId) and
                        (sp.fromDate less Instant.now()) and
                        (sp.toDate.isNull()) and
                        exists(ss.select(ss.columns)
                            .where { (ss.subjectId eq s.id) and (ss.hourlyLoadId eq hourlyLoadId) }
                        )
            }
            .map { row -> s.createEntity(row) }
    }

    override fun getAllBySearchAndSpecialityIdAndHourlyLoad(
        search: String,
        specialityId: Long,
        hourlyLoadId: Long
    ): List<Subject> {
        val s = Subjects
        val c = Courses
        val sp = StudyPlans
        val ss = ScheduleSubjects
        val st = SubjectTypes
        return s
            .innerJoin(c)
            .innerJoin(sp)
            .select(s.columns + c.columns + sp.columns + st.columns)
            .where {
                (sp.organizationUnitId eq specialityId) and
                        (sp.fromDate less Instant.now()) and
                        (sp.toDate.isNull()) and
                        exists(ss.select(ss.columns)
                            .where {
                                (ss.subjectId eq s.id) and
                                        (ss.hourlyLoadId eq hourlyLoadId)
                            }
                        ) and
                        (c.name.unaccent() ilike ("%$search%").unaccent())
            }
            .map { row -> s.createEntity(row) }

    }

    override fun getAllBySearchAndSpecialityIdAndHourlyLoad(
        search: String,
        specialityId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int
    ): Page<Subject> {
        val s = Subjects
        val c = Courses
        val sp = StudyPlans
        val st = SubjectTypes
        val ss = ScheduleSubjects
        val query = s
            .innerJoin(c)
            .innerJoin(sp)
            .leftJoin(st)
            .select(s.columns + c.columns + sp.columns + st.columns)
            .where {
                (sp.organizationUnitId eq specialityId) and
                        (sp.fromDate less Instant.now()) and
                        (sp.toDate.isNull()) and
                        exists(ss.select(ss.id)
                            .where {
                                (ss.subjectId eq s.id) and
                                        (ss.hourlyLoadId eq hourlyLoadId)
                            }
                        ) and
                        searchCourse(c, search)
            }
        val queryResultCount = query.count()
        val queryResult = query.limit(n = limit, offset = offset.toLong())

        val list = queryResult.map { row -> s.createEntity(row) }
        return Page(offset, limit, queryResultCount.toInt(), content = list.toList())

    }


    private fun searchCourse(
        c: Courses,
        search: String
    ) = (c.name.unaccent() ilike ("%$search%").unaccent()) or (c.id ilike ("%$search%"))
}


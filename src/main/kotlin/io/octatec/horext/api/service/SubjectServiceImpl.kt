package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.util.concat
import io.octatec.horext.api.util.lower
import io.octatec.horext.api.util.unaccent
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class SubjectServiceImpl : SubjectService {
    @Autowired
    lateinit var database: Database

    override fun getAllBySpecialityId(specialityId: Long, hourlyLoadId: Long): List<Subject> {
        val s = Subjects
        val c = s.courseId.referenceTable as Courses
        val sp = s.studyPlanId.referenceTable as StudyPlans
        val ss = ScheduleSubjects
        return database
            .from(s)
            .innerJoin(c, on = s.courseId eq c.id)
            .innerJoin(sp, on = sp.id eq s.studyPlanId)
            .select()
            .where {
                (sp.organizationUnitId eq specialityId) and
                        (sp.fromDate less Instant.now()) and
                        (sp.toDate.isNull()) and
                        exists(database.from(ss).select()
                            .where { (ss.subjectId eq s.id) and (ss.hourlyLoadId eq hourlyLoadId) }
                        )
            }
            .map { row -> s.createEntity(row) }
    }

    override fun getAllBySearchAndSpecialityIdAndHourlyLoad(search:String,specialityId: Long, hourlyLoadId: Long): List<Subject> {
        val s = Subjects
        val c = s.courseId.referenceTable as Courses
        val sp = s.studyPlanId.referenceTable as StudyPlans
        val ss = ScheduleSubjects
        return database
            .from(s)
            .innerJoin(c, on = s.courseId eq c.id)
            .innerJoin(sp, on = sp.id eq s.studyPlanId)
            .select()
            .where {
                (sp.organizationUnitId eq specialityId) and
                        (sp.fromDate less Instant.now()) and
                        (sp.toDate.isNull()) and
                        exists(database.from(ss).select()
                            .where { (ss.subjectId eq s.id) and
                                    (ss.hourlyLoadId eq hourlyLoadId) }
                        ) and
                        (c.name.unaccent().lower() like ("%$search%").lowercase().unaccent())
            }
            .map { row -> s.createEntity(row) }

    }

    override fun getAllBySearchAndSpecialityIdAndHourlyLoad(search:String,specialityId: Long, hourlyLoadId: Long, offset: Int, limit: Int): Page<Subject> {
        val s = Subjects
        val c = s.courseId.referenceTable as Courses
        val sp = s.studyPlanId.referenceTable as StudyPlans
        val st = s.typeId.referenceTable as SubjectTypes
        val ss = ScheduleSubjects
        val queryResult = database
            .from(s)
            .innerJoin(c, on = s.courseId eq c.id)
            .innerJoin(sp, on = sp.id eq s.studyPlanId)
            .innerJoin(st, on = st.id eq s.typeId)
            .select()
            .where {
                (sp.organizationUnitId eq specialityId) and
                        (sp.fromDate less Instant.now()) and
                        (sp.toDate.isNull()) and
                        exists(database.from(ss).select(ss.id)
                            .where { (ss.subjectId eq s.id) and
                                    (ss.hourlyLoadId eq hourlyLoadId) }
                        ) and
                        (concat(" ",c.name,c.id).unaccent().lower() like ("%$search%").lowercase().unaccent())
            }.limit(offset,limit)

        val list = queryResult.map { row -> s.createEntity(row) }
        return Page(offset, limit, queryResult.totalRecords, content = list.toList())

    }
}
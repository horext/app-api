package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.util.concat
import io.octatec.horext.api.util.lower
import io.octatec.horext.api.util.unaccent
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant


@Service
class ScheduleSubjectServiceImpl : ScheduleSubjectService {
    @Autowired
    lateinit var database: Database

    override fun findBySubjectIdAndHourlyLoadId(subjectId: Long, hourlyLoadId: Long): List<ScheduleSubject> {

        val ss = ScheduleSubjects
        val s = ss.scheduleId.referenceTable as Schedules
        return database
            .from(ss)
            .leftJoin(s, on = s.id eq ss.scheduleId)
            .select(ss.columns+s.columns)
            .where{(ss.subjectId eq subjectId) and
                    (ss.hourlyLoadId eq hourlyLoadId) }
            .map { row -> ss.createEntity(row) }

    }

    override fun getAllByIds(ids: List<Long>): List<ScheduleSubject> {
        val ss = ScheduleSubjects
        val s = ss.subjectId.referenceTable as Subjects
        val c = s.courseId.referenceTable as Courses
        val skt = ss.scheduleId.referenceTable as Schedules
        return database
            .from(ss)
            .innerJoin(s, on = ss.subjectId eq s.id)
            .innerJoin(c, on = s.courseId eq c.id)
            .innerJoin(skt, on = ss.scheduleId eq skt.id)
            .select()
            .where(ss.id.inList(ids))
            .map { row -> ss.createEntity(row) }
    }

    override fun getAllBySearchAndSpecialityIdAndHourlyLoad(
        search: String,
        specialityId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int
    ): Page<ScheduleSubject> {

        val ss = ScheduleSubjects
        val s = ss.subjectId.referenceTable as Subjects
        val c = s.courseId.referenceTable as Courses
        val sp = s.studyPlanId.referenceTable as StudyPlans
        val skd = ss.scheduleId.referenceTable as Schedules
        val queryResult = database
            .from(ss)
            .innerJoin(s, on = ss.subjectId eq s.id)
            .innerJoin(c, on = s.courseId eq c.id)
            .innerJoin(sp, on = sp.id eq s.studyPlanId)
            .innerJoin(skd, on = skd.id eq ss.scheduleId)
            .select()
            .where {
                (sp.organizationUnitId eq specialityId) and
                        (sp.fromDate less Instant.now()) and
                        (sp.toDate.isNull()) and
                        exists(database.from(ss).select()
                            .where { (ss.subjectId eq s.id) and
                                    (ss.hourlyLoadId eq hourlyLoadId) }
                        ) and
                        (concat(" ",c.name,c.id).unaccent().lower() like ("%$search%").lowercase().unaccent())
            }.limit(limit).offset(offset)

        val list = queryResult.map { row -> ss.createEntity(row) }
        return Page(offset, limit, queryResult.totalRecords, content = list.toList())
    }
}
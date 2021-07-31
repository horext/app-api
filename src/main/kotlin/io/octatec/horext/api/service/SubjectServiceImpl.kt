package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
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
}
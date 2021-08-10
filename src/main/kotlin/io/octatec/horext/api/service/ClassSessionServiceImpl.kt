package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassSessionServiceImpl : ClassSessionService {
    @Autowired
    lateinit var database: Database

    override fun findByScheduleId(scheduleId: Long): List<ClassSession> {
        val cs = ClassSessions
        val cst = cs.classSessionTypeId.referenceTable as ClassSessionTypes
        val cr = cs.classroomId.referenceTable as Classrooms
        val t = cs.teacherId.referenceTable as Teachers
        return database
            .from(cs)
            .innerJoin(cst, on = cst.id eq cs.classSessionTypeId)
            .innerJoin(cr, on = cr.id eq cs.classroomId)
            .innerJoin(t, on = t.id eq cs.teacherId)
            .select()
            .where(cs.scheduleId eq scheduleId)
            .map { row -> cs.createEntity(row) }

    }

    override fun findByScheduleIds(scheduleIds: List<Long>): List<ClassSession> {

        val cs = ClassSessions
        val cst = cs.classSessionTypeId.referenceTable as ClassSessionTypes
        val cr = cs.classroomId.referenceTable as Classrooms
        val t = cs.teacherId.referenceTable as Teachers
        return database
            .from(cs)
            .innerJoin(cst, on = cst.id eq cs.classSessionTypeId)
            .innerJoin(cr, on = cr.id eq cs.classroomId)
            .innerJoin(t, on = t.id eq cs.teacherId)
            .select()
            .where(cs.scheduleId.inList(scheduleIds))
            .map { row -> cs.createEntity(row) }
    }
}
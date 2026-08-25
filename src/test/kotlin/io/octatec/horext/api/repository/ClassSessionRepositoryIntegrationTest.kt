package io.octatec.horext.api.repository

import io.octatec.horext.api.repository.table.ClassSessionTypes
import io.octatec.horext.api.repository.table.ClassSessions
import io.octatec.horext.api.repository.table.Classrooms
import io.octatec.horext.api.repository.table.Schedules
import io.octatec.horext.api.repository.table.Teachers
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.insert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalTime
import kotlin.test.assertEquals

@SpringBootTest
@Transactional
class ClassSessionRepositoryIntegrationTest(
    @Autowired private val repository: ClassSessionRepository,
) {
    @Test
    fun `findByScheduleIds excludes deleted and unrelated sessions and orders active sessions`() {
        val graph = createScheduleGraph()
        val typeId = graph.idBase + 20
        val classroomId = graph.idBase + 21
        val teacherId = graph.idBase + 22

        ClassSessionTypes.insert {
            it[id] = EntityID(typeId, ClassSessionTypes)
            it[code] = "TEST"
            it[name] = "Test session"
        }
        Classrooms.insert {
            it[id] = EntityID(classroomId, Classrooms)
            it[code] = "ROOM-${graph.idBase}"
            it[name] = "Test room"
        }
        Teachers.insert {
            it[id] = EntityID(teacherId, Teachers)
            it[code] = null
            it[fullName] = "Test Teacher"
        }

        insertSession(graph.idBase + 23, graph.linkedScheduleId, 3, LocalTime.of(10, 0), null, typeId, classroomId, teacherId)
        insertSession(graph.idBase + 24, graph.linkedScheduleId, 1, LocalTime.of(8, 0), null, typeId, classroomId, teacherId)
        insertSession(graph.idBase + 25, graph.linkedScheduleId, 2, LocalTime.of(9, 0), Instant.now(), typeId, classroomId, teacherId)
        insertSession(graph.idBase + 26, graph.unrelatedScheduleId, 0, LocalTime.of(7, 0), null, typeId, classroomId, teacherId)

        val result = repository.findByScheduleIds(listOf(graph.linkedScheduleId))

        assertEquals(listOf(graph.idBase + 24, graph.idBase + 23), result.map { it.id })
    }

    private fun insertSession(
        id: Long,
        scheduleId: Long,
        day: Int,
        startTime: LocalTime,
        deletedAt: Instant?,
        typeId: Long,
        classroomId: Long,
        teacherId: Long,
    ) {
        ClassSessions.insert {
            it[ClassSessions.id] = EntityID(id, ClassSessions)
            it[ClassSessions.scheduleId] = EntityID(scheduleId, Schedules)
            it[ClassSessions.day] = day
            it[ClassSessions.startTime] = startTime
            it[ClassSessions.endTime] = startTime.plusHours(1)
            it[ClassSessions.deletedAt] = deletedAt
            it[ClassSessions.classSessionTypeId] = EntityID(typeId, ClassSessionTypes)
            it[ClassSessions.classroomId] = EntityID(classroomId, Classrooms)
            it[ClassSessions.teacherId] = EntityID(teacherId, Teachers)
        }
    }
}

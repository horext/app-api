package io.octatec.horext.api.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@SpringBootTest
@Transactional
class ScheduleSubjectRepositoryIntegrationTest(
    @Autowired private val repository: ScheduleSubjectRepository,
) {
    @Test
    fun `getAllByIds joins the schedule by schedule subject instead of course`() {
        val graph = createScheduleGraph()

        val result = repository.getAllByIds(listOf(graph.scheduleSubjectId))

        assertEquals(1, result.size)
        assertEquals(graph.scheduleSubjectId, result.single().id)
        assertEquals(graph.linkedScheduleId, result.single().schedule.id)
    }
}

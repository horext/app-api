package io.octatec.horext.api.repository

import io.octatec.horext.api.repository.table.OrganizationUnits
import io.octatec.horext.api.repository.table.StudyPlans
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.insert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@SpringBootTest
@Transactional
class StudyPlanRepositoryIntegrationTest(
    @Autowired private val repository: StudyPlanRepository,
) {
    @Test
    fun `getStudyPlanById hydrates the explicitly joined organization unit`() {
        val graph = createScheduleGraph()

        val result = repository.getStudyPlanById(graph.studyPlanId)

        assertNotNull(result)
        assertEquals(graph.studyPlanId, result.id)
        assertEquals(graph.organizationId, result.organizationUnit?.id)
        assertEquals("OU-${graph.courseId.removePrefix("JOIN-")}", result.organizationUnit?.code)
    }

    @Test
    fun `getAllSpecialityId excludes plans belonging to another organization and orders newest first`() {
        val graph = createScheduleGraph()
        val unrelatedGraph = createScheduleGraph()
        val now = Instant.now()
        val olderPlanId = graph.idBase + 40
        val newerPlanId = graph.idBase + 41

        insertStudyPlan(olderPlanId, graph.organizationId, now.minusSeconds(120), now)
        insertStudyPlan(newerPlanId, graph.organizationId, now.minusSeconds(60), now)

        val result = repository.getAllSpecialityId(graph.organizationId)

        assertEquals(
            listOf(newerPlanId, olderPlanId, graph.studyPlanId),
            result.map { it.id },
        )
        assertFalse(result.any { it.id == unrelatedGraph.studyPlanId })
        result.forEach { assertEquals(graph.organizationId, it.organizationUnit?.id) }
    }

    private fun insertStudyPlan(
        id: Long,
        organizationId: Long,
        fromDate: Instant,
        now: Instant,
    ) {
        StudyPlans.insert {
            it[StudyPlans.id] = EntityID(id, StudyPlans)
            it[code] = "SP-$id"
            it[name] = "Test plan $id"
            it[StudyPlans.fromDate] = fromDate
            it[organizationUnitId] = EntityID(organizationId, OrganizationUnits)
            it[createdAt] = now
            it[updatedAt] = now
        }
    }
}

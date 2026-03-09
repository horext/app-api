package io.octatec.horext.api.service

import io.octatec.horext.api.domain.StudyPlan
import io.octatec.horext.api.repository.StudyPlanRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class StudyPlanServiceTest {

    @Autowired
    private lateinit var studyPlanService: StudyPlanService

    @MockBean
    private lateinit var studyPlanRepository: StudyPlanRepository

    @Test
    fun testGetAllStudyPlan() {
        val studyPlans = listOf(StudyPlan(1), StudyPlan(2))
        Mockito.`when`(studyPlanRepository.getAllStudyPlan()).thenReturn(studyPlans)

        val result = studyPlanService.getAllStudyPlan()

        assertEquals(studyPlans, result)
    }

    @Test
    fun testGetStudyPlanById() {
        val studyPlanId = 1L
        val studyPlans = listOf(StudyPlan(studyPlanId))
        Mockito.`when`(studyPlanRepository.getStudyPlanById(studyPlanId)).thenReturn(studyPlans)

        val result = studyPlanService.getStudyPlanById(studyPlanId)

        assertEquals(studyPlans, result)
    }
}

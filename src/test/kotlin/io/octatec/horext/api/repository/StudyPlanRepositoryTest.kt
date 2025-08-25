package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.StudyPlan
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class StudyPlanRepositoryTest {

    @Autowired
    private lateinit var studyPlanRepository: StudyPlanRepository

    @MockBean
    private lateinit var mockStudyPlanRepository: StudyPlanRepository

    @Test
    fun testGetAllStudyPlan() {
        val studyPlans = listOf(StudyPlan(1), StudyPlan(2))
        Mockito.`when`(mockStudyPlanRepository.getAllStudyPlan()).thenReturn(studyPlans)

        val result = studyPlanRepository.getAllStudyPlan()

        assertEquals(studyPlans, result)
    }

    @Test
    fun testGetStudyPlanById() {
        val studyPlanId = 1L
        val studyPlans = listOf(StudyPlan(studyPlanId))
        Mockito.`when`(mockStudyPlanRepository.getStudyPlanById(studyPlanId)).thenReturn(studyPlans)

        val result = studyPlanRepository.getStudyPlanById(studyPlanId)

        assertEquals(studyPlans, result)
    }
}

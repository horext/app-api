package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.StudyPlan
import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.service.StudyPlanService
import io.octatec.horext.api.service.SubjectService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(StudyPlanController::class)
class StudyPlanControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var studyPlanService: StudyPlanService

    @MockBean
    private lateinit var subjectService: SubjectService

    @Test
    fun testGetAllStudyPlans() {
        val studyPlans = listOf(StudyPlan(), StudyPlan())
        Mockito.`when`(studyPlanService.getAllStudyPlan()).thenReturn(studyPlans)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/studyPlans"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(studyPlans, result.response.contentAsString)
    }

    @Test
    fun testGetAllSpecialitiesByFacultyId() {
        val studyPlanId = 1L
        val studyPlans = listOf(StudyPlan(), StudyPlan())
        Mockito.`when`(studyPlanService.getStudyPlanById(studyPlanId)).thenReturn(studyPlans)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/studyPlans/$studyPlanId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(studyPlans, result.response.contentAsString)
    }

    @Test
    fun testGetAllSubjectsByStudyPlanId() {
        val studyPlanId = 1L
        val subjects = listOf(Subject(), Subject())
        Mockito.`when`(subjectService.getAllByStudyPlanId(studyPlanId)).thenReturn(subjects)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/studyPlans/$studyPlanId/subjects"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(subjects, result.response.contentAsString)
    }
}

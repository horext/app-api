package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.service.ScheduleSubjectService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(ScheduleSubjectController::class)
class ScheduleSubjectControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var scheduleSubjectService: ScheduleSubjectService

    @Test
    fun testGetAllByIds() {
        val ids = listOf(1L, 2L, 3L)
        val scheduleSubjects = listOf(ScheduleSubject(), ScheduleSubject())
        Mockito.`when`(scheduleSubjectService.getAllByIds(ids)).thenReturn(scheduleSubjects)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/scheduleSubjects?ids=${ids.joinToString(",")}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(scheduleSubjects, result.response.contentAsString)
    }

    @Test
    fun testGetAllBySpeciality() {
        val subjectId = 1L
        val hourlyLoadId = 1L
        val scheduleSubjects = listOf(ScheduleSubject(), ScheduleSubject())
        Mockito.`when`(scheduleSubjectService.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)).thenReturn(scheduleSubjects)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/scheduleSubjects?subject=$subjectId&hourlyLoad=$hourlyLoadId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(scheduleSubjects, result.response.contentAsString)
    }
}

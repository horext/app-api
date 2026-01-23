package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.service.ScheduleService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(ScheduleController::class)
class ScheduleControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var scheduleService: ScheduleService

    @Test
    fun testGetAllBySpeciality() {
        val subjectId = 1L
        val hourlyLoadId = 1L
        val schedules = listOf(Schedule(), Schedule())
        Mockito.`when`(scheduleService.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)).thenReturn(schedules)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/schedules?subject=$subjectId&hourlyLoad=$hourlyLoadId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(schedules, result.response.contentAsString)
    }
}

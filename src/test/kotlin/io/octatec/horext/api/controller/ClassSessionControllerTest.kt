package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.ClassSession
import io.octatec.horext.api.service.ClassSessionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(ClassSessionController::class)
class ClassSessionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var classSessionService: ClassSessionService

    @Test
    fun testGetAllBySpeciality() {
        val scheduleId = 1L
        val classSessions = listOf(ClassSession(), ClassSession())
        Mockito.`when`(classSessionService.findByScheduleId(scheduleId)).thenReturn(classSessions)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/classSessions?schedule=$scheduleId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(classSessions, result.response.contentAsString)
    }

    @Test
    fun testGetAllBySchedulesId() {
        val scheduleIds = listOf(1L, 2L)
        val classSessions = listOf(ClassSession(), ClassSession())
        Mockito.`when`(classSessionService.findByScheduleIds(scheduleIds)).thenReturn(classSessions)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/classSessions?schedules=${scheduleIds.joinToString(",")}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(classSessions, result.response.contentAsString)
    }
}

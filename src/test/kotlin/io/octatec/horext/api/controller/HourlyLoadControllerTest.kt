package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.HourlyLoad
import io.octatec.horext.api.service.HourlyLoadService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(HourlyLoadController::class)
class HourlyLoadControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var hourlyLoadService: HourlyLoadService

    @Test
    fun testGetLatestBySpeciality() {
        val facultyId = 1L
        val hourlyLoad = HourlyLoad()
        Mockito.`when`(hourlyLoadService.getLatestByFaculty(facultyId)).thenReturn(hourlyLoad)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/hourlyLoads/latest?faculty=$facultyId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(hourlyLoad, result.response.contentAsString)
    }
}

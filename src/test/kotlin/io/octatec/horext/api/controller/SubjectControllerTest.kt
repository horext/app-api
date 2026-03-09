package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
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

@WebMvcTest(SubjectController::class)
class SubjectControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var subjectService: SubjectService

    @Test
    fun testGetAllBySpeciality() {
        val specialityId = 1L
        val hourlyLoadId = 1L
        val subjects = listOf(Subject(), Subject())
        Mockito.`when`(subjectService.getAllBySpecialityId(specialityId, hourlyLoadId)).thenReturn(subjects)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/subjects?speciality=$specialityId&hourlyLoad=$hourlyLoadId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(subjects, result.response.contentAsString)
    }

    @Test
    fun testGetAllBySearch() {
        val search = "test"
        val specialityId = 1L
        val hourlyLoadId = 1L
        val offset = 0
        val limit = 10
        val subjects = listOf(Subject(), Subject())
        val page = Page(subjects, offset, limit, subjects.size)
        Mockito.`when`(subjectService.getAllBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId, offset, limit)).thenReturn(page)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/subjects?search=$search&speciality=$specialityId&hourlyLoad=$hourlyLoadId&offset=$offset&limit=$limit"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(page, result.response.contentAsString)
    }
}

package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.service.OrganizationUnitService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(SpecialityController::class)
class SpecialityControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var organizationUnitService: OrganizationUnitService

    @Test
    fun testGetAllByFacultyId() {
        val facultyId = 1L
        val organizationUnits = listOf(OrganizationUnit(), OrganizationUnit())
        Mockito.`when`(organizationUnitService.getAllSpecialityByFacultyId(facultyId)).thenReturn(organizationUnits)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/specialities?faculty=$facultyId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertEquals(organizationUnits, result.response.contentAsString)
    }
}

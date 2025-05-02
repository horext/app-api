package io.octatec.horext.api.service

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.repository.OrganizationUnitRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class OrganizationUnitServiceTest {

    @Autowired
    private lateinit var organizationUnitService: OrganizationUnitService

    @MockBean
    private lateinit var organizationUnitRepository: OrganizationUnitRepository

    @Test
    fun testGetAllSpeciality() {
        val organizationUnits = listOf(OrganizationUnit(), OrganizationUnit())
        Mockito.`when`(organizationUnitRepository.getAllSpeciality()).thenReturn(organizationUnits)

        val result = organizationUnitService.getAllSpeciality()

        assertEquals(organizationUnits, result)
    }

    @Test
    fun testGetAllFaculty() {
        val organizationUnits = listOf(OrganizationUnit(), OrganizationUnit())
        Mockito.`when`(organizationUnitRepository.getAllFaculty()).thenReturn(organizationUnits)

        val result = organizationUnitService.getAllFaculty()

        assertEquals(organizationUnits, result)
    }

    @Test
    fun testGetAllSpecialityByFacultyId() {
        val facultyId = 1L
        val organizationUnits = listOf(OrganizationUnit(), OrganizationUnit())
        Mockito.`when`(organizationUnitRepository.getAllSpecialityByFacultyId(facultyId)).thenReturn(organizationUnits)

        val result = organizationUnitService.getAllSpecialityByFacultyId(facultyId)

        assertEquals(organizationUnits, result)
    }
}

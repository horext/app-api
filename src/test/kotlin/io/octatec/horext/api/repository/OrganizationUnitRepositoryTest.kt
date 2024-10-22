package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.OrganizationUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class OrganizationUnitRepositoryTest {

    @Autowired
    private lateinit var organizationUnitRepository: OrganizationUnitRepository

    @MockBean
    private lateinit var organizationUnitRepositoryMock: OrganizationUnitRepository

    @Test
    fun testGetAllSpeciality() {
        val organizationUnits = listOf(OrganizationUnit(), OrganizationUnit())
        Mockito.`when`(organizationUnitRepositoryMock.getAllSpeciality()).thenReturn(organizationUnits)

        val result = organizationUnitRepository.getAllSpeciality()

        assertEquals(organizationUnits, result)
    }

    @Test
    fun testGetAllFaculty() {
        val organizationUnits = listOf(OrganizationUnit(), OrganizationUnit())
        Mockito.`when`(organizationUnitRepositoryMock.getAllFaculty()).thenReturn(organizationUnits)

        val result = organizationUnitRepository.getAllFaculty()

        assertEquals(organizationUnits, result)
    }

    @Test
    fun testGetAllSpecialityByFacultyId() {
        val facultyId = 1L
        val organizationUnits = listOf(OrganizationUnit(), OrganizationUnit())
        Mockito.`when`(organizationUnitRepositoryMock.getAllSpecialityByFacultyId(facultyId)).thenReturn(organizationUnits)

        val result = organizationUnitRepository.getAllSpecialityByFacultyId(facultyId)

        assertEquals(organizationUnits, result)
    }
}

package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.exception.ResourceNotFoundException
import io.octatec.horext.api.service.OrganizationUnitService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class FacultyControllerTest {
    @Mock
    private lateinit var organizationUnitService: OrganizationUnitService

    private lateinit var facultyController: FacultyController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        facultyController = FacultyController(organizationUnitService)
    }

    @Test
    fun testGetAll() {
        val expectedFacultyList =
            listOf(
                OrganizationUnit(id = 1, name = "Faculty 1"),
                OrganizationUnit(id = 2, name = "Faculty 2"),
            )
        `when`(organizationUnitService.getAllFaculty()).thenReturn(expectedFacultyList)

        val actualFacultyList = facultyController.getAll()

        assertEquals(expectedFacultyList, actualFacultyList)
    }

    @Test
    fun testGetAllSpecialitiesByFacultyId() {
        val facultyId = 1L
        val expectedSpecialityList =
            listOf(
                OrganizationUnit(id = 1, name = "Speciality 1"),
                OrganizationUnit(id = 2, name = "Speciality 2"),
            )
        `when`(organizationUnitService.getAllSpecialityByFacultyId(facultyId)).thenReturn(expectedSpecialityList)

        val actualSpecialityList = facultyController.getAllSpecialitiesByFacultyId(facultyId)

        assertEquals(expectedSpecialityList, actualSpecialityList)
    }

    @Test
    fun testGetAllSpecialitiesByFacultyId_ResourceNotFoundException() {
        val facultyId = 1L
        `when`(organizationUnitService.getAllSpecialityByFacultyId(facultyId)).thenThrow(ResourceNotFoundException::class.java)

        assertThrows(ResourceNotFoundException::class.java) {
            facultyController.getAllSpecialitiesByFacultyId(facultyId)
        }
    }
}

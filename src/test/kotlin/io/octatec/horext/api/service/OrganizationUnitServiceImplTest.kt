package io.octatec.horext.api.service

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.repository.OrganizationUnitRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class OrganizationUnitServiceImplTest {
    @Mock
    private lateinit var organizationUnitRepository: OrganizationUnitRepository

    private lateinit var service: OrganizationUnitServiceImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        service = OrganizationUnitServiceImpl(organizationUnitRepository)
    }

    @Test
    fun getAllFaculty_returnsRepositoryResult() {
        val expected = listOf(OrganizationUnit(id = 1, name = "Faculty"))
        `when`(organizationUnitRepository.getFacultiesHavingStudyPlans()).thenReturn(expected)

        val result = service.getAllFaculty()

        assertEquals(expected, result)
        verify(organizationUnitRepository).getFacultiesHavingStudyPlans()
    }

    @Test
    fun getAllFaculty_propagatesRepositoryException() {
        `when`(organizationUnitRepository.getFacultiesHavingStudyPlans()).thenThrow(RuntimeException("db"))

        assertThrows(RuntimeException::class.java) {
            service.getAllFaculty()
        }
        verify(organizationUnitRepository).getFacultiesHavingStudyPlans()
    }
}

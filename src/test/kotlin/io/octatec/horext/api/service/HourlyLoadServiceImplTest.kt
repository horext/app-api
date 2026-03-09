package io.octatec.horext.api.service

import io.octatec.horext.api.domain.HourlyLoad
import io.octatec.horext.api.repository.HourlyLoadRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class HourlyLoadServiceImplTest {
    @Mock
    private lateinit var hourlyLoadRepository: HourlyLoadRepository

    private lateinit var service: HourlyLoadServiceImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        service = HourlyLoadServiceImpl(hourlyLoadRepository)
    }

    @Test
    fun getLatestByFaculty_returnsRepositoryResult() {
        val facultyId = 1L
        val expected = HourlyLoad(id = 10L)
        `when`(hourlyLoadRepository.getLatestByFaculty(facultyId)).thenReturn(expected)

        val result = service.getLatestByFaculty(facultyId)

        assertEquals(expected, result)
        verify(hourlyLoadRepository).getLatestByFaculty(facultyId)
    }

    @Test
    fun getLatestByFaculty_propagatesRepositoryException() {
        val facultyId = 1L
        `when`(hourlyLoadRepository.getLatestByFaculty(facultyId)).thenThrow(RuntimeException("db"))

        assertThrows(RuntimeException::class.java) {
            service.getLatestByFaculty(facultyId)
        }
        verify(hourlyLoadRepository).getLatestByFaculty(facultyId)
    }
}

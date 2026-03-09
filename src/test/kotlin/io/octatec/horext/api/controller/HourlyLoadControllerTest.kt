package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.HourlyLoad
import io.octatec.horext.api.exception.ResourceNotFoundException
import io.octatec.horext.api.service.HourlyLoadService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class HourlyLoadControllerTest {
    @Mock
    private lateinit var hourlyLoadService: HourlyLoadService

    private lateinit var hourlyLoadController: HourlyLoadController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        hourlyLoadController = HourlyLoadController(hourlyLoadService)
    }

    @Test
    fun getLatestBySpeciality_returnsServiceResult() {
        val facultyId = 1L
        val expected = HourlyLoad(id = 10L)
        `when`(hourlyLoadService.getLatestByFaculty(facultyId)).thenReturn(expected)

        val actual = hourlyLoadController.getLatestBySpeciality(facultyId)

        assertEquals(expected, actual)
        verify(hourlyLoadService).getLatestByFaculty(facultyId)
    }

    @Test
    fun getLatestBySpeciality_propagatesResourceNotFoundException() {
        val facultyId = 1L
        `when`(hourlyLoadService.getLatestByFaculty(facultyId)).thenThrow(ResourceNotFoundException::class.java)

        assertThrows(ResourceNotFoundException::class.java) {
            hourlyLoadController.getLatestBySpeciality(facultyId)
        }
        verify(hourlyLoadService).getLatestByFaculty(facultyId)
    }
}

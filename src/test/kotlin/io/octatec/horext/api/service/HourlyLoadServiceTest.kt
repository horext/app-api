package io.octatec.horext.api.service

import io.octatec.horext.api.domain.HourlyLoad
import io.octatec.horext.api.repository.HourlyLoadRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class HourlyLoadServiceTest {

    @Autowired
    private lateinit var hourlyLoadService: HourlyLoadService

    @MockBean
    private lateinit var hourlyLoadRepository: HourlyLoadRepository

    @Test
    fun testGetLatestByFaculty() {
        val facultyId = 1L
        val hourlyLoad = HourlyLoad()
        Mockito.`when`(hourlyLoadRepository.getLatestByFaculty(facultyId)).thenReturn(hourlyLoad)

        val result = hourlyLoadService.getLatestByFaculty(facultyId)

        assertEquals(hourlyLoad, result)
    }
}

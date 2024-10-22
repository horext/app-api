package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.HourlyLoad
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class HourlyLoadRepositoryTest {

    @Autowired
    private lateinit var hourlyLoadRepository: HourlyLoadRepository

    @MockBean
    private lateinit var hourlyLoadRepositoryMock: HourlyLoadRepository

    @Test
    fun testGetLatestByFaculty() {
        val facultyId = 1L
        val hourlyLoad = HourlyLoad(1L)
        Mockito.`when`(hourlyLoadRepositoryMock.getLatestByFaculty(facultyId)).thenReturn(hourlyLoad)

        val result = hourlyLoadRepository.getLatestByFaculty(facultyId)

        assertEquals(hourlyLoad, result)
    }
}

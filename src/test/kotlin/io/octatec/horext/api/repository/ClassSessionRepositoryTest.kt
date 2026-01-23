package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.ClassSession
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class ClassSessionRepositoryTest {

    @Autowired
    private lateinit var classSessionRepository: ClassSessionRepository

    @MockBean
    private lateinit var classSessionRepositoryMock: ClassSessionRepository

    @Test
    fun testFindByScheduleId() {
        val scheduleId = 1L
        val classSessions = listOf(ClassSession(), ClassSession())
        Mockito.`when`(classSessionRepositoryMock.findByScheduleId(scheduleId)).thenReturn(classSessions)

        val result = classSessionRepository.findByScheduleId(scheduleId)

        assertEquals(classSessions, result)
    }

    @Test
    fun testFindByScheduleIds() {
        val scheduleIds = listOf(1L, 2L)
        val classSessions = listOf(ClassSession(), ClassSession())
        Mockito.`when`(classSessionRepositoryMock.findByScheduleIds(scheduleIds)).thenReturn(classSessions)

        val result = classSessionRepository.findByScheduleIds(scheduleIds)

        assertEquals(classSessions, result)
    }
}

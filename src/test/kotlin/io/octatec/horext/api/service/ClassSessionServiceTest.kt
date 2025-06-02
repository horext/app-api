package io.octatec.horext.api.service

import io.octatec.horext.api.domain.ClassSession
import io.octatec.horext.api.repository.ClassSessionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class ClassSessionServiceTest {

    @Autowired
    private lateinit var classSessionService: ClassSessionService

    @MockBean
    private lateinit var classSessionRepository: ClassSessionRepository

    @Test
    fun testFindByScheduleId() {
        val scheduleId = 1L
        val classSessions = listOf(ClassSession(), ClassSession())
        Mockito.`when`(classSessionRepository.findByScheduleId(scheduleId)).thenReturn(classSessions)

        val result = classSessionService.findByScheduleId(scheduleId)

        assertEquals(classSessions, result)
    }

    @Test
    fun testFindByScheduleIds() {
        val scheduleIds = listOf(1L, 2L)
        val classSessions = listOf(ClassSession(), ClassSession())
        Mockito.`when`(classSessionRepository.findByScheduleIds(scheduleIds)).thenReturn(classSessions)

        val result = classSessionService.findByScheduleIds(scheduleIds)

        assertEquals(classSessions, result)
    }
}

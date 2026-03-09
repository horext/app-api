package io.octatec.horext.api.service

import io.octatec.horext.api.domain.StudyPlan
import io.octatec.horext.api.repository.StudyPlanRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class StudyPlanServiceImplTest {
    @Mock
    private lateinit var studyPlanRepository: StudyPlanRepository

    private lateinit var service: StudyPlanServiceImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        service = StudyPlanServiceImpl(studyPlanRepository)
    }

    @Test
    fun getStudyPlanById_returnsRepositoryResult() {
        val studyPlanId = 1L
        val expected = listOf(StudyPlan(id = studyPlanId))
        `when`(studyPlanRepository.getStudyPlanById(studyPlanId)).thenReturn(expected)

        val result = service.getStudyPlanById(studyPlanId)

        assertEquals(expected, result)
        verify(studyPlanRepository).getStudyPlanById(studyPlanId)
    }

    @Test
    fun getStudyPlanById_propagatesRepositoryException() {
        val studyPlanId = 1L
        `when`(studyPlanRepository.getStudyPlanById(studyPlanId)).thenThrow(RuntimeException("db"))

        assertThrows(RuntimeException::class.java) {
            service.getStudyPlanById(studyPlanId)
        }
        verify(studyPlanRepository).getStudyPlanById(studyPlanId)
    }
}

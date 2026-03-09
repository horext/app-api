package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.repository.SubjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SubjectServiceImplTest {
    @Mock
    private lateinit var subjectRepository: SubjectRepository

    private lateinit var service: SubjectServiceImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        service = SubjectServiceImpl(subjectRepository)
    }

    @Test
    fun getAllByStudyPlanId_returnsRepositoryResult() {
        val studyPlanId = 1L
        val expected = listOf(Subject(id = 100L))
        `when`(subjectRepository.getAllByStudyPlanId(studyPlanId)).thenReturn(expected)

        val result = service.getAllByStudyPlanId(studyPlanId)

        assertEquals(expected, result)
        verify(subjectRepository).getAllByStudyPlanId(studyPlanId)
    }

    @Test
    fun getAllByStudyPlanId_propagatesRepositoryException() {
        val studyPlanId = 1L
        `when`(subjectRepository.getAllByStudyPlanId(studyPlanId)).thenThrow(RuntimeException("db"))

        assertThrows(RuntimeException::class.java) {
            service.getAllByStudyPlanId(studyPlanId)
        }
        verify(subjectRepository).getAllByStudyPlanId(studyPlanId)
    }
}

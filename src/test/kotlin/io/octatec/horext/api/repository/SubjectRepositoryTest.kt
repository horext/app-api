package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class SubjectRepositoryTest {

    @Autowired
    private lateinit var subjectRepository: SubjectRepository

    @MockBean
    private lateinit var subjectRepositoryMock: SubjectRepository

    @Test
    fun testGetAllByStudyPlanId() {
        val studyPlanId = 1L
        val subjects = listOf(Subject(), Subject())
        Mockito.`when`(subjectRepositoryMock.getAllByStudyPlanId(studyPlanId)).thenReturn(subjects)

        val result = subjectRepository.getAllByStudyPlanId(studyPlanId)

        assertEquals(subjects, result)
    }

    @Test
    fun testGetAllBySpecialityId() {
        val specialityId = 1L
        val hourlyLoadId = 1L
        val subjects = listOf(Subject(), Subject())
        Mockito.`when`(subjectRepositoryMock.getAllBySpecialityId(specialityId, hourlyLoadId)).thenReturn(subjects)

        val result = subjectRepository.getAllBySpecialityId(specialityId, hourlyLoadId)

        assertEquals(subjects, result)
    }

    @Test
    fun testGetAllBySearchAndSpecialityIdAndHourlyLoad() {
        val search = "test"
        val specialityId = 1L
        val hourlyLoadId = 1L
        val subjects = listOf(Subject(), Subject())
        Mockito.`when`(subjectRepositoryMock.getAllBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId)).thenReturn(subjects)

        val result = subjectRepository.getAllBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId)

        assertEquals(subjects, result)
    }

    @Test
    fun testGetAllBySearchAndSpecialityIdAndHourlyLoadWithPagination() {
        val search = "test"
        val specialityId = 1L
        val hourlyLoadId = 1L
        val offset = 0
        val limit = 10
        val subjects = listOf(Subject(), Subject())
        val page = Page(subjects, offset, limit, subjects.size)
        Mockito.`when`(subjectRepositoryMock.getAllBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId, offset, limit)).thenReturn(page)

        val result = subjectRepository.getAllBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId, offset, limit)

        assertEquals(page, result)
    }
}

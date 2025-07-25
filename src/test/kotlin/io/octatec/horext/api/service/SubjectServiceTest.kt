package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.repository.SubjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class SubjectServiceTest {

    @Autowired
    private lateinit var subjectService: SubjectService

    @MockBean
    private lateinit var subjectRepository: SubjectRepository

    @Test
    fun testGetAllByStudyPlanId() {
        val studyPlanId = 1L
        val subjects = listOf(Subject(), Subject())
        Mockito.`when`(subjectRepository.getAllByStudyPlanId(studyPlanId)).thenReturn(subjects)

        val result = subjectService.getAllByStudyPlanId(studyPlanId)

        assertEquals(subjects, result)
    }

    @Test
    fun testGetAllBySpecialityId() {
        val specialityId = 1L
        val hourlyLoadId = 1L
        val subjects = listOf(Subject(), Subject())
        Mockito.`when`(subjectRepository.getAllBySpecialityId(specialityId, hourlyLoadId)).thenReturn(subjects)

        val result = subjectService.getAllBySpecialityId(specialityId, hourlyLoadId)

        assertEquals(subjects, result)
    }

    @Test
    fun testGetAllBySearchAndSpecialityIdAndHourlyLoad() {
        val search = "test"
        val specialityId = 1L
        val hourlyLoadId = 1L
        val subjects = listOf(Subject(), Subject())
        Mockito.`when`(subjectRepository.getAllBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId)).thenReturn(subjects)

        val result = subjectService.getAllBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId)

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
        Mockito.`when`(subjectRepository.getAllBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId, offset, limit)).thenReturn(page)

        val result = subjectService.getAllBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId, offset, limit)

        assertEquals(page, result)
    }
}

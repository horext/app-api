package io.octatec.horext.api.service

import io.octatec.horext.api.dto.PageDTO
import io.octatec.horext.api.model.Subject
import io.octatec.horext.api.repository.SubjectRepository
import io.octatec.horext.api.util.Pagination
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class SubjectServiceImpl(val subjectRepository: SubjectRepository) : SubjectService {
    override fun getAllBySpecialityId(specialityId: Long, hourlyLoadId: Long): List<Subject> {
        return subjectRepository.findAllBySpecialityIdAndHourlyLoadId(specialityId,hourlyLoadId)
    }

    override
    fun getAll(page: Int, size: Int): PageDTO<Subject> {
        Pagination.validatePageNumberAndSize(page, size)
        val pageable: Pageable = PageRequest.of(page, size)
        val elementsPage: Page<Subject> = subjectRepository.findAll(pageable)
        val body: MutableList<Subject> = elementsPage.stream().map {
                s -> Subject(s.id,s.cycle,s.credits,s.course)
        }.collect(Collectors.toList())

        return PageDTO(
            body,
            elementsPage.number, elementsPage.size,
            elementsPage.totalElements,
            elementsPage.totalPages, elementsPage.isLast
        )
    }

    override fun getAllBySearch(page: Int, size: Int, search: String): PageDTO<Subject> {
        Pagination.validatePageNumberAndSize(page, size)
        val pageable: Pageable = PageRequest.of(page, size)
        val subjects: Page<Subject> = subjectRepository.getAllBySearch(search, pageable)
        val body: MutableList<Subject> = subjects.stream().map {
                s -> Subject(s.id,s.cycle,s.credits,s.course)
        }.collect(Collectors.toList())

        return PageDTO(
            body,
            subjects.number, subjects.size,
            subjects.totalElements,
            subjects.totalPages, subjects.isLast
        )
    }
}
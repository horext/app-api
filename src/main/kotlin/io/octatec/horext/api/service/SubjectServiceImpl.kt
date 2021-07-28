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

    override
    fun getAll(pageable: Pageable, specialityId: Long, hourlyLoadId: Long): PageDTO<Subject> {
        val elementsPage: Page<Subject> = subjectRepository.findByOrganizationIdAndHourlyLoadId(specialityId,hourlyLoadId,pageable)
        val body: MutableList<Subject> = elementsPage.stream().map { s ->
            println(s.id)
            s?.course?.let { Subject(s.id, s.cycle, s.credits, it) }
        }.collect(Collectors.toList())

        return PageDTO(
            body,
            elementsPage.number, elementsPage.size,
            elementsPage.totalElements,
            elementsPage.totalPages, elementsPage.isLast
        )
    }

    override fun getAllBySearch(pageable: Pageable, search: String, specialityId: Long, hourlyLoadId: Long): PageDTO<Subject> {
        val subjects: Page<Subject> = subjectRepository.getAllBySearch(search, specialityId,hourlyLoadId,pageable)
        val body: List<Subject> = subjects.stream().map {
                s ->
            s.course?.let { Subject(s.id,s.cycle,s.credits, it) }
        }.collect(Collectors.toList())

        return PageDTO(
            body,
            subjects.number, subjects.size,
            subjects.totalElements,
            subjects.totalPages, subjects.isLast
        )
    }
}
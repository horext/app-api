package io.octatec.horext.api.service

import io.octatec.horext.api.model.Subject
import io.octatec.horext.api.repository.SubjectRepository
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap

@Service
class SubjectServiceImpl(val subjectRepository: SubjectRepository) : SubjectService {
    override fun getAllBySpecialityId(specialityId: Long, hourlyLoadId: Long): List<Subject> {
        return subjectRepository.findAllBySpecialityIdAndHourlyLoadId(specialityId,hourlyLoadId)
    }
}
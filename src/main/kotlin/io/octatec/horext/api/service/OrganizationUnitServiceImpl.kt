package io.octatec.horext.api.service

import io.octatec.horext.api.domain.OrganizationUnits
import io.octatec.horext.api.domain.organizationUnitTypes
import io.octatec.horext.api.domain.organizationUnits
import io.octatec.horext.api.model.OrganizationUnit
import io.octatec.horext.api.model.OrganizationUnitType
import io.octatec.horext.api.repository.OrganizationUnitRepository
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.filter
import org.ktorm.entity.find
import org.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrganizationUnitServiceImpl(val organizationUnitRepository: OrganizationUnitRepository) : OrganizationUnitService {

    @Autowired
    lateinit var database: Database

    override fun getAllSpeciality(): List<OrganizationUnit> {
       return organizationUnitRepository.findAllByOrganizationUnitType(OrganizationUnitType(3)).
       map { o -> OrganizationUnit(o.id,o.code,o.name) }
    }
    override fun getAllFaculty(): List<io.octatec.horext.api.domain.OrganizationUnit> {
        return database.organizationUnits.filter { it.typeId eq 2 }.toList()
    }

    override fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit> {
        return organizationUnitRepository.findAllByParentOrganization(OrganizationUnit(id)).
        map { o -> OrganizationUnit(o.id,o.code,o.name) }
    }
}
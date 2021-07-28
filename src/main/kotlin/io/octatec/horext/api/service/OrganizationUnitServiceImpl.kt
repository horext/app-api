package io.octatec.horext.api.service

import io.octatec.horext.api.domain.OrganizationUnit
import io.octatec.horext.api.domain.organizationUnits
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrganizationUnitServiceImpl : OrganizationUnitService {

    @Autowired
    lateinit var database: Database

    override fun getAllSpeciality(): List<OrganizationUnit> {
       return database.organizationUnits.filter { it.typeId eq 3 }.toList()
    }
    override fun getAllFaculty(): List<OrganizationUnit> {
        return database.organizationUnits.filter { it.typeId eq 2 }.toList()
    }

    override fun getAllSpecialityByFacultyId(id: Long): List<OrganizationUnit> {
        return database.organizationUnits.filter { it.parentOrganizationId eq id}.toList()
    }
}
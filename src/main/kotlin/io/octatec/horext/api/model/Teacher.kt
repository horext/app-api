package io.octatec.horext.api.model

import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Teacher : Person() {
    var code: String? = null

    @OneToMany(mappedBy = "organizationUnit")
    private val organizationUnitTeachers: List<OrganizationUnitTeacher>? = null
}

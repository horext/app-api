package io.octatec.horext.api.model

import javax.persistence.Entity
import javax.persistence.Id


@Entity
class SubjectType {
    @Id
    private val id: Long? = null
    private val code: String? = null
    private val name: String? = null
}
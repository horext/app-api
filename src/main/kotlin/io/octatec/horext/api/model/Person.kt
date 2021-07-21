package io.octatec.horext.api.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Inheritance
import javax.persistence.InheritanceType


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
open class Person {
    @Id
    open var id: Long? = null
    open var firstName: String? = null
    open var lastName: String? = null
}

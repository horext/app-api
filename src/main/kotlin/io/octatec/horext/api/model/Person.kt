package io.octatec.horext.api.model

import javax.persistence.*


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
open class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null
    open var firstName: String? = null
    open var lastName: String? = null
}

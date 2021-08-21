package io.octatec.horext.api.domain

import org.ktorm.entity.Entity
import org.ktorm.schema.*

interface Section : Entity<Section> {
    companion object : Entity.Factory<Section>()

    val id: String

}

object Sections : Table<Section>("section") {

    val id = varchar("id").primaryKey().bindTo { it.id }

}

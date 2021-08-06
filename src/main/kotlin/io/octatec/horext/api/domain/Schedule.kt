package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.Instant

interface Schedule : Entity<Schedule> {
    companion object : Entity.Factory<Schedule>()

    val id: Long

    var section: Section
}

object Schedules : Table<Schedule>("schedule") {

    val id = long("id").primaryKey().bindTo { it.id }

    val sectionId = long("section_id").references(Sections) { it.section }

}

package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.schema.*
import java.time.Instant

interface Schedule : Entity<Schedule> {
    companion object : Entity.Factory<Schedule>()

    val id: Long

    var section: Section

    fun schedulesSubjects(db: Database) = db.scheduleSubjects.filter { it.scheduleId eq id }.toList()

    fun classSessions(db: Database) = db.classSessions.filter { it.scheduleId eq id }.toList()
}

open class Schedules(alias: String?)  : Table<Schedule>("schedule", alias) {
    companion object : Schedules(null)
    override fun aliased(alias: String) = Schedules(alias)

    val id = long("id").primaryKey().bindTo { it.id }

    val sectionId = varchar("section_id").references(Sections) { it.section }
}


val Database.schedules get() = this.sequenceOf(Schedules)
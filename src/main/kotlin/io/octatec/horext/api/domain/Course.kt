package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

/**
 * The organization unit entity.
 */
interface Course : Entity<Course> {
    companion object : Entity.Factory<Course>()

    val id: String

    var name: String
}

open class Courses(alias: String?)  : Table<Course>("course", alias) {
    companion object : Courses(null)
    override fun aliased(alias: String) = Courses(alias)

    val id = varchar("id").primaryKey().bindTo { it.id }

    val name = varchar("name").bindTo { it.name }
}

val Database.courses get() = this.sequenceOf(Courses)
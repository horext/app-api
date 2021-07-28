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

    val id: Long

    var code: String

    var name: String
}

object Courses : Table<Course>("course") {

    val id = long("id").primaryKey().bindTo { it.id }

    val code = varchar("code").bindTo { it.code }

    val name = varchar("name").bindTo { it.name }
}

val Database.courses get() = this.sequenceOf(Courses)
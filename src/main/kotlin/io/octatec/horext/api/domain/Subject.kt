package io.octatec.horext.api.domain

import io.octatec.horext.api.domain.OrganizationUnits.references
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface Subject : Entity<Subject> {
    companion object : Entity.Factory<Subject>()

    val id: Long

    var code: String

    var name: String

    var course: Course
}

object Subjects : Table<Subject>("subject") {

    val id = long("id").primaryKey().bindTo { it.id }

    val code = varchar("code").bindTo { it.code }

    val name = varchar("name").bindTo { it.name }

    val course = int("course_id").references(Courses) { it.course }
}

val Database.subjects get() = this.sequenceOf(Subjects)
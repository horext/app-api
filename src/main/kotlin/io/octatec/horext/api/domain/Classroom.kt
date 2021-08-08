package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface Classroom : Entity<Classroom> {
    companion object : Entity.Factory<Classroom>()

    val id: Long

    var code: String

    var name: String
}

open class Classrooms(alias: String?)  : Table<Classroom>("classroom", alias) {
    companion object : Classrooms(null)

    override fun aliased(alias: String) = Classrooms(alias)

    val id = long("id").primaryKey().bindTo { it.id }

    val name = varchar("name").bindTo { it.name }

    val code = varchar("code").bindTo { it.code }
}


val Database.classrooms get() = this.sequenceOf(Classrooms)
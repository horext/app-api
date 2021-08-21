package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface Teacher : Entity<Teacher> {
    companion object : Entity.Factory<Teacher>()

    val id: Long

    var code: String

    var fullName: String

}

open class Teachers(alias: String?)  : Table<Teacher>("teacher", alias) {
    companion object : Teachers(null)

    override fun aliased(alias: String) = Teachers(alias)

    val id = long("id").primaryKey().bindTo { it.id }

    val fullName = varchar("full_name").bindTo { it.fullName }

    val code = varchar("code").bindTo { it.code }
}


val Database.teachers get() = this.sequenceOf(Teachers)
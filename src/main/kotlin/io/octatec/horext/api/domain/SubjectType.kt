package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface SubjectType : Entity<SubjectType> {
    companion object : Entity.Factory<SubjectType>()

    val id: Long

    var code: String

    var name: String
}

open class SubjectTypes(alias: String?)  : Table<SubjectType>("subject_type", alias) {
    companion object : SubjectTypes(null)

    override fun aliased(alias: String) = SubjectTypes(alias)

    val id = long("id").primaryKey().bindTo { it.id }

    val name = varchar("name").bindTo { it.name }

    val code = varchar("code").bindTo { it.code }
}


val Database.SubjectsTypes get() = this.sequenceOf(SubjectTypes)
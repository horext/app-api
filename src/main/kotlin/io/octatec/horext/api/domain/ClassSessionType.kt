package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

interface ClassSessionType : Entity<ClassSessionType> {
    companion object : Entity.Factory<ClassSessionType>()

    val id: Long

    var code: String

    var name: String
}

open class ClassSessionTypes(alias: String?)  : Table<ClassSessionType>("class_session_type", alias) {
    companion object : ClassSessionTypes(null)

    override fun aliased(alias: String) = ClassSessionTypes(alias)

    val id = long("id").primaryKey().bindTo { it.id }

    val name = varchar("name").bindTo { it.name }

    val code = varchar("code").bindTo { it.code }
}


val Database.classSessionsTypes get() = this.sequenceOf(ClassSessionTypes)
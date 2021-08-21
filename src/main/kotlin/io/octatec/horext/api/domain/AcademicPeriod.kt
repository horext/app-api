package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.LocalDate

interface AcademicPeriod : Entity<AcademicPeriod> {
    companion object : Entity.Factory<AcademicPeriod>()

    val id: Long

    var code: String

    var name: String

}

object AcademicPeriods : Table<AcademicPeriod>("academic_period") {

    val id = long("id").primaryKey().bindTo { it.id }

    val code = varchar("code").bindTo { it.code }

    val name = varchar("name").bindTo { it.name }
}

val Database.academicPeriods get() = this.sequenceOf(AcademicPeriods)
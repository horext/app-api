package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.Instant

interface HourlyLoad : Entity<HourlyLoad> {
    companion object : Entity.Factory<HourlyLoad>()

    val id: Long

    var name: String

    var checkedAt: Instant

    var publishedAt: Instant

    var academicPeriodOrganizationUnit: AcademicPeriodOrganizationUnit

}

object HourlyLoads : Table<HourlyLoad>("hourly_load") {

    val id = long("id").primaryKey().bindTo { it.id }


    val name = varchar("name").bindTo { it.name }

    val checkedAt = timestamp("checked_at").bindTo { it.checkedAt }

    val publishedAt = timestamp("published_at").bindTo { it.publishedAt }

    val academicPeriodOrganizationUnitId = long("academic_period_organization_unit_id")
        .references(AcademicPeriodOrganizationUnits) { it.academicPeriodOrganizationUnit }

}

val Database.hourlyLoads get() = this.sequenceOf(HourlyLoads)
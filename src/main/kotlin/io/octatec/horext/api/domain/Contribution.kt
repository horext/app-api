package io.octatec.horext.api.domain

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.timestamp

object Contributions : LongIdTable("contribution") {
    val authorName = varchar("author_name", 255)
    val authorEmail = varchar("author_email", 255).nullable()
    val committedAt = timestamp("committed_at").defaultExpression(org.jetbrains.exposed.v1.core.CurrentDateTime)
}

object HourlyLoadContributions : Table("hourly_load_contribution") {
    val hourlyLoadId = reference("hourly_load_id", HourlyLoads)
    val contributionId = reference("contribution_id", Contributions)
    override val primaryKey = PrimaryKey(hourlyLoadId, contributionId)
}

object StudyPlanContributions : Table("study_plan_contribution") {
    val studyPlanId = reference("study_plan_id", StudyPlans)
    val contributionId = reference("contribution_id", Contributions)
    override val primaryKey = PrimaryKey(studyPlanId, contributionId)
}

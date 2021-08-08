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

    var course: Course

    var studyPlan: StudyPlan

    var credits: Int

    var cycle: Int
}

object Subjects : Table<Subject>("subject") {

    val id = long("id").primaryKey().bindTo { it.id }

    val courseId = varchar("course_id").references(Courses) { it.course }

    val studyPlanId = long("study_plan_id").references(StudyPlans) { it.studyPlan }

    val credits = int("credits").bindTo { it.credits }

    val cycle = int("cycle").bindTo { it.cycle }
}

val Database.subjects get() = this.sequenceOf(Subjects)
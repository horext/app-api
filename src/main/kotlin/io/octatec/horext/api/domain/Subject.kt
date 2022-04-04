package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*

interface Subject : Entity<Subject> {
    companion object : Entity.Factory<Subject>()

    val id: Long

    var course: Course

    var studyPlan: StudyPlan

    var type: SubjectType

    var credits: Int

    var cycle: Int
}

open class Subjects(alias: String?)  : Table<Subject>("subject", alias) {
    companion object : Subjects(null)
    override fun aliased(alias: String) = Subjects(alias)

    val id = long("id").primaryKey().bindTo { it.id }

    val courseId = varchar("course_id").references(Courses) { it.course }

    val typeId = long("subject_type_id").references(SubjectTypes) { it.type }

    val studyPlanId = long("study_plan_id").references(StudyPlans) { it.studyPlan }

    val credits = int("credits").bindTo { it.credits }

    val cycle = int("cycle").bindTo { it.cycle }
}

val Database.subjects get() = this.sequenceOf(Subjects)
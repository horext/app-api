package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

data class Subject(

    val id: Long,

    var course: Course?,

    var studyPlan: StudyPlan?,

    var type: SubjectType?,

    var credits: Int?,

    var cycle: Int?
) {

    constructor(id: Long) : this(id, null, null, null, null, null)
}

object Subjects : LongIdTable("subject") {

    val courseId = reference("course_id", Courses)

    val typeId = reference("subject_type_id", SubjectTypes)

    val studyPlanId = reference("study_plan_id", StudyPlans)

    val credits = integer("credits")

    val cycle = integer("cycle")

    fun createEntity(row: ResultRow): Subject {
        return Subject(
            id = row[id].value,
            course = runCatching { Courses.createEntity(row) }
                .getOrElse { Course(id = row[courseId].value) },
            studyPlan = runCatching { StudyPlans.createEntity(row) }
                .getOrElse { StudyPlan(id = row[studyPlanId].value) },
            type = runCatching { SubjectTypes.createEntity(row) }
                .getOrElse { SubjectType(id = row[typeId].value) },
            credits = row[credits],
            cycle = row[cycle]
        )
    }
}

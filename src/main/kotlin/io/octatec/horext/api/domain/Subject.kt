package io.octatec.horext.api.domain

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

data class Subject(
    val id: Long,
    var course: Course?,
    var studyPlan: StudyPlan?,
    var type: SubjectType?,
    var credits: Int?,
    var cycle: Int?,
    var relationships: List<SubjectRelationship> = ArrayList(),
) {
    constructor(id: Long) : this(id, null, null, null, null, null)
}

object Subjects : LongIdTable("subject") {
    val courseId = reference("course_id", Courses)

    val typeId = reference("subject_type_id", SubjectTypes).nullable()

    val studyPlanId = reference("study_plan_id", StudyPlans)

    val credits = integer("credits").nullable()

    val cycle = integer("cycle").nullable()

    val totalWeeklyHours = integer("total_weekly_hours").nullable()

    val weeklyTheoryHours = integer("weekly_theory_hours").nullable()

    val weeklyPracticeHours = integer("weekly_practice_hours").nullable()

    val weeklyPracticeLaboratoryHours = integer("weekly_practice_laboratory_hours").nullable()

    val weeklyLaboratoryHours = integer("weekly_laboratory_hours").nullable()

    val maxCycle = integer("max_cycle").nullable()

    val minCycle = integer("min_cycle").nullable()

    val note = varchar("note", 255).nullable()

    val requiredCredits = integer("required_credits").nullable()

    val evaluationSystemId = long("evaluation_system_id").nullable()

    fun createEntity(row: ResultRow): Subject =
        Subject(
            id = row[id].value,
            course =
                runCatching { Courses.createEntity(row) }
                    .getOrElse { Course(id = row[courseId].value) },
            studyPlan =
                runCatching { StudyPlans.createEntity(row) }
                    .getOrElse { StudyPlan(id = row[studyPlanId].value) },
            type =
                row.getOrNull(typeId)?.let { typeEntityId ->
                    runCatching { SubjectTypes.createEntity(row) }
                        .getOrElse { SubjectType(id = typeEntityId.value) }
                },
            credits = row[credits],
            cycle = row[cycle],
        )
}

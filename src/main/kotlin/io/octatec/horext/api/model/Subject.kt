package io.octatec.horext.api.model

import javax.persistence.*

@Entity
class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    var weeklyTheoryHours: Int? = null
    var weeklyPracticeHours: Int? = null
    var weeklyLaboratoryHours: Int? = null
    var weeklyPracticeLaboratoryHours: Int? = null
    var totalWeeklyHours: Int? = null
    var cycle: Int? = null
    var minCycle: Int? = null
    var maxCycle: Int? = null
    var credits: Int? = null
    var requiredCredits: Int? = null
    var note: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course: Course? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var subjectType: SubjectType? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var studyPlan: StudyPlan? = null

    @OneToMany(mappedBy = "fromSubject",fetch = FetchType.LAZY)
    var fromSubjectRelationships: List<SubjectRelationship> ? = null

    @OneToMany(mappedBy = "toSubject",fetch = FetchType.LAZY)
    var toSubjectRelationships: List<SubjectRelationship> ? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var evaluationSystem: EvaluationSystem ? = null

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    var scheduleSubjects: List<ScheduleSubject>? = null

    constructor(id: Long?, weeklyTheoryHours: Int?) {
        this.id = id
        this.weeklyTheoryHours = weeklyTheoryHours
    }

    constructor(id: Long?) {
        this.id = id
    }

    constructor(weeklyPracticeLaboratoryHours: Int?, totalWeeklyHours: Int?) {
        this.weeklyPracticeLaboratoryHours = weeklyPracticeLaboratoryHours
        this.totalWeeklyHours = totalWeeklyHours
    }

    constructor(id: Long?, cycle: Int?, credits: Int?, course: Course){
        this.id = id
        this.cycle = cycle
        this.credits = credits
        this.course = Course(course.id, course.name)
    }

    constructor(
        id: Long?,
        weeklyTheoryHours: Int?,
        weeklyPracticeHours: Int?,
        weeklyLaboratoryHours: Int?,
        weeklyPracticeLaboratoryHours: Int?,
        totalWeeklyHours: Int?,
        cycle: Int?,
        minCycle: Int?,
        maxCycle: Int?,
        credits: Int?,
        requiredCredits: Int?,
        note: String?,
        course: Course?,
        subjectType: SubjectType?,
        studyPlan: StudyPlan?,
        fromSubjectRelationships: List<SubjectRelationship>?,
        toSubjectRelationships: List<SubjectRelationship>?,
        evaluationSystem: EvaluationSystem?,
        scheduleSubjects: List<ScheduleSubject>?
    ) {
        this.id = id
        this.weeklyTheoryHours = weeklyTheoryHours
        this.weeklyPracticeHours = weeklyPracticeHours
        this.weeklyLaboratoryHours = weeklyLaboratoryHours
        this.weeklyPracticeLaboratoryHours = weeklyPracticeLaboratoryHours
        this.totalWeeklyHours = totalWeeklyHours
        this.cycle = cycle
        this.minCycle = minCycle
        this.maxCycle = maxCycle
        this.credits = credits
        this.requiredCredits = requiredCredits
        this.note = note
        this.course = course
        this.subjectType = subjectType
        this.studyPlan = studyPlan
        this.fromSubjectRelationships = fromSubjectRelationships
        this.toSubjectRelationships = toSubjectRelationships
        this.evaluationSystem = evaluationSystem
        this.scheduleSubjects = scheduleSubjects
    }


}
package io.octatec.horext.api.model

import javax.persistence.*

@Entity
class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    var course: Course? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var subjectType: SubjectType? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var studyPlan: StudyPlan? = null

    @OneToMany(mappedBy = "fromSubject",fetch = FetchType.LAZY)
    var fromSubjectRelationships: List<SubjectRelationship>? = null

    @OneToMany(mappedBy = "toSubject",fetch = FetchType.LAZY)
    var toSubjectRelationships: List<SubjectRelationship>? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var evaluationSystem: EvaluationSystem? = null

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


}
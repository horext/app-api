package io.octatec.horext.api.model

import org.hibernate.annotations.SQLDelete
import java.time.Instant
import javax.persistence.*

@Entity
class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var vacancies: Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var section: Section? = null

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    var classSessions: List<ClassSession>? = null

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    var scheduleSubjects: List<ScheduleSubject>? = null

     val deletedAt: Instant? = null
}

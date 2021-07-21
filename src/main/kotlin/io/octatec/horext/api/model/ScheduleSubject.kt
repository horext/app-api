package io.octatec.horext.api.model

import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["hourly_load_id", "schedule_id", "subject_id"])])
class ScheduleSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var hourlyLoad: HourlyLoad? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var schedule: Schedule? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var subject: Subject? = null
    var fromDatetime: Timestamp? = null
    var toDatetime: Timestamp? = null

    @ManyToMany(fetch = FetchType.LAZY)
    var accessLinks: List<AccessLink>? = null
}

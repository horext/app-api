package io.octatec.horext.api.model

import java.util.*
import javax.persistence.*


@Entity
class AccessLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var uuid: UUID? = null

    @JoinTable(name = "access_link_schedule_subject",
            joinColumns = [JoinColumn(name = "access_link_id")],
            inverseJoinColumns = [JoinColumn(name = "schedule_subject_id")]
    )
    @ManyToMany
    var scheduleSubject: List<ScheduleSubject> =   ArrayList()

    constructor() {}
    constructor(uuid: UUID?) {
        this.uuid = uuid
    }
}

package io.octatec.horext.api.model

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.sql.Time
import java.time.Instant
import javax.persistence.*


@Entity
class ClassSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne
    var schedule: Schedule? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var classSessionType: ClassSessionType? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var classroom: Classroom? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var teacher: Teacher? = null
    var startTime: Time? = null
    var endTime: Time? = null
    var day: Int? = null
    var deletedAt: Instant? = null
}

package io.octatec.horext.api.domain

import java.time.Instant

data class ScheduleSubject(
    val id: Long,
    var fromDate: Instant?,
    var toDate: Instant?,
    var subject: Subject,
    var hourlyLoad: HourlyLoad,
    var schedule: Schedule,
)

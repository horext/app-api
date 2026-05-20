package io.octatec.horext.api.domain

import java.time.LocalTime

data class ClassSession(
    val id: Long,
    var schedule: Schedule,
    var type: ClassSessionType,
    var classroom: Classroom,
    var teacher: Teacher,
    var day: Int,
    var startTime: LocalTime,
    var endTime: LocalTime,
)

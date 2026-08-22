package io.octatec.horext.api.domain

import java.time.Instant

data class Schedule(
    val id: Long,
    var section: Section?,
    var course: Course?,
    var deleteAt: Instant?,
    var sessions: List<ClassSession>? = ArrayList(),
) {
    constructor(id: Long) : this(id, null, null, null, null)
}

package io.octatec.horext.api.domain

import java.time.Instant

data class Schedule(
    val id: Long,
    var section: Section?,
    var deleteAt: Instant?,
    var sessions: List<ClassSession>? = null,
) {
    constructor(id: Long) : this(id, null, null, null)
}

package io.octatec.horext.api.domain

import java.time.Instant

/**
 * The organization unit entity.
 */
data class Course(
    val id: String,
    var name: String?,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
) {
    constructor(id: String) : this(id, null)
}

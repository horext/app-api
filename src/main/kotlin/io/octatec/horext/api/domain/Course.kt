package io.octatec.horext.api.domain

/**
 * The organization unit entity.
 */
data class Course(
    val id: String,
    var name: String?,
) {
    constructor(id: String) : this(id, null)
}

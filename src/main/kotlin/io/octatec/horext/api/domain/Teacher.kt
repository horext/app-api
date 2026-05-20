package io.octatec.horext.api.domain

data class Teacher(
    val id: Long,
    var code: String?,
    var fullName: String?,
) {
    constructor(id: Long) : this(id, null, null)
}

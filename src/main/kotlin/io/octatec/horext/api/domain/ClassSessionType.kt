package io.octatec.horext.api.domain

data class ClassSessionType(
    val id: Long,
    var code: String?,
    var name: String?,
) {
    constructor(id: Long) : this(id, null, null)
}

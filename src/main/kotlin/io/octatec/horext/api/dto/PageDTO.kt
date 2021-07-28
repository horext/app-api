package io.octatec.horext.api.dto

class PageDTO<C>(
    var content: List<C>?,
    var page: Int,
    var size: Int,
    var totalElements: Long,
    var totalPages: Int,
    last: Boolean
) {
    var isLast = last

}
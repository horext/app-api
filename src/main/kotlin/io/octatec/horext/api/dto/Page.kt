package io.octatec.horext.api.dto

data class Page<T>(
    val offset: Int,
    val limit: Int,
    val totalElements: Int,
    val pageSize: Int = limit,
    val totalPages: Int = if (totalElements % pageSize == 0) totalElements / pageSize else totalElements / pageSize + 1,
    val content: List<T>,
)

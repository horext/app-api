package io.octatec.horext.api.dto

import io.octatec.horext.api.config.AppConstants
import io.octatec.horext.api.exception.BadRequestException

data class SubjectSearchQuery(
    var search: String = "",
    var offset: Int = 0,
    var limit: Int = 10,
) {
    fun validate(): SubjectSearchQuery {
        if (search.isBlank()) {
            throw BadRequestException("search must not be blank")
        }
        if (offset < 0) {
            throw BadRequestException("offset must be greater than or equal to 0")
        }
        if (limit <= 0 || limit > AppConstants.MAX_PAGE_SIZE) {
            throw BadRequestException("limit must be between 1 and ${AppConstants.MAX_PAGE_SIZE}")
        }
        return this
    }
}

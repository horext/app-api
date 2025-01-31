package io.octatec.horext.api.util

import io.octatec.horext.api.config.AppConstants
import io.octatec.horext.api.exception.BadRequestException

object Pagination {
    fun validatePageNumberAndSize(
        page: Int,
        size: Int,
    ) {
        if (page < 0) {
            throw BadRequestException("Page number cannot be less than zero.")
        }
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE)
        }
    }
}

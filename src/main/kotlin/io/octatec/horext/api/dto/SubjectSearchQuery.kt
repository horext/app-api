package io.octatec.horext.api.dto

import io.octatec.horext.api.config.AppConstants
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

data class SubjectSearchQuery(
    @field:NotBlank var search: String = "",
    @field:PositiveOrZero var offset: Int = 0,
    @field:Max(AppConstants.MAX_PAGE_SIZE.toLong())
    @field:Positive var limit: Int = 10,
)

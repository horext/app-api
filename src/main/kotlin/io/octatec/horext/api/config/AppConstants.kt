package io.octatec.horext.api.config

interface AppConstants {
    companion object {
        const val DEFAULT_PAGE_NUMBER = "0"
        const val DEFAULT_PAGE_SIZE = "15"
        const val MAX_PAGE_SIZE = 30
        const val DEFAULT_PAGE_SORT = "createdAt"
    }
}
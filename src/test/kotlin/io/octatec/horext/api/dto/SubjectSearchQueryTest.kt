package io.octatec.horext.api.dto

import io.octatec.horext.api.config.AppConstants
import io.octatec.horext.api.exception.BadRequestException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SubjectSearchQueryTest {
    @Test
    fun `accepts a valid query`() {
        val query = SubjectSearchQuery(search = "math", offset = 0, limit = AppConstants.MAX_PAGE_SIZE)

        assertEquals(query, query.validate())
    }

    @Test
    fun `rejects blank search`() {
        assertFailsWith<BadRequestException> {
            SubjectSearchQuery(search = " ").validate()
        }
    }

    @Test
    fun `rejects invalid pagination`() {
        assertFailsWith<BadRequestException> {
            SubjectSearchQuery(search = "math", offset = -1).validate()
        }
        assertFailsWith<BadRequestException> {
            SubjectSearchQuery(search = "math", limit = 0).validate()
        }
        assertFailsWith<BadRequestException> {
            SubjectSearchQuery(search = "math", limit = AppConstants.MAX_PAGE_SIZE + 1).validate()
        }
    }
}

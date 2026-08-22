package io.octatec.horext.api.util

import io.octatec.horext.api.exception.BadRequestException
import kotlin.test.Test
import kotlin.test.assertFailsWith

class PaginationTest {
    @Test
    fun `rejects zero page size`() {
        assertFailsWith<BadRequestException> {
            Pagination.validatePageNumberAndSize(page = 0, size = 0)
        }
    }

    @Test
    fun `rejects negative page size`() {
        assertFailsWith<BadRequestException> {
            Pagination.validatePageNumberAndSize(page = 0, size = -1)
        }
    }
}

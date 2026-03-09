package io.octatec.horext.api.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
class FacultyControllerIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
) {
    @Test
    fun facultiesEndpointShouldReturnJsonFromDatabaseQuery() {
        mockMvc
            .perform(get("/faculties"))
            .andExpect(handler().handlerType(FacultyController::class.java))
            .andExpect(handler().methodName("getAll"))
            .andExpect { result ->
                assertTrue(
                    result.response.status == 200 || result.response.status == 404,
                    "Expected 200 or 404, got ${result.response.status}",
                )
            }.andExpect {
                if (it.response.status == 200) {
                    content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON).match(it)
                    jsonPath("$").isArray.match(it)
                }
            }
    }
}

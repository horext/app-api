package io.octatec.horext.api.service

import io.octatec.horext.api.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ThreadServiceImpl : ThreadService {
    @Value("\${app.thread-api-url}")
    val baseUrl = ""

    @Autowired
    private val restTemplate: RestTemplate? = null

    override fun getThread(
        document: String,
        courseCode: String,
    ): Any? {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request =
            HttpEntity(
                "{\"operationName\":null,\"variables\":{}," +
                    "\"query\":\"{\\n  publicThread(document: \\\"$document\\\", courseId: \\\"$courseCode\\\") {\\n    id\\n  }\\n}\\n\"}",
                headers,
            )
        val responseEntityStr =
            restTemplate!!.postForEntity(
                baseUrl,
                request,
                java.util.HashMap::class.java,
            )
        val entity = (responseEntityStr.body)!!
        return try {
            Objects.requireNonNull(entity)["data"]
        } catch (e: Exception) {
            throw ResourceNotFoundException("Thread no encontrado")
        }
    }
}

package io.octatec.horext.api.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

data class ApiErrorResponse(
    val timestamp: Instant,
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val trace: String,
)

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(
        ex: RuntimeException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val status = resolveStatus(ex)
        val body =
            ApiErrorResponse(
                timestamp = Instant.now(),
                status = status.value(),
                error = status.reasonPhrase,
                message = ex.message ?: status.reasonPhrase,
                path = request.requestURI,
                trace = ex.stackTraceToString(),
            )

        return ResponseEntity.status(status).body(body)
    }

    private fun resolveStatus(ex: Throwable): HttpStatus {
        val responseStatus = AnnotationUtils.findAnnotation(ex.javaClass, ResponseStatus::class.java)
        return responseStatus?.code ?: HttpStatus.INTERNAL_SERVER_ERROR
    }
}

package io.octatec.horext.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException : RuntimeException {
    var resourceName: String? = null
        private set
    var fieldName: String? = null
        private set
    var fieldValue: Any? = null
        private set

    constructor(
        resourceName: String?,
        fieldName: String?,
        fieldValue: Any?
    ) : super(String.format("%s no fue encontrado con  %s : '%s'", resourceName, fieldName, fieldValue)) {
        this.resourceName = resourceName
        this.fieldName = fieldName
        this.fieldValue = fieldValue
    }

    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}
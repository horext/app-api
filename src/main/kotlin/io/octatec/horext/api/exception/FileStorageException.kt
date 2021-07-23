package io.octatec.horext.api.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class FileStorageException : RuntimeException {
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}
package io.octatec.horext.api.service

import io.octatec.horext.api.service.ThreadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import java.util.HashMap
import io.octatec.horext.api.service.ThreadServiceImpl

interface ThreadService {
    fun getThread(document: String, courseCode: String): Any?
}
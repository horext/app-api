package io.octatec.horext.api.service

interface ThreadService {
    fun getThread(
        document: String,
        courseCode: String,
    ): Any?
}

package io.octatec.horext.api.repository

import io.octatec.horext.api.model.ClassSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ClassSessionRepository : JpaRepository<ClassSession?, Long?>

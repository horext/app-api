package io.octatec.horext.api.repository

import io.octatec.horext.api.model.AccessLink
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface AccessLinkRepository : JpaRepository<AccessLink, UUID>
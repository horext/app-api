package io.octatec.horext.api.repository

import io.octatec.horext.api.model.HourlyLoad
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface HourlyLoadRepository : JpaRepository<HourlyLoad, Long>{
    @Query(value = "select hl from HourlyLoad hl " +
            " inner join hl.academicPeriodOrganizationUnit as apo" +
            " where  apo.organizationUnit.id = ( ?1 ) " +
            " and hl.publishedAt<=current_timestamp" +
            " and apo.fromDate<=current_date and apo.toDate is null ")
    fun getLatestByOrganizationUnitId( organizationUnitId: Long): HourlyLoad
}
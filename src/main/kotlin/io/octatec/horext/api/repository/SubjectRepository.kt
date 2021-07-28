package io.octatec.horext.api.repository

import io.octatec.horext.api.model.Course
import io.octatec.horext.api.model.Subject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface SubjectRepository : JpaRepository<Subject, Long> {

    @Query(
        "SELECT s FROM Subject s inner join  s.course  c" +
                " WHERE fts( c.id||' '|| c.name, :name ) = true and " +
                "  s.studyPlan.organizationUnit.id = ( :organizationUnitId  ) " +
                " and s.studyPlan.fromDate < current_date " +
                " and  s.studyPlan.toDate is null " +
                " and exists (select ss from ScheduleSubject ss " +
                " where ss.subject.id = s.id and ss.hourlyLoad.id = ( :hourlyLoadId  ) )",
        countQuery = "SELECT count(s) FROM Subject s inner join  s.course c " +
                " WHERE fts( c.id||' '||c.name, :name ) = true and " +
                " s.studyPlan.organizationUnit.id = ( :organizationUnitId  ) " +
                " and s.studyPlan.fromDate < current_date " +
                " and  s.studyPlan.toDate is null " +
                " and exists (select ss from ScheduleSubject ss " +
                " where ss.subject.id = s.id and ss.hourlyLoad.id = ( :hourlyLoadId  ) )",
    )
    fun getAllBySearch(
        @Param("name") name: String,
        @Param("organizationUnitId") organizationUnitId: Long,
        @Param("hourlyLoadId") hourlyLoadId: Long, pageable: Pageable
    ): Page<Subject>

    @Query(
        "SELECT s FROM Subject s  JOIN FETCH s.course  c JOIN FETCH s.studyPlan sp" +
                " where sp.organizationUnit.id = ( :organizationUnitId  ) " +
                " and sp.fromDate < current_date " +
                " and  sp.toDate is null " +
                " and exists (select ss from ScheduleSubject ss " +
                " where ss.subject.id = s.id and ss.hourlyLoad.id = ( :hourlyLoadId  ) )",
        countQuery = "SELECT count(s) FROM Subject s join s.course c join  s.studyPlan sp" +
                " where sp.organizationUnit.id = ( :organizationUnitId  ) " +
                " and sp.fromDate < current_date " +
                " and  sp.toDate is null " +
                " and exists (select ss from ScheduleSubject ss " +
                " where ss.subject.id = s.id and ss.hourlyLoad.id = ( :hourlyLoadId  ) )"
    )
    fun findByOrganizationIdAndHourlyLoadId(
        @Param("organizationUnitId") organizationUnitId: Long,
        @Param("hourlyLoadId") hourlyLoadId: Long,
        pageable: Pageable
    ): Page<Subject>
}

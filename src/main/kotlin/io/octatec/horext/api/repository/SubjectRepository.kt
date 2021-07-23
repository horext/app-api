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
        value = "select  s from Subject s " +
                " join fetch s.course" +
                " where s.studyPlan.organizationUnit.id = ( ?1 ) " +
                " and s.studyPlan.fromDate < current_date " +
                " and  s.studyPlan.toDate is null " +
                " and exists (select ss from ScheduleSubject ss " +
                " where ss.subject.id = s.id and ss.hourlyLoad.id = ( ?2 ) )"
    )
    fun findAllBySpecialityIdAndHourlyLoadId(specialityId: Long?, hourlyLoadId: Long?): List<Subject>


    @Query(
        "SELECT s FROM Subject s join  s.course  p WHERE fts( p.id|| p.name, :name ) = true",
        countQuery = "SELECT count(s) FROM Subject s join  s.course p  WHERE fts( p.id|| p.name, :name ) = true"
    )
    fun getAllBySearch(@Param("name") name: String, pageable: Pageable): Page<Subject>
}

package io.octatec.horext.api.repository

import io.octatec.horext.api.model.Course
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface CourseRepository : JpaRepository<Course, Long> {


    @Query(
        "SELECT p FROM Course p WHERE fts( p.id|| p.name, :name ) = true",
        countQuery = "SELECT  count(p) FROM Course p WHERE fts( p.id|| p.name, :name ) = true"
    )
    fun getAllBySearch(@Param("name")  name: String , pageable: Pageable) : Page<Course>

}

package io.octatec.horext.api.repository

import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
@Transactional
class ScheduleSubjectRepositoryIntegrationTest(
    @Autowired private val repository: ScheduleSubjectRepository,
    @Autowired private val jdbcTemplate: JdbcTemplate,
) {
    @Test
    fun `getAllByIds joins the schedule by schedule subject instead of course`() {
        val suffix = UUID.randomUUID().toString().take(8)
        val idBase = 1_000_000_000L + (suffix.hashCode().toLong() and 0x0fffffff)
        val courseId = "JOIN-$suffix"
        val firstSectionId = "X${suffix[0]}"
        val secondSectionId = "Y${suffix[0]}"
        val subjectId = idBase + 1
        val hourlyLoadId = idBase + 2
        val linkedScheduleId = idBase + 3
        val unrelatedScheduleId = idBase + 4
        val scheduleSubjectId = idBase + 5

        jdbcTemplate.update("insert into course (id, name) values (?, ?)", courseId, "Join regression course")
        jdbcTemplate.update("insert into section (id, code) values (?, ?)", firstSectionId, firstSectionId)
        jdbcTemplate.update("insert into section (id, code) values (?, ?)", secondSectionId, secondSectionId)

        jdbcTemplate.update("insert into study_plan (id, name) values (?, ?)", idBase, "Join regression plan")
        jdbcTemplate.update(
            "insert into subject (id, course_id, study_plan_id) values (?, ?, ?)",
            subjectId,
            courseId,
            idBase,
        )
        jdbcTemplate.update("insert into hourly_load (id, name) values (?, ?)", hourlyLoadId, "Join regression load")
        jdbcTemplate.update(
            "insert into schedule (id, course_id, section_id) values (?, ?, ?)",
            linkedScheduleId,
            courseId,
            firstSectionId,
        )
        jdbcTemplate.update(
            "insert into schedule (id, course_id, section_id) values (?, ?, ?)",
            unrelatedScheduleId,
            courseId,
            secondSectionId,
        )
        jdbcTemplate.update(
            """
            insert into schedule_subject (id, schedule_id, subject_id, hourly_load_id)
            values (?, ?, ?, ?)
            """.trimIndent(),
            scheduleSubjectId,
            linkedScheduleId,
            subjectId,
            hourlyLoadId,
        )

        val result = repository.getAllByIds(listOf(scheduleSubjectId))

        assertEquals(1, result.size)
        assertEquals(scheduleSubjectId, result.single().id)
        assertEquals(linkedScheduleId, result.single().schedule.id)
    }
}

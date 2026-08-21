package db.migration.schema

import db.csv.CsvLimits
import db.csv.CsvSchema
import db.csv.csvSchema
import db.migration.R__200_GenerateHourlyLoad.ScheduleResume
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun hourlyLoadCsvSchema(
    defaultFacultyCode: String,
    defaultUpdatedAt: LocalDateTime,
): CsvSchema<ScheduleResume> {
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    return csvSchema("hourly-load") {
        limits(CsvLimits.MIGRATION_DEFAULTS)

        val faculty =
            optionalString("codigo_facultad") {
                trim()
                maxLength(50)
                rejectControlCharacters()
            }
        val course =
            requiredString("codigo_curso") {
                trim()
                maxLength(50)
                rejectControlCharacters()
                normalize(::normalizeCourseCode)
            }
        val section =
            requiredString("seccion") {
                trim()
                maxLength(50)
                rejectControlCharacters()
            }
        val vacancies =
            intColumn("vacantes") {
                default(0)
                range(0..10_000)
            }
        val updatedAt = dateTimeColumn("updated_at")
        val deletedAt = dateTimeColumn("deleted_at")
        val startTime = timeColumn("inicio") { formats("HH:mm", "H") }
        val endTime = timeColumn("fin") { formats("HH:mm", "H") }
        val classroom =
            optionalString("aula") {
                trim()
                uppercase()
                maxLength(50)
                rejectControlCharacters()
                default("NO_CLASSROOM")
            }
        val teacherDni =
            optionalString("dni_docente") {
                trim()
                maxLength(50)
                rejectControlCharacters()
            }
        val teacher =
            optionalString("nombre_docente", requiredHeader = true) {
                trim()
                maxLength(100)
                rejectControlCharacters()
                default("NN")
            }
        val sessionType =
            optionalString("tipo") {
                trim()
                uppercase()
                maxLength(50)
                rejectControlCharacters()
                default("UNSPECIFIED")
            }
        val day =
            optionalString("dia", requiredHeader = true) {
                trim()
                maxLength(20)
                rejectControlCharacters()
            }

        mapRow {
            ScheduleResume(
                facultyCode = faculty() ?: defaultFacultyCode,
                course = course(),
                section = section(),
                vacancies = vacancies(),
                updatedAt = updatedAt() ?: defaultUpdatedAt,
                deletedAt = deletedAt(),
                startTime = startTime()?.format(timeFormat).orEmpty(),
                endTime = endTime()?.format(timeFormat).orEmpty(),
                classroom = requireNotNull(classroom()),
                teacherDni = teacherDni(),
                teacherName = requireNotNull(teacher()),
                sessionType = requireNotNull(sessionType()),
                day = day().orEmpty(),
            )
        }

        validateFile {
            maxDistinct("teachers", 1_000) { it.teacherDni ?: it.teacherName }
            maxDistinct("classrooms", 500) { it.classroom }
            consistentMapping("teacher DNI to name", { it.teacherDni }, { it.teacherName })
        }
    }
}

internal fun normalizeCourseCode(value: String): String = value.replace("-", "")

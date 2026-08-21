package db.migration.schema

import db.csv.CsvSchema
import db.csv.csvSchema
import db.migration.R__100_UpdateAcademicPeriods.AcademicPeriodRow

fun academicPeriodCsvSchema(): CsvSchema<AcademicPeriodRow> =
    csvSchema("academic-period") {
        val code =
            requiredString("code") {
                trim()
                maxLength(50)
                rejectControlCharacters()
            }
        val fromDate = instantColumn("from_date")
        val toDate = instantColumn("to_date")
        val facultyCode =
            requiredString("faculty_code") {
                trim()
                maxLength(50)
                rejectControlCharacters()
            }

        mapRow {
            AcademicPeriodRow(
                code = code(),
                fromDate = fromDate(),
                toDate = toDate(),
                facultyCode = facultyCode(),
            )
        }
    }

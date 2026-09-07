package io.octatec.horext.api.repository

import io.octatec.horext.api.repository.table.AcademicPeriodOrganizationUnits
import io.octatec.horext.api.repository.table.AcademicPeriods
import io.octatec.horext.api.repository.table.Courses
import io.octatec.horext.api.repository.table.HourlyLoads
import io.octatec.horext.api.repository.table.OrganizationUnitTypes
import io.octatec.horext.api.repository.table.OrganizationUnits
import io.octatec.horext.api.repository.table.ScheduleSubjects
import io.octatec.horext.api.repository.table.Schedules
import io.octatec.horext.api.repository.table.Sections
import io.octatec.horext.api.repository.table.StudyPlans
import io.octatec.horext.api.repository.table.Subjects
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.insert
import java.time.Instant
import java.util.UUID

internal data class ScheduleGraph(
    val idBase: Long,
    val courseId: String,
    val organizationId: Long,
    val studyPlanId: Long,
    val subjectId: Long,
    val hourlyLoadId: Long,
    val linkedScheduleId: Long,
    val unrelatedScheduleId: Long,
    val scheduleSubjectId: Long,
)

internal fun createScheduleGraph(): ScheduleGraph {
    val suffix = UUID.randomUUID().toString().take(8)
    val idBase = 1_000_000_000L + (suffix.hashCode().toLong() and 0x0fffffff)
    val now = Instant.now()
    val courseId = "JOIN-$suffix"
    val firstSectionId = "X${suffix[0]}"
    val secondSectionId = "Y${suffix[0]}"
    val organizationTypeId = idBase
    val organizationId = idBase + 1
    val academicPeriodId = idBase + 2
    val apouId = idBase + 3
    val studyPlanId = idBase + 4
    val subjectId = idBase + 5
    val hourlyLoadId = idBase + 6
    val linkedScheduleId = idBase + 7
    val unrelatedScheduleId = idBase + 8
    val scheduleSubjectId = idBase + 9

    OrganizationUnitTypes.insert {
        it[OrganizationUnitTypes.id] = EntityID(organizationTypeId, OrganizationUnitTypes)
        it[OrganizationUnitTypes.name] = "Test type $suffix"
    }
    OrganizationUnits.insert {
        it[OrganizationUnits.id] = EntityID(organizationId, OrganizationUnits)
        it[OrganizationUnits.code] = "OU-$suffix"
        it[OrganizationUnits.name] = "Test unit $suffix"
        it[OrganizationUnits.typeId] = EntityID(organizationTypeId, OrganizationUnitTypes)
    }
    AcademicPeriods.insert {
        it[AcademicPeriods.id] = EntityID(academicPeriodId, AcademicPeriods)
        it[AcademicPeriods.code] = "AP-$suffix"
        it[AcademicPeriods.name] = "Test period $suffix"
    }
    AcademicPeriodOrganizationUnits.insert {
        it[AcademicPeriodOrganizationUnits.id] = EntityID(apouId, AcademicPeriodOrganizationUnits)
        it[AcademicPeriodOrganizationUnits.academicPeriodId] = EntityID(academicPeriodId, AcademicPeriods)
        it[AcademicPeriodOrganizationUnits.organizationUnitId] = EntityID(organizationId, OrganizationUnits)
    }
    StudyPlans.insert {
        it[StudyPlans.id] = EntityID(studyPlanId, StudyPlans)
        it[StudyPlans.code] = "SP-$suffix"
        it[StudyPlans.name] = "Test plan $suffix"
        it[StudyPlans.organizationUnitId] = EntityID(organizationId, OrganizationUnits)
        it[StudyPlans.createdAt] = now
        it[StudyPlans.updatedAt] = now
    }
    Courses.insert {
        it[Courses.id] = EntityID(courseId, Courses)
        it[Courses.name] = "Join regression course"
        it[Courses.createdAt] = now
        it[Courses.updatedAt] = now
    }
    Subjects.insert {
        it[Subjects.id] = EntityID(subjectId, Subjects)
        it[Subjects.courseId] = EntityID(courseId, Courses)
        it[Subjects.studyPlanId] = EntityID(studyPlanId, StudyPlans)
        it[Subjects.createdAt] = now
        it[Subjects.updatedAt] = now
    }
    Sections.insert {
        it[Sections.id] = EntityID(firstSectionId, Sections)
        it[Sections.code] = firstSectionId
    }
    Sections.insert {
        it[Sections.id] = EntityID(secondSectionId, Sections)
        it[Sections.code] = secondSectionId
    }
    HourlyLoads.insert {
        it[HourlyLoads.id] = EntityID(hourlyLoadId, HourlyLoads)
        it[HourlyLoads.name] = "Test load $suffix"
        it[HourlyLoads.academicPeriodOrganizationUnitId] = EntityID(apouId, AcademicPeriodOrganizationUnits)
    }
    Schedules.insert {
        it[Schedules.id] = EntityID(linkedScheduleId, Schedules)
        it[Schedules.courseId] = EntityID(courseId, Courses)
        it[Schedules.sectionId] = EntityID(firstSectionId, Sections)
    }
    Schedules.insert {
        it[Schedules.id] = EntityID(unrelatedScheduleId, Schedules)
        it[Schedules.courseId] = EntityID(courseId, Courses)
        it[Schedules.sectionId] = EntityID(secondSectionId, Sections)
    }
    ScheduleSubjects.insert {
        it[ScheduleSubjects.id] = EntityID(scheduleSubjectId, ScheduleSubjects)
        it[ScheduleSubjects.scheduleId] = EntityID(linkedScheduleId, Schedules)
        it[ScheduleSubjects.subjectId] = EntityID(subjectId, Subjects)
        it[ScheduleSubjects.hourlyLoadId] = EntityID(hourlyLoadId, HourlyLoads)
    }

    return ScheduleGraph(
        idBase = idBase,
        courseId = courseId,
        organizationId = organizationId,
        studyPlanId = studyPlanId,
        subjectId = subjectId,
        hourlyLoadId = hourlyLoadId,
        linkedScheduleId = linkedScheduleId,
        unrelatedScheduleId = unrelatedScheduleId,
        scheduleSubjectId = scheduleSubjectId,
    )
}

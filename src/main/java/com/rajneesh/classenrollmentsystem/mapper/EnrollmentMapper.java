package com.rajneesh.classenrollmentsystem.mapper;

import com.rajneesh.classenrollmentsystem.domain.entity.CourseEnrollmentEntity;
import com.rajneesh.classenrollmentsystem.domain.entity.EnrollmentEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Enrollment;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.stream.Collectors;

/**
 * This class is a map struct class to map the object from {@link EnrollmentEntity} to
 * {@link Enrollment} vice versa.
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnrollmentMapper {

	EnrollmentEntity toEntity(Enrollment dto);

	Enrollment toDto(EnrollmentEntity entity);

	@AfterMapping
	default void setCourses(@MappingTarget Enrollment enrollment, EnrollmentEntity enrollmentEntity) {
		enrollment.setCourses(new CourseMapperImpl()
				.toDtos(enrollmentEntity
						.getCourseEnrollments()
						.stream()
						.map(CourseEnrollmentEntity::getCourse)
						.collect(Collectors.toSet())));
	}

}
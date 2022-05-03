package com.rajneesh.classenrollmentsystem.mapper;

import com.rajneesh.classenrollmentsystem.domain.entity.CourseEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Course;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

/**
 * This class is a map struct class to map the object from {@link CourseEntity}
 * to {@link Course} vice versa.
 *
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

	CourseEntity toEntity(Course dto);

	Course toDto(CourseEntity entity);

	Set<Course> toDtos(Set<CourseEntity> entities);

}
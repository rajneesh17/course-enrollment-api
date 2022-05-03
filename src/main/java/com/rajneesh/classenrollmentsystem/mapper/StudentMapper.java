package com.rajneesh.classenrollmentsystem.mapper;

import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Student;
import com.rajneesh.classenrollmentsystem.domain.modal.StudentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

/**
 * This class is a map struct class to map the object from {@link StudentEntity} to
 * {@link Student} vice versa.
 *
 */
@Mapper(componentModel = "spring", uses = {
		EnrollmentMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

	Student toDto(StudentEntity entity);

	Set<Student> toDtos(Set<StudentEntity> entities);

	StudentEntity toEntity(StudentRequest student);

}

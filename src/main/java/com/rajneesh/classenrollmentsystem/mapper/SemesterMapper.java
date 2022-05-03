package com.rajneesh.classenrollmentsystem.mapper;

import com.rajneesh.classenrollmentsystem.domain.entity.SemesterEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Semester;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * This class is a map struct class to map the object from {@link SemesterEntity}
 * to {@link Semester} vice versa.
 *
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SemesterMapper {


	SemesterEntity toEntity(Semester dto);

	Semester toDto(SemesterEntity entity);

}

package com.rajneesh.classenrollmentsystem.domain.modal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rajneesh.classenrollmentsystem.domain.entity.EnrollmentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * A Data Transformation Object for {@link EnrollmentEntity}
 */
@Getter
@Setter
@NoArgsConstructor
public class Enrollment {

	private Semester semester;
	private Set<Course> courses;
	@JsonInclude(Include.NON_NULL)
	private Boolean isFullTime;
}

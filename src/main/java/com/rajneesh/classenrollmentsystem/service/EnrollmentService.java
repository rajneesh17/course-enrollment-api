package com.rajneesh.classenrollmentsystem.service;

import com.rajneesh.classenrollmentsystem.domain.modal.EnrollmentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * A service class to perform the logic for the enrollments.
 */
@Validated
public interface EnrollmentService {

	/**
	 * 
	 * Enroll a student to a class for a particular semester
	 *
     * @param enrollmentRequest request
     * @return
     */
	ResponseEntity<Object> enroll(@Valid EnrollmentRequest enrollmentRequest);

	/**
	 * Drop a student from a class.
	 *
	 * @param id id of the student
	 * @param course course
	 */
	void drop(
			@Valid @NotNull(message = "{request.enrollment.studentid.notnull}") Long id,
			@NotEmpty(message = "{request.enrollment.course.notempty}") String course);
}

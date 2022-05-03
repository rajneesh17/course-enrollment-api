package com.rajneesh.classenrollmentsystem.service;

import com.rajneesh.classenrollmentsystem.domain.entity.SemesterEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Semester;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * A service class to perform the logic for {@link SemesterEntity}.
 * 
 */
@Validated
public interface SemesterService {

	/**
	 * Add a semester
	 * 
	 * @param semester request
	 * @return semester object
	 */
	Semester add(@Valid Semester semester);

	/**
	 * Find a semester by its name
	 * 
	 * @param name name of semester
	 * @return semester entity
	 */
	SemesterEntity findEntityByName(@Valid @NotEmpty(message = "{find.semester.name.notempty}") String name);

	/**
	 * Find a semester by its name
	 *
	 * @param name name of semester
	 * @return semester object
	 */
	Semester findByName(@Valid @NotEmpty(message = "{find.semester.name.notempty}") String name);

}

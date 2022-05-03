package com.rajneesh.classenrollmentsystem.service;

import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.SearchRequest;
import com.rajneesh.classenrollmentsystem.domain.modal.Student;
import com.rajneesh.classenrollmentsystem.domain.modal.StudentRequest;
import com.rajneesh.classenrollmentsystem.domain.modal.validation.CreationValidationGroup;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * A service class to perform the logic for {@link StudentEntity}.
 *
 */
@Validated
public interface StudentService {

	/**
	 * 
	 * Add a new student with the basic information.
	 * 
	 * @param student request
	 * @return Student
	 */
	@Validated(CreationValidationGroup.class)
	Student add(@Valid StudentRequest student);

	/**
	 * Update the information in the existing student record.
	 *
	 * @param student request
	 * @return student object
	 */
	Student update(@Valid StudentRequest student);

	/**
	 * 
	 * Find Student by ID
	 * 
	 * @param id unique id
	 * @return student object
	 */
	Student findStudentById(@Valid @NotNull(message = "Id is required.") Long id);

	/**
	 * 
	 * Get the collection of students enrolled in a class for a particular semester.
	 * 
	 * @param searchRequest request object
	 * @return collection of student
	 */
	Set<Student> fetchStudents(@Valid SearchRequest searchRequest);

}

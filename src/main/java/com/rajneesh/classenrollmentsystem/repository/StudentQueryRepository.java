package com.rajneesh.classenrollmentsystem.repository;

import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.SearchRequest;
import org.springframework.data.domain.Page;

/**
 * 
 * {@link StudentQueryRepository} is an interface to fetch the records by using criteria builder
 */
public interface StudentQueryRepository {

	/**
	 * Gets the list of students enrolled in a class for a particular semester.
	 * 
	 * @param searchRequest request object
	 * @return collection of student
	 */
	Page<StudentEntity> fetchStudents(SearchRequest searchRequest);

}

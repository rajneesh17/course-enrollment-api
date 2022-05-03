package com.rajneesh.classenrollmentsystem.repository;


import com.rajneesh.classenrollmentsystem.domain.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 
 * {@link CourseRepository} to persist/fetch {@link CourseEntity} collection from rdbms database.
 *
 */
@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {

	/**
	 * Finds the {@link CourseEntity} by its name.
	 * 
	 * @param name
	 * @return optional CourseEntity
	 */
	Optional<CourseEntity> findByName(String name);

}

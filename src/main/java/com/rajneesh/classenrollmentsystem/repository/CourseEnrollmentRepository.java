package com.rajneesh.classenrollmentsystem.repository;


import com.rajneesh.classenrollmentsystem.domain.entity.CourseEnrollmentEntity;
import com.rajneesh.classenrollmentsystem.domain.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 
 * {@link CourseEnrollmentRepository} to persist/fetch {@link CourseEnrollmentEntity} collection from rdbms database.
 *
 */
@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollmentEntity, Integer> {

}

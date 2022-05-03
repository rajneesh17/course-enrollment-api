package com.rajneesh.classenrollmentsystem.repository;

import com.rajneesh.classenrollmentsystem.domain.entity.SemesterEntity;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link SemesterRepository} to persist/fetch {@link SemesterEntity} collection from rdbms database.
 * 
 */
@Repository
public interface SemesterRepository extends JpaRepository<SemesterEntity, Integer> {

	/**
	 * Find {@link SemesterEntity} by its name.
	 * 
	 * @param name
	 * @return
	 */
	Optional<SemesterEntity> findByName(String name);
}

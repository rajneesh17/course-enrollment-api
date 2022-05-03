package com.rajneesh.classenrollmentsystem.repository;

import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link StudentRepository} to persist/fetch {@link StudentEntity} collection from  database
 */
@Repository
public interface StudentRepository
        extends
        JpaRepository<StudentEntity, Long>, StudentQueryRepository  {

    @Query("select student from StudentEntity student " +
            " left join fetch student.enrollments enrollment " +
            " where student.id =:id")
    Optional<StudentEntity> findStudentById(@Param("id") Long id);

    @Query("select case when count(student) > 0 then true else false end from StudentEntity student where student.id =:id")
    boolean existsStudentById(@Param("id") Long id);
}

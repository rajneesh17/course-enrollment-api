package com.rajneesh.classenrollmentsystem.repository;

import com.rajneesh.classenrollmentsystem.domain.entity.CourseEntity;
import com.rajneesh.classenrollmentsystem.domain.entity.EnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 *  enrollment repository to persist/fetch data from enrollment table/child table
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, Long> {

    @Modifying
    @Query("delete from CourseEnrollmentEntity ce where ce.course.id = :courseId and ce.enrollment in (select enroll from EnrollmentEntity enroll  " +
            " where enroll.student.id = :id and ce.enrollment.id = enroll.id)")
    void drop(@Param("id") Long id, @Param("courseId") Long courseId);

    @Query("select distinct ce.course from EnrollmentEntity enrollment join CourseEnrollmentEntity ce on ce.enrollment.id = enrollment.id " +
            " where enrollment.student.id =:id and enrollment.semester.name =:semesterName")
    Set<CourseEntity> fetchCourses(@Param("id") Long id, @Param("semesterName") String semesterName);

}

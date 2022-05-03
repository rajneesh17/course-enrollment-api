package com.rajneesh.classenrollmentsystem.service;

import com.rajneesh.classenrollmentsystem.domain.entity.CourseEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Course;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * A service class to perform the logic for {@link Course}.
 */
@Validated
public interface CourseService {

    /**
     * To add a {@link Course}
     *
     * @param course input object
     * @return course
     */
    Course add(@Valid Course course);

    /**
     * Get the list of classes for a particular student for a semester, or the history of classes enrolled.
     *
     * @param studentId id of the student
     * @param semesterName semester name
     * @return collection of course
     */
    Set<Course> fetchedCourses(@Valid @NotNull(message = "{fetch.course.id.notnull}") Long studentId, String semesterName);

    /**
     * Find the class by its name
     *
     * @param name name of course
     * @return course entity
     */
    CourseEntity findEntityByName(@Valid @NotEmpty(message = "{find.course.name.notempty}") String name);

    /**
     * Find the class by its name
     *
     * @param name name of course
     * @return course dto
     */
    Course findByName(@Valid @NotEmpty(message = "{find.course.name.notempty}") String name);

}

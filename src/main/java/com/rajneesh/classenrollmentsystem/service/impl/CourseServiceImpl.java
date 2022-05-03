package com.rajneesh.classenrollmentsystem.service.impl;


import com.rajneesh.classenrollmentsystem.domain.entity.CourseEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Course;
import com.rajneesh.classenrollmentsystem.exception.RecordNotFoundException;
import com.rajneesh.classenrollmentsystem.mapper.CourseMapper;
import com.rajneesh.classenrollmentsystem.repository.CourseRepository;
import com.rajneesh.classenrollmentsystem.repository.EnrollmentRepository;
import com.rajneesh.classenrollmentsystem.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.text.MessageFormat.format;

/**
 * An implementation class for {@link CourseService}
 */
@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseClassRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Set<Course> fetchedCourses(Long studentId, String semesterName) {
        log.info("Fetching the classes enrolled for the student id - {} and semester name - {}"
                , studentId
                , semesterName);
        Set<CourseEntity> fetchedCourses = enrollmentRepository.fetchCourses(studentId, semesterName);
        return toDtos(fetchedCourses);
    }

    @Override
    public Course add(Course course) {
        log.info("Saving the class - {} into the database", course.getName());
        return toDto(save(course));
    }

    @CachePut(value = "courses", key = "#course.name")
    public CourseEntity save(Course course) {
        return courseClassRepository.save(toEntity(course));
    }

    @Override
    @Cacheable(value = "courses", key = "#name", unless = "#result==null")
    public CourseEntity findEntityByName(String name) {
        log.info("Loading the class - {} from the database.", name);
        return courseClassRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException(
                        format("Course {0} not found.", name)));
    }

    @Override
    public Course findByName(String name) {
        return toDto(findEntityByName(name));
    }

    private CourseEntity toEntity(Course course) {
        return courseMapper.toEntity(course);
    }

    private Course toDto(CourseEntity course) {
        return courseMapper.toDto(course);
    }

    private Set<Course> toDtos(Set<CourseEntity> entities) {
        return courseMapper.toDtos(entities);
    }

}

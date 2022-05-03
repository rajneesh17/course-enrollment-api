package com.rajneesh.classenrollmentsystem.service.impl;

import com.rajneesh.classenrollmentsystem.domain.entity.*;
import com.rajneesh.classenrollmentsystem.domain.modal.Course;
import com.rajneesh.classenrollmentsystem.domain.modal.EnrollmentRequest;
import com.rajneesh.classenrollmentsystem.exception.AlreadyCourseEnrollException;
import com.rajneesh.classenrollmentsystem.exception.InvalidEnrollmentRequestException;
import com.rajneesh.classenrollmentsystem.exception.StudentInfoNotFoundException;
import com.rajneesh.classenrollmentsystem.repository.EnrollmentRepository;
import com.rajneesh.classenrollmentsystem.repository.StudentRepository;
import com.rajneesh.classenrollmentsystem.service.CourseService;
import com.rajneesh.classenrollmentsystem.service.EnrollmentService;
import com.rajneesh.classenrollmentsystem.service.SemesterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.rajneesh.classenrollmentsystem.util.Constants.MAX_CREDIT_PER_SEMESTER;
import static com.rajneesh.classenrollmentsystem.util.Constants.MIN_CREDIT_FOR_FULLTIME;


/**
 * An implementation class for {@link EnrollmentService}.
 */
@Slf4j
@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public ResponseEntity<Object> enroll(EnrollmentRequest enrollReq) {
        log.debug(
                "Enrolling the student {} into the class {} for the semester {}",
                enrollReq.getStudentId(), enrollReq.getSemester(),
                enrollReq.getCourse());

        StudentEntity fetchedStudent = findOrElseThrowEx(enrollReq.getStudentId());
        SemesterEntity semester = semesterService.findEntityByName(enrollReq.getSemester());
        CourseEntity course = courseService.findEntityByName(enrollReq.getCourse());

        log.debug("Finding the existing enrollments for the student {} and the semester {}", fetchedStudent.getId(), semester.getName());

        Optional<EnrollmentEntity> existingEnrollmentOpt = findExistingEnrollment(fetchedStudent, semester.getName());
        EnrollmentEntity enrollment;
        if (existingEnrollmentOpt.isPresent()) {
            log.debug("Found the current enrollments for the student {} and the semester {}", fetchedStudent.getId(), semester.getName());
            enrollment = existingEnrollmentOpt.get();
        } else {
            log.debug("No current enrollments for the student {} and the semester {}", fetchedStudent.getId(), semester.getName());
            enrollment = new EnrollmentEntity();
            enrollment.setSemester(semester);
            fetchedStudent.addEnrollment(enrollment);
        }
        if (enrollment.getCourseEnrollments().stream().noneMatch(ce -> Objects.equals(ce.getCourse().getName(), course.getName()))) {
            CourseEnrollmentEntity courseEnrollment = new CourseEnrollmentEntity();
            courseEnrollment.setEnrollment(enrollment);
            courseEnrollment.setCourse(course);
            enrollment.addCourseEnrollment(courseEnrollment);
            int sumOfCredits = sumOfCredits(enrollment);
            log.debug("Total credit for the semester - {} = {} ", semester.getName(), sumOfCredits);

            Boolean isFullTime = Boolean.FALSE;
            // Each student is only allowed to be enrolled in a maximum of 20 credits for each semester.
            if (sumOfCredits > MAX_CREDIT_PER_SEMESTER) {
                log.error("Total credit {} for the semester exceeds the limit {}", sumOfCredits, MAX_CREDIT_PER_SEMESTER);
                throw new InvalidEnrollmentRequestException();
            }
            // There is a minimum of 10 credits to be considered full time.
            else if (sumOfCredits >= MIN_CREDIT_FOR_FULLTIME) {
                log.error("Total credit {} for the semester exceeds the minimum limit {} to be considered full time", sumOfCredits, MAX_CREDIT_PER_SEMESTER);
                isFullTime = Boolean.TRUE;
            }
            enrollment.setIsFullTime(isFullTime);
        } else {
            throw new AlreadyCourseEnrollException(fetchedStudent.getStudentId(), course.getName());
        }

        studentRepository.save(fetchedStudent);
        return null;
    }

    @Override
    @Transactional
    public void drop(Long id, String course) {
        log.debug("Dropping the student {} from the class {}", id, course);
        enrollmentRepository.drop(id, courseService.findEntityByName(course).getId());
    }

    private Optional<EnrollmentEntity> findExistingEnrollment(StudentEntity fetchedStudent, String semesterName) {
        return fetchedStudent.getEnrollments().isEmpty()
                ? Optional.empty()
                : fetchedStudent.getEnrollments().stream()
                .filter(existingEnroll -> StringUtils.equalsIgnoreCase(
                        existingEnroll.getSemester().getName(),
                        semesterName))
                .findFirst();
    }

    private int sumOfCredits(EnrollmentEntity enrollment) {
        return findDocumentsByNames(enrollment.getCourseEnrollments()
                .stream()
                .map(CourseEnrollmentEntity::getCourse)
                .map(CourseEntity::getName)
                .collect(Collectors.toSet()))
                .stream()
                .mapToInt(Course::getCredit)
                .sum();
    }

    private List<Course> findDocumentsByNames(Set<String> classes) {
        return classes.stream()
                .map(course -> courseService.findByName(course))
                .collect(Collectors.toList());
    }

    private StudentEntity findOrElseThrowEx(Long id) {
        return studentRepository.findStudentById(id)
                .orElseThrow(() -> new StudentInfoNotFoundException(id));
    }

}

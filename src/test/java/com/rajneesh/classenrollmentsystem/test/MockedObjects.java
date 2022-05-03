package com.rajneesh.classenrollmentsystem.test;


import com.rajneesh.classenrollmentsystem.domain.entity.*;
import com.rajneesh.classenrollmentsystem.domain.modal.*;

import java.time.LocalDate;

/**
 * Mocked Objects for Api's junits.
 */
public final class MockedObjects {

    private MockedObjects() {

    }

    public static StudentEntity mockedStudent(SemesterEntity semester, CourseEntity course) {
        return mockedStudent(semester, course, true);
    }

    public static StudentEntity mockedStudent() {
        return mockedStudent(mockedSemester(), mockedCourse(), true);
    }

    public static StudentEntity mockedStudent(SemesterEntity semester, CourseEntity course, boolean flag) {
        StudentEntity student = new StudentEntity();
        student.setId(1L);
        student.setFirstName("TestFN");
        student.setLastName("TestLN");
        student.setNationality("US");
        EnrollmentEntity enrollment = new EnrollmentEntity();
        if (flag) {
            enrollment.addCourseEnrollment(mockedCourseEnrollmentClass(course));
        }
        enrollment.setSemester(semester);
        student.addEnrollment(enrollment);
        return student;
    }

    public static SemesterEntity mockedSemester() {
        SemesterEntity semester = new SemesterEntity();
        semester.setName("Summer2022");
        semester.setStartDate(LocalDate.now());
        semester.setEndDate(LocalDate.now().plusDays(90));
        return semester;
    }

    public static Semester mockedSemesterDto() {
        Semester semester = new Semester();
        semester.setName("Summer2022");
        semester.setStartDate(LocalDate.now());
        semester.setEndDate(LocalDate.now().plusDays(90));
        return semester;
    }

    public static CourseEnrollmentEntity mockedCourseEnrollmentClass(CourseEntity course) {
        CourseEnrollmentEntity courseEnrollment = new CourseEnrollmentEntity();
        courseEnrollment.setCourse(course);
        return courseEnrollment;
    }

    public static CourseEntity mockedCourse() {
        return mockCourse("classA");
    }
    public static CourseEntity mockCourse(String courseString) {
        CourseEntity course = new CourseEntity();
        course.setName(courseString);
        course.setCredit(4);
        return course;
    }

    public static Course mockedCourseDto() {
        Course course = new Course();
        course.setName("classA");
        course.setCredit(4);
        return course;
    }

    public static StudentRequest mockedStudentRequest() {
        StudentRequest student = new StudentRequest();
        student.setId(1L);
        student.setFirstName("TestFN");
        student.setLastName("TestLN");
        student.setNationality("US");
        return student;
    }

    public static EnrollmentRequest mockedEnrollmentRequestDto() {
        return mockedEnrollmentRequestDto("classB");
    }

    public static EnrollmentRequest mockedEnrollmentRequestDto(String course) {
        EnrollmentRequest request = new EnrollmentRequest();
        request.setCourse(course);
        request.setSemester("Summer2022");
        request.setStudentId(1L);
        return request;
    }

    public static SearchRequest mockedSearchRequestDto() {
        SearchRequest request = new SearchRequest();
        request.setCourse("classA");
        request.setSemester("Summer2022");
        request.setPageNo(0);
        request.setPageSize(10);
        request.setSortBy("id");
        request.setDirection("ASC");
        return request;
    }

}

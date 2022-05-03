package com.rajneesh.classenrollmentsystem.impl;

import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.EnrollmentRequest;
import com.rajneesh.classenrollmentsystem.exception.AlreadyCourseEnrollException;
import com.rajneesh.classenrollmentsystem.exception.InvalidEnrollmentRequestException;
import com.rajneesh.classenrollmentsystem.exception.RecordNotFoundException;
import com.rajneesh.classenrollmentsystem.exception.StudentInfoNotFoundException;
import com.rajneesh.classenrollmentsystem.mapper.CourseMapperImpl;
import com.rajneesh.classenrollmentsystem.mapper.SemesterMapperImpl;
import com.rajneesh.classenrollmentsystem.repository.CourseRepository;
import com.rajneesh.classenrollmentsystem.repository.EnrollmentRepository;
import com.rajneesh.classenrollmentsystem.repository.SemesterRepository;
import com.rajneesh.classenrollmentsystem.repository.StudentRepository;
import com.rajneesh.classenrollmentsystem.service.CourseService;
import com.rajneesh.classenrollmentsystem.service.EnrollmentService;
import com.rajneesh.classenrollmentsystem.service.impl.CourseServiceImpl;
import com.rajneesh.classenrollmentsystem.service.impl.EnrollmentServiceImpl;
import com.rajneesh.classenrollmentsystem.service.impl.SemesterServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static com.rajneesh.classenrollmentsystem.test.MockedObjects.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests all {@link EnrollmentService} service methods.
 * 
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EnrollmentServiceImpl.class,
		SemesterServiceImpl.class, CourseServiceImpl.class,
		CourseMapperImpl.class, SemesterMapperImpl.class})
class EnrollmentServiceImplTest {

	@Autowired
	private EnrollmentService enrollmentService;

	@MockBean
	private EnrollmentRepository enrollmentRepository;

	@MockBean
	private StudentRepository studentRepository;

	@MockBean
	private CourseRepository courseClassRepository;

	@MockBean
	private SemesterRepository semesterRepository;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Enroll a class when student id not found")
	final void testEnroll_whenStudentIdNotFound() {
		EnrollmentRequest mockedRequest = mockedEnrollmentRequestDto();
		assertThrows(StudentInfoNotFoundException.class,
				() -> enrollmentService.enroll(mockedRequest));
	}

	@Test
	@DisplayName("Enroll a class when class name not found")
	final void testEnroll_whenClassNameNotFound() {

		when(studentRepository.findStudentById(1L))
				.thenReturn(Optional.of(mockedStudent(mockedSemester(), mockedCourse())));
		EnrollmentRequest mockedRequest = mockedEnrollmentRequestDto();

		assertThrows(RecordNotFoundException.class,
				() -> enrollmentService.enroll(mockedRequest));
	}

	@Test
	@DisplayName("Enroll a class when semester name not found")
	final void testEnroll_whenSemesterNameNotFound() {

		when(studentRepository.findStudentById(1L))
				.thenReturn(Optional.of(mockedStudent()));
		when(semesterRepository.findByName("Summer2022"))
				.thenReturn(Optional.of(mockedSemester()));

		EnrollmentRequest mockedRequest = mockedEnrollmentRequestDto();

		assertThrows(RecordNotFoundException.class,
				() -> enrollmentService.enroll(mockedRequest));
	}

	@Test
	@DisplayName("Enroll a class")
	final void testEnroll() {

		when(studentRepository.findStudentById(1L))
				.thenReturn(Optional.of(mockedStudent()));

		when(semesterRepository.findByName("Summer2022"))
				.thenReturn(Optional.of(mockedSemester()));

		when(courseClassRepository.findByName("classB"))
				.thenReturn(Optional.of(mockCourse("classB")));
		when(courseClassRepository.findByName("classA"))
				.thenReturn(Optional.of(mockedCourse()));

		enrollmentService.enroll(mockedEnrollmentRequestDto());

		verify(studentRepository, times(1)).save(any());

	}

	@Test
	@DisplayName("Enroll a class with no prior enrollments")
	final void testEnroll_WithNoPriorEnrollments() {

		StudentEntity mockedStudent = mockedStudent(mockedSemester(), mockedCourse(), false);
		mockedStudent.setEnrollments(new ArrayList<>());
		when(studentRepository.findStudentById(1L))
				.thenReturn(Optional.of(mockedStudent));
		when(semesterRepository.findByName("Summer2022"))
				.thenReturn(Optional.of(mockedSemester()));
		when(courseClassRepository.findByName("classB"))
				.thenReturn(Optional.of(mockCourse("classB")));

		enrollmentService.enroll(mockedEnrollmentRequestDto());

		verify(studentRepository, times(1)).save(any());

	}

	@Test
	@DisplayName("Enroll a Full Time Student")
	final void testEnrollFullTime() {

		StudentEntity student = mockedStudent();
		when(studentRepository.findStudentById(1L))
				.thenReturn(Optional.of(student));

		when(semesterRepository.findByName("Summer2022"))
				.thenReturn(Optional.of(mockedSemester()));

		when(courseClassRepository.findByName("classB"))
				.thenReturn(Optional.of(mockCourse("classB")));
		when(courseClassRepository.findByName("classA"))
				.thenReturn(Optional.of(mockCourse("classA")));
		when(courseClassRepository.findByName("classC"))
				.thenReturn(Optional.of(mockCourse("classC")));;

		enrollmentService.enroll(mockedEnrollmentRequestDto());
		enrollmentService.enroll(mockedEnrollmentRequestDto("classC"));

		assertTrue(student.getEnrollments().get(0).getIsFullTime());
	}



	@Test
	@DisplayName("Student Max Credit ")
	final void testMaxCredit() {

		StudentEntity student = mockedStudent();
		when(studentRepository.findStudentById(1L))
				.thenReturn(Optional.of(student));

		when(semesterRepository.findByName("Summer2022"))
				.thenReturn(Optional.of(mockedSemester()));

		when(courseClassRepository.findByName("classB"))
				.thenReturn(Optional.of(mockCourse("classB")));
		when(courseClassRepository.findByName("classA"))
				.thenReturn(Optional.of(mockCourse("classA")));
		when(courseClassRepository.findByName("classC"))
				.thenReturn(Optional.of(mockCourse("classC")));
		when(courseClassRepository.findByName("classD"))
				.thenReturn(Optional.of(mockCourse("classD")));
		when(courseClassRepository.findByName("classE"))
				.thenReturn(Optional.of(mockCourse("classE")));
		when(courseClassRepository.findByName("classF"))
				.thenReturn(Optional.of(mockCourse("classF")));

		enrollmentService.enroll(mockedEnrollmentRequestDto());
		enrollmentService.enroll(mockedEnrollmentRequestDto("classC"));
		enrollmentService.enroll(mockedEnrollmentRequestDto("classD"));
		enrollmentService.enroll(mockedEnrollmentRequestDto("classE"));


		assertThrows(InvalidEnrollmentRequestException.class,
				() -> enrollmentService.enroll(mockedEnrollmentRequestDto("classF")));
	}

	@Test
	@DisplayName("Course Enrolled already ")
	final void testCourseAlreadyEnrolled() {

		StudentEntity student = mockedStudent();
		when(studentRepository.findStudentById(1L))
				.thenReturn(Optional.of(student));
		when(courseClassRepository.findByName("classA"))
				.thenReturn(Optional.of(mockCourse("classA")));
		when(semesterRepository.findByName("Summer2022"))
				.thenReturn(Optional.of(mockedSemester()));

		assertThrows(AlreadyCourseEnrollException.class,
				() -> enrollmentService.enroll(mockedEnrollmentRequestDto("classA")));
	}

}

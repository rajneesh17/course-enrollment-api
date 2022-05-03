
package com.rajneesh.classenrollmentsystem.controller;

import com.rajneesh.classenrollmentsystem.domain.entity.CourseEntity;
import com.rajneesh.classenrollmentsystem.domain.entity.SemesterEntity;
import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.EnrollmentRequest;
import com.rajneesh.classenrollmentsystem.repository.CourseEnrollmentRepository;
import com.rajneesh.classenrollmentsystem.repository.CourseRepository;
import com.rajneesh.classenrollmentsystem.repository.SemesterRepository;
import com.rajneesh.classenrollmentsystem.repository.StudentRepository;
import com.rajneesh.classenrollmentsystem.test.IntegrationTestBase;
import com.rajneesh.classenrollmentsystem.test.NullableArgumentConverter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.rajneesh.classenrollmentsystem.test.AssertionHelper.*;
import static com.rajneesh.classenrollmentsystem.test.MockedObjects.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Tests all {@link EnrollmentController} APIs.
 *
 */
class EnrollmentControllerTest extends IntegrationTestBase {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CourseEnrollmentRepository courseEnrollmentRepository;

	@Autowired
	private SemesterRepository semesterRepository;

	private static final String BASE_CONTEXT_PATH = "/enrollments";

	/**
	 * @throws Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		// Needs to clear the embedded database after each method to make the
		// junit
		// methods work independently.
		courseEnrollmentRepository.deleteAll();
		studentRepository.deleteAll();
		courseRepository.deleteAll();
		semesterRepository.deleteAll();
	}

	@Nested
	@DisplayName("Test API to enroll a student into a class for a particular semester")
	class Enroll {

		/**
		 * Test method for
		 * {@link EnrollmentController#enroll(EnrollmentRequest)}.
		 */
		@Test
		@DisplayName("When no content in the body")
		final void testEnroll_withoutBody() throws Exception {
			ResultActions resultActions = mockMvc
					.perform(post(BASE_CONTEXT_PATH));

			assertBadRequest("Malformed JSON request", resultActions);
		}

		@Test
		@DisplayName("When the values are null in the request")
		final void testEnroll_withNullValues() throws Exception {

			EnrollmentRequest request = new EnrollmentRequest();
			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					request);

			assertBadRequest("Request has constraint violations", resultActions)
					.andExpect(jsonPath("$.errors").isArray())
					.andExpect(jsonPath("$.errors", hasSize(3)))
					.andExpect(jsonPath(
							"$.errors[?(@.field == 'enroll.enrollmentRequest.studentId')].message",
							hasItem("Id is required.")))
					.andExpect(jsonPath(
							"$.errors[?(@.field == 'enroll.enrollmentRequest.semester')].message",
							hasItem("Semester is required.")))
					.andExpect(jsonPath(
							"$.errors[?(@.field == 'enroll.enrollmentRequest.course')].message",
							hasItem("Course is required.")));
		}

		@Test
		@DisplayName("When the input is valid but the id is not found in the database")
		final void testEnroll_withValidInputNIdNotFound() throws Exception {

			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					mockedEnrollmentRequestDto());

			assertNotFound("Student id 1 not found.", resultActions);

		}

		@Test
		@DisplayName("When the input is valid")
		@Transactional
		void testEnroll_withValidInputNIdFound() throws Exception {

			SemesterEntity semesterEntity = semesterRepository.save(mockedSemester());
			CourseEntity courseEntity = courseRepository.save(mockCourse("classA"));
			studentRepository.save(mockedStudent(semesterEntity, courseEntity));

			courseRepository.save(mockCourse("classB"));

			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					mockedEnrollmentRequestDto());

			assertOK(resultActions);

			Optional<StudentEntity> fetchedStudent = studentRepository.findStudentById(1L);

			Assertions.assertTrue(fetchedStudent.isPresent());

			Assertions.assertEquals(2, fetchedStudent.get().getEnrollments()
					.get(0).getCourseEnrollments().size());
		}
	}

	@Nested
	@DisplayName("Test API to drop a student from a class.")
	class Drop {
		/**
		 * Test method for
		 * {@link EnrollmentController#drop(String, Long)}.
		 */
		@Test
		@DisplayName("When missing the parameters in the request")
		final void testDrop_missingParameters() throws Exception {
			ResultActions resultActions = mockMvc
					.perform(delete(BASE_CONTEXT_PATH));

			assertBadRequest("Missing Parameters", resultActions)
					.andExpect(jsonPath("$.errors").isArray())
					.andExpect(jsonPath("$.errors", hasSize(1)))
					.andExpect(jsonPath("$.errors[0].field", is("course")))
					.andExpect(
							jsonPath("$.errors[0].rejectedValue", nullValue()))
					.andExpect(jsonPath("$.errors[0].message",
							is("parameter is missing")));
		}

		@Test
		@DisplayName("When type is mismatch")
		final void testDrop_typeMismatch() throws Exception {
			ResultActions resultActions = mockMvc.perform(
					delete(BASE_CONTEXT_PATH).param("id", "invalidData")
							.param("course", "classA"));

			assertBadRequest("Type Mismatch", resultActions);
		}

		@ParameterizedTest
		@CsvSource({"123, null", "null, classA"})
		@DisplayName("When the input is valid")
		final void testDrop_invalidParameters(
				@ConvertWith(NullableArgumentConverter.class) String id,
				@ConvertWith(NullableArgumentConverter.class) String course)
				throws Exception {

			ResultActions resultActions = mockMvc
					.perform(delete(BASE_CONTEXT_PATH).param("id", id)
							.param("course", course));

			assertBadRequest("Missing Parameters", resultActions)
					.andExpect(jsonPath("$.errors").isArray())
					.andExpect(jsonPath("$.errors", hasSize(1)))
					.andExpect(
							jsonPath("$.errors[0].rejectedValue", nullValue()))
					.andExpect(jsonPath("$.errors[0].message",
							is("parameter is missing")));
		}

		@Test
		@DisplayName("When input is valid")
		@Transactional
		void testDrop_validInput() throws Exception {

			studentRepository.save(mockedStudent(semesterRepository.save(mockedSemester())
					, courseRepository.save(mockCourse("classA")), false));

			ResultActions resultActions = mockMvc
					.perform(delete(BASE_CONTEXT_PATH).param("id", "1")
							.param("course", "classA"));

			assertOK(resultActions);

			Optional<StudentEntity> fetchedStudent = studentRepository.findStudentById(1L);

			Assertions.assertTrue(fetchedStudent.isPresent());

			Assertions.assertTrue(fetchedStudent.get().getEnrollments().get(0)
					.getCourseEnrollments().isEmpty());
		}
	}

}

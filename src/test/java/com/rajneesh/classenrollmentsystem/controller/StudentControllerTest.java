
package com.rajneesh.classenrollmentsystem.controller;

import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.StudentRequest;
import com.rajneesh.classenrollmentsystem.repository.CourseEnrollmentRepository;
import com.rajneesh.classenrollmentsystem.repository.CourseRepository;
import com.rajneesh.classenrollmentsystem.repository.SemesterRepository;
import com.rajneesh.classenrollmentsystem.repository.StudentRepository;
import com.rajneesh.classenrollmentsystem.test.IntegrationTestBase;
import com.rajneesh.classenrollmentsystem.test.MockedObjects;
import com.rajneesh.classenrollmentsystem.test.NullableArgumentConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static com.rajneesh.classenrollmentsystem.test.AssertionHelper.*;
import static com.rajneesh.classenrollmentsystem.test.MockedObjects.mockedStudent;
import static com.rajneesh.classenrollmentsystem.test.MockedObjects.mockedStudentRequest;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Tests all {@link StudentController} APIs.
 */
class StudentControllerTest extends IntegrationTestBase {

    private static final String BASE_CONTEXT_PATH = "/students";

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseEnrollmentRepository courseEnrollmentRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    /**
     * @throws Exception
     */
    @AfterEach
    void tearDown() throws Exception {
        courseEnrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
        semesterRepository.deleteAll();
    }

    @Nested
    @DisplayName("Test API to add a new student")
    class CreateStudent {

        /**
         * Test method for
         * {@link StudentController#createStudent(StudentRequest)}.
         */
        @Test
        @DisplayName("When no content in the body")
        final void testCreateStudent_withoutBody() throws Exception {
            ResultActions resultActions = mockMvc
                    .perform(post(BASE_CONTEXT_PATH));

            assertBadRequest("Malformed JSON request", resultActions);
        }

        @Test
        @DisplayName("When the values are null in the request")
        final void testCreateStudent_withNullValues() throws Exception {

            StudentRequest semester = new StudentRequest();

            ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
                    semester);

            assertBadRequest("Request has constraint violations", resultActions)
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(3)))
                    .andExpect(jsonPath(
                            "$.errors[?(@.field == 'add.student.firstName')].message",
                            hasItem("First name is required.")))
                    .andExpect(jsonPath(
                            "$.errors[?(@.field == 'add.student.lastName')].message",
                            hasItem("Last name is required.")))
                    .andExpect(jsonPath(
                            "$.errors[?(@.field == 'add.student.id')].message",
                            hasItem("Id is required.")));
        }

        @Test
        @DisplayName("When the input is valid")
        final void testCreateStudent_withValidInput() throws Exception {

            ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
                    mockedStudentRequest());

            // SUCCESS Creation
            assertCreated(resultActions);

            // Trying the second time would return constraint violations
            ResultActions secondAttemptResultActions = performPost(
                    BASE_CONTEXT_PATH, mockedStudentRequest());

            assertBadRequest("Request has constraint violations",
                    secondAttemptResultActions)
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].field",
                            is("add.student.id")))
                    .andExpect(jsonPath("$.errors[0].rejectedValue",
                            is(1)))
                    .andExpect(jsonPath("$.errors[0].message",
                            is("Student id must be unique.")));
        }

    }

    @Nested
    @DisplayName("Test API to update the student")
    class UpdateStudent {

        /**
         * Test method for
         * {@link StudentController#updateStudent(StudentRequest)}.
         */
        @Test
        @DisplayName("When no content in the body")
        final void testUpdateStudent_withoutBody() throws Exception {
            ResultActions resultActions = mockMvc
                    .perform(put(BASE_CONTEXT_PATH));

            assertBadRequest("Malformed JSON request", resultActions);
        }

        @Test
        @DisplayName("When the values are null in the request")
        final void testUpdateStudent_withNullValues() throws Exception {

            StudentRequest studentRequest = new StudentRequest();

            ResultActions resultActions = performPut(BASE_CONTEXT_PATH,
                    studentRequest);

            assertBadRequest("Request has constraint violations", resultActions)
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(3)))
                    .andExpect(jsonPath(
                            "$.errors[?(@.field == 'update.student.firstName')].message",
                            hasItem("First name is required.")))
                    .andExpect(jsonPath(
                            "$.errors[?(@.field == 'update.student.lastName')].message",
                            hasItem("Last name is required.")))
                    .andExpect(jsonPath(
                            "$.errors[?(@.field == 'update.student.id')].message",
                            hasItem("Id is required.")));
        }

        @Test
        @DisplayName("When the input is valid but the id not found")
        final void testUpdateStudent_withValidInputNIdNotFound()
                throws Exception {

            ResultActions resultActions = performPut(BASE_CONTEXT_PATH,
                    mockedStudentRequest());

            assertNotFound("Student id 1 not found.", resultActions);
        }

        @Test
        @DisplayName("When the input is valid")
        final void testUpdateStudent_withValidInput() throws Exception {

            studentRepository.save(mockedStudent(semesterRepository.save(MockedObjects.mockedSemester())
                    , courseRepository.save(MockedObjects.mockCourse("classA"))));

            ResultActions resultActions = performPut(BASE_CONTEXT_PATH,
                    mockedStudentRequest());

            assertOK(resultActions);
        }

    }

    @Nested
    @DisplayName("Test API to get the student by id")
    class FindStudentById {
        /**
         * Test method for
         * {@link StudentController#findStudentById(Long)}.
         */
        @Test
        @DisplayName("When no value in the path variable")
        final void testFindStudentById_noPathProvided() throws Exception {
            ResultActions resultActions = mockMvc
                    .perform(get(BASE_CONTEXT_PATH));

            assertMethodNotAllowed(resultActions);
        }

        @Test
        @DisplayName("When the input is valid but no record found")
        final void testFindStudentById_validInputNoRecordFound()
                throws Exception {
            ResultActions resultActions = mockMvc
                    .perform(get(BASE_CONTEXT_PATH + "/{id}", "123"));

            assertNotFound("Student id 123 not found.", resultActions);
        }

        @Test
        @DisplayName("When the input is valid and the record is found")
        final void testFindStudentById_validRecordFound() throws Exception {

            StudentEntity mockedStudent = studentRepository.save(mockedStudent(semesterRepository.save(MockedObjects.mockedSemester())
                    , courseRepository.save(MockedObjects.mockCourse("classA"))));

            ResultActions resultActions = mockMvc
                    .perform(get(BASE_CONTEXT_PATH + "/{id}", "1"));

            assertOK(resultActions).andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.firstName",
                            is(mockedStudent.getFirstName())))
                    .andExpect(jsonPath("$.lastName",
                            is(mockedStudent.getLastName())))
                    .andExpect(jsonPath("$.id", is(1)));

        }

    }

    @Nested
    @DisplayName("Test API to get the list of students")
    class FetchStudent {

        @Test
        @DisplayName("When type is mismatch")
        final void testFetchStudents_typeMismatch() throws Exception {
            ResultActions resultActions = mockMvc
                    .perform(get("/fetchStudents").param("pageNo", "invalidData"));

            assertBadRequest("Type Mismatch", resultActions);
        }

        @ParameterizedTest
        @CsvSource({"classA, Summer2022,  1, 10, ASC, id",
                "classA, Summer2022,  1, 10, DESC, id",
                "classA, null,  null, null, null, null",
                "classA, Summer2022,  null, null, null, null",
                "classA, Summer2022,  null, null, null, null",
                "null, Summer2022,  null, null, null, null",
                "null, null, null, null, null, null"})
        @DisplayName("When the input is valid")
        final void testFetchStudents_ByStudentIdNSemesterWithEmptyResult(
                String course,
                String semester,
                @ConvertWith(NullableArgumentConverter.class) String pageNo,
                @ConvertWith(NullableArgumentConverter.class) String pageSize,
                @ConvertWith(NullableArgumentConverter.class) String direction,
                @ConvertWith(NullableArgumentConverter.class) String sortBy)
                throws Exception {

            studentRepository.save(mockedStudent(semesterRepository.save(MockedObjects.mockedSemester())
                    , courseRepository.save(MockedObjects.mockCourse("classA"))));

            ResultActions resultActions = mockMvc.perform(get("/fetchStudents")
                    .param("course", course).param("semester", semester).param("pageNo", pageNo)
                    .param("pageSize", pageSize).param("direction", direction)
                    .param("sortBy", sortBy));

            assertOK(resultActions).andExpect(jsonPath("$").isArray());
        }

    }

}

/**
 *
 */
package com.rajneesh.classenrollmentsystem.controller;

import com.rajneesh.classenrollmentsystem.domain.entity.CourseEnrollmentEntity;
import com.rajneesh.classenrollmentsystem.domain.entity.CourseEntity;
import com.rajneesh.classenrollmentsystem.domain.entity.SemesterEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Course;
import com.rajneesh.classenrollmentsystem.repository.CourseEnrollmentRepository;
import com.rajneesh.classenrollmentsystem.repository.CourseRepository;
import com.rajneesh.classenrollmentsystem.repository.SemesterRepository;
import com.rajneesh.classenrollmentsystem.repository.StudentRepository;
import com.rajneesh.classenrollmentsystem.test.IntegrationTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static com.rajneesh.classenrollmentsystem.test.AssertionHelper.*;
import static com.rajneesh.classenrollmentsystem.test.MockedObjects.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Tests all {@link CourseController} APIs.
 *
 */
class CourseControllerIntTest extends IntegrationTestBase {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseClassRepository;

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
        courseClassRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Nested
    @DisplayName("Test the API to get the list of classes")
    class FetchClass {

        /**
         * Test method for
         * {@link CourseController#fetchedCourses(String, Long)}.
         *
         * @throws Exception
         */
        @Test
        @DisplayName("When missing the parameters in the request")
        final void testFetchClassesWhenMissingParameters() throws Exception {
            ResultActions resultActions = mockMvc.perform(get("/fetchClasses"));

            assertBadRequest("Missing Parameters", resultActions)
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].field", is("id")))
                    .andExpect(
                            jsonPath("$.errors[0].rejectedValue", nullValue()))
                    .andExpect(jsonPath("$.errors[0].message",
                            is("parameter is missing")));
        }

        @Test
        @DisplayName("When type is mismatch")
        final void testFetchClassesWhenTypeMismatch() throws Exception {
            ResultActions resultActions = mockMvc
                    .perform(get("/fetchClasses").param("id", "invalidData"));

            assertBadRequest("Type Mismatch", resultActions);
        }

        @ParameterizedTest
        @CsvSource({"123, null", "123, Summer2022"})
        @DisplayName("When the input is valid")
        final void testFetchClasses_ByStudentIdNSemesterWithEmptyResult(
                String id, String semester) throws Exception {

            SemesterEntity semesterEntity = semesterRepository.save(mockedSemester());
            CourseEntity course = courseClassRepository.save(mockedCourse());
            studentRepository.save(mockedStudent(semesterEntity, course));

            ResultActions resultActions = mockMvc.perform(get("/fetchClasses")
                    .param("id", id).param("semester", semester));

            assertOK(resultActions).andExpect(jsonPath("$").isArray());
        }

    }

    @Nested
    @DisplayName("Test API to add new class")
    class CreateClass {

        /**
         * Test method for
         * {@link CourseController#createCourse(Course)}.
         *
         * @throws Exception
         */
        @Test
        @DisplayName("When no content in the body")
        final void testCreateClass_withoutBody() throws Exception {
            ResultActions resultActions = mockMvc.perform(post("/classes"));

            assertBadRequest("Malformed JSON request", resultActions);
        }

        @Test
        @DisplayName("When the class name is null")
        final void testCreateClass_withNullName() throws Exception {

            Course course = new Course();
            course.setName(null);
            course.setCredit(3);

            ResultActions resultActions = performPost("/classes", course);

            assertBadRequest("Request has constraint violations", resultActions)
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].field",
                            is("add.course.name")))
                    .andExpect(
                            jsonPath("$.errors[0].rejectedValue", nullValue()))
                    .andExpect(jsonPath("$.errors[0].message",
                            is("Course name is required.")));
        }

        @Test
        @DisplayName("When the class credit/unit is null")
        final void testCreateClass_withNullCredit() throws Exception {

            Course course = new Course();
            course.setName("classA");
            course.setCredit(null);

            ResultActions resultActions = performPost("/classes", course);

            assertBadRequest("Request has constraint violations", resultActions)
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].field",
                            is("add.course.credit")))
                    .andExpect(
                            jsonPath("$.errors[0].rejectedValue", nullValue()))
                    .andExpect(jsonPath("$.errors[0].message",
                            is("Credit is required.")));
        }

        @Test
        @DisplayName("When the class credit/unit is invalid")
        final void testCreateClass_withInvalidCredit() throws Exception {

            Course course = new Course();
            course.setName("classA");
            course.setCredit(10);

            ResultActions resultActions = performPost("/classes", course);

            assertBadRequest("Request has constraint violations", resultActions)
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].field",
                            is("add.course.credit")))
                    .andExpect(jsonPath("$.errors[0].rejectedValue", is(10)))
                    .andExpect(jsonPath("$.errors[0].message",
                            is("Credit cannot exceed 4 per class.")));
        }

        @Test
        @DisplayName("When the input is valid")
        final void testCreateClass_withValidInput() throws Exception {

            ResultActions resultActions = performPost("/classes", mockedCourse());
            assertCreated(resultActions);
        }

    }

    @Nested
    @DisplayName("Test API to get class by name")
    class FindClassByName {
        /**
         * Test method for
         * {@link CourseController#findClassByName(String)}.
         *
         * @throws Exception
         */
        @Test
        @DisplayName("When no value in the path variable")
        final void testFindClassByName_noPathProvided() throws Exception {
            ResultActions resultActions = mockMvc.perform(get("/classes/"));

            assertMethodNotAllowed(resultActions);
        }

        @Test
        @DisplayName("When the input is valid but no record found")
        final void testFindClassByName_validInputNoRecordFound()
                throws Exception {
            String className = "classB";
            ResultActions resultActions = mockMvc
                    .perform(get("/classes/{name}", className));

            assertNotFound("Course classB not found.", resultActions);
        }

        @Test
        @DisplayName("When the input is valid and the record is found")
        final void testFindClassByName_validRecordFound() throws Exception {

            courseClassRepository.save(mockedCourse());

            String className = "classA";
            ResultActions resultActions = mockMvc
                    .perform(get("/classes/{name}", className));

            assertOK(resultActions).andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.name", is("classA")))
                    .andExpect(jsonPath("$.credit", is(4)));

        }

    }

}

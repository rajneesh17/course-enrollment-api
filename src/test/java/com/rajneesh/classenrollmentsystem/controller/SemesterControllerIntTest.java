
package com.rajneesh.classenrollmentsystem.controller;

import com.rajneesh.classenrollmentsystem.domain.modal.Semester;
import com.rajneesh.classenrollmentsystem.repository.SemesterRepository;
import com.rajneesh.classenrollmentsystem.test.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static com.rajneesh.classenrollmentsystem.test.AssertionHelper.*;
import static com.rajneesh.classenrollmentsystem.test.MockedObjects.mockedSemester;
import static com.rajneesh.classenrollmentsystem.test.MockedObjects.mockedSemesterDto;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Tests all {@link SemesterController} APIs.
 */
class SemesterControllerIntTest extends IntegrationTestBase {

	private static final String BASE_CONTEXT_PATH = "/semesters";

	@Autowired
	private SemesterRepository semesterRepository;

	@Nested
	@DisplayName("API to add a new semester")
	class CreateSemester {

		/**
		 * Test method for
		 * {@link SemesterController#createSemester(Semester)}.
		 */
		@Test
		@DisplayName("When no content in the body")
		final void testCreateSemester_withoutBody() throws Exception {
			ResultActions resultActions = mockMvc
					.perform(post(BASE_CONTEXT_PATH));

			assertBadRequest("Malformed JSON request", resultActions);
		}

		@Test
		@DisplayName("When the semester name is null")
		final void testCreateSemester_withNullName() throws Exception {

			Semester semester = new Semester();
			semester.setName(null);

			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					semester);

			assertBadRequest("Request has constraint violations", resultActions)
					.andExpect(jsonPath("$.errors").isArray())
					.andExpect(jsonPath("$.errors", hasSize(1)))
					.andExpect(jsonPath("$.errors[0].field",
							is("add.semester.name")))
					.andExpect(
							jsonPath("$.errors[0].rejectedValue", nullValue()))
					.andExpect(jsonPath("$.errors[0].message",
							is("Semester name is required.")));
		}

		@Test
		@DisplayName("When the input is valid")
		final void testCreateSemester_withValidInput() throws Exception {

			ResultActions resultActions = performPost(BASE_CONTEXT_PATH,
					mockedSemesterDto());

			assertCreated(resultActions);
		}

	}

	@Nested
	@DisplayName("API to get the semester by name")
	class FindSemesterByName {
		/**
		 * Test method for
		 * {@link SemesterController#findSemesterByName(String)}.
		 */
		@Test
		@DisplayName("When no value in the path variable")
		final void testFindSemesterByName_noPathProvided() throws Exception {
			ResultActions resultActions = mockMvc
					.perform(get(BASE_CONTEXT_PATH));

			assertMethodNotAllowed(resultActions);
		}

		@Test
		@DisplayName("When the input is valid but no record found")
		final void testFindSemesterByName_validInputNoRecordFound()
				throws Exception {
			ResultActions resultActions = mockMvc
					.perform(get(BASE_CONTEXT_PATH + "/{name}", "Spring2022"));

			assertNotFound("Semester Spring2022 not found.", resultActions);
		}

		@Test
		@DisplayName("When the input is valid and the record is found")
		final void testFindSemesterByName_validRecordFound() throws Exception {

			semesterRepository.save(mockedSemester());

			ResultActions resultActions = mockMvc
					.perform(get(BASE_CONTEXT_PATH + "/{name}", "Summer2022"));

			assertOK(resultActions).andExpect(jsonPath("$").isNotEmpty())
					.andExpect(jsonPath("$.name", is("Summer2022")))
					.andExpect(jsonPath("$.startDate", notNullValue()));

		}

	}

}

package com.rajneesh.classenrollmentsystem.test;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * Common assertions to help the junit tasks.
 *
 */
public class AssertionHelper {

	private AssertionHelper() {

	}

	public static ResultActions assertBadRequest(String expectedMessage,
			ResultActions actualResult) throws Exception {
		return actualResult.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
				.andExpect(jsonPath("$.message", is(expectedMessage)));
	}

	public static ResultActions assertOK(ResultActions actualResult)
			throws Exception {
		return actualResult.andExpect(status().isOk());
	}

	public static ResultActions assertCreated(ResultActions actualResult)
			throws Exception {
		return actualResult.andExpect(status().isCreated());
	}

	public static ResultActions assertNotFound(String expectedMessage,
			ResultActions actualResult) throws Exception {
		return actualResult.andExpect(status().isNotFound())
				.andExpect(
						jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
				.andExpect(jsonPath("$.message", is(expectedMessage)));
	}

	public static ResultActions assertMethodNotAllowed(
			ResultActions actualResult) throws Exception {
		return actualResult.andExpect(status().isMethodNotAllowed())
				.andExpect(jsonPath("$.status",
						is(HttpStatus.METHOD_NOT_ALLOWED.name())))
				.andExpect(jsonPath("$.message", is("Method not allowed")));
	}

}

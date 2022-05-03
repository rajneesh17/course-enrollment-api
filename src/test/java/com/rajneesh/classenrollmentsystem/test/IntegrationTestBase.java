package com.rajneesh.classenrollmentsystem.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.ClassRule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * An abstract class to provide the capabilities to run the integration tests on
 * the controllers.
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
public abstract class IntegrationTestBase {
	@Autowired
	protected MockMvc mockMvc;

	protected static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.registerModule(new JavaTimeModule());
	}

	protected ResultActions performPost(String path, Object bodyContent)
			throws Exception, JsonProcessingException {
		return perform(post(path), bodyContent);
	}

	protected ResultActions performPut(String path, Object bodyContent)
			throws Exception, JsonProcessingException {
		return perform(put(path), bodyContent);
	}

	private ResultActions perform(MockHttpServletRequestBuilder requestBuilder,
			Object bodyContent) throws Exception, JsonProcessingException {
		return mockMvc.perform(requestBuilder
				.content(objectMapper.writeValueAsBytes(bodyContent))
				.contentType(MediaType.APPLICATION_JSON));
	}


}

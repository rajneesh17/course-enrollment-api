package com.rajneesh.classenrollmentsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This config enables the open API, which helps to create a specification and
 * initiative for the creation of interface files used to describe, produce,
 * consume, and visualize RESTful web services
 * 
 */
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().components(new Components()).info(new Info().title(
				"Rest API for a class enrollment system for students for a university")
				.description(
						"The users of the sytem consists of both school administrators and students. School administrators will create student identities, and students will be able to enroll themselves into classes before each term."));
	}
}
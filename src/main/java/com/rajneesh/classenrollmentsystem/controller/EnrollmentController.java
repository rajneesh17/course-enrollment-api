package com.rajneesh.classenrollmentsystem.controller;

import com.rajneesh.classenrollmentsystem.domain.modal.EnrollmentRequest;
import com.rajneesh.classenrollmentsystem.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The Enrollment API
 *
 */
@RestController
@Tag(name = "enrollment", description = "the Enrollment API")
public class EnrollmentController {

	@Autowired
	private EnrollmentService enrollmentService;

	/**
	 * API to enroll a student into a class for a particular semester
	 * 
	 * @param enrollmentRequest request
	 * @return object
	 */
	@Operation(summary = "Enroll a student into a class", description = "Enroll a student into a class for a particular semester", tags = {
			"enrollment"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnrollmentRequest.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "404", description = "Student not found")})
	@PostMapping("/enrollments")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> enroll(
			@Parameter(description = "Enrollment Request. Cannot null or empty.", required = true, schema = @Schema(implementation = EnrollmentRequest.class)) @RequestBody EnrollmentRequest enrollmentRequest) {
		return enrollmentService.enroll(enrollmentRequest);

	}

	/**
	 * API to drop a student from a class.
	 * 
	 * @param id student id
	 */
	@Operation(summary = "Drop a student from a class", description = "", tags = {
			"contact"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "404", description = "Student not found")})
	@DeleteMapping("/enrollments")
	@ResponseStatus(HttpStatus.OK)
	public void drop(
			@Parameter(description = "Course name", required = true) @RequestParam(name = "course") String course,
			@Parameter(description = "Student id", required = true) @RequestParam() Long id) {
		 enrollmentService.drop(id, course);
	}
}

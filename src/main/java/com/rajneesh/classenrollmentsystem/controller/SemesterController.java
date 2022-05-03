package com.rajneesh.classenrollmentsystem.controller;

import com.rajneesh.classenrollmentsystem.domain.modal.Semester;
import com.rajneesh.classenrollmentsystem.service.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * The Semester API
 *
 */
@RestController
@Tag(name = "semester", description = "the Semester API")
public class SemesterController {

	@Autowired
	private SemesterService semesterService;

	/**
	 * API to add new semester
	 * 
	 * @param semester request
	 * @return Semester
	 */
	@Operation(summary = "Add a new semester", description = "Add the basic information of semester", tags = {
			"semester"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Semester created", content = @Content(schema = @Schema(implementation = Semester.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input")})
	@PostMapping("/semesters")
	@ResponseStatus(HttpStatus.CREATED)
	public Semester createSemester(
			@Parameter(description = "Semester to add. Cannot null or empty.", required = true, schema = @Schema(implementation = Semester.class)) @RequestBody Semester semester) {
		return semesterService.add(semester);
	}

	/**
	 * API to get semester by name.
	 * 
	 * @param name semester name
	 * @return semester
	 */
	@Operation(summary = "Get semester by name", description = "Returns a single semester", tags = {
			"semester"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Semester.class))),
			@ApiResponse(responseCode = "404", description = "Semester not found")})
	@GetMapping("/semesters/{name}")
	public Semester findSemesterByName(
			@Parameter(description = "Name of the semester to be obtained. Cannot be empty.", required = true) @PathVariable String name) {
		return semesterService.findByName(name);
	}
}

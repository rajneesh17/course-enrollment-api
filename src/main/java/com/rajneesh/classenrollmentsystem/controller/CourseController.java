package com.rajneesh.classenrollmentsystem.controller;

import com.rajneesh.classenrollmentsystem.domain.modal.Course;
import com.rajneesh.classenrollmentsystem.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * The Course API
 */
@RestController
@Tag(name = "course", description = "the Course API")
public class CourseController {

	@Autowired
	private CourseService courseService;

	/**
	 * API to get the list of classes for a particular student for a semester,
	 * or the history of classes enrolled.
	 * 
	 * @return
	 */
	@Operation(summary = "Get the list of classes", description = "Get  the list of classes for a particular student for a semester, or the fully history of classes enrolled.", tags = {
			"course"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Course.class))))})
	@GetMapping("/fetchClasses")
	@ResponseStatus(HttpStatus.OK)
	public Set<Course> fetchedCourses(
			@Parameter(description = "Semester name, it is optional") @RequestParam(required = false) String semester,
			@Parameter(description = "Student id", required = true) @RequestParam(name = "id") Long studentId) {
		return courseService.fetchedCourses(studentId, semester);

	}

	/**
	 * API to add a new class
	 * 
	 * @param course
	 * @return
	 */
	@Operation(summary = "Add a new class", description = "Add the basic information of class", tags = {
			"course"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Course created", content = @Content(schema = @Schema(implementation = Course.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input")})
	@PostMapping("/classes")
	@ResponseStatus(HttpStatus.CREATED)
	public Course createCourse(
			@Parameter(description = "Course to add. Cannot null or empty.", required = true, schema = @Schema(implementation = Course.class)) @RequestBody Course course) {
			return courseService.add(course);

	}

	/**
	 * API to get class by name
	 * 
	 * @param name
	 * @return
	 */
	@Operation(summary = "Get class by name", description = "Returns a single class", tags = {
			"course"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Course.class))),
			@ApiResponse(responseCode = "404", description = "Course not found")})
	@GetMapping("/classes/{name}")
	@ResponseStatus(HttpStatus.OK)
	public Course findClassByName(
			@Parameter(description = "Name of the class to be obtained. Cannot be empty.", required = true) @PathVariable String name) {
		return courseService.findByName(name);
	}

}

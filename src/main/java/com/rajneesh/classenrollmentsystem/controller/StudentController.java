package com.rajneesh.classenrollmentsystem.controller;

import com.rajneesh.classenrollmentsystem.domain.modal.SearchRequest;
import com.rajneesh.classenrollmentsystem.domain.modal.Student;
import com.rajneesh.classenrollmentsystem.domain.modal.StudentRequest;
import com.rajneesh.classenrollmentsystem.service.StudentService;
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
 * The Student API
 */
@RestController
@Tag(name = "student", description = "Student API")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * Fetch bulk record: all students in database API to get the list of
     * students enrolled in a class for a particular semester.
     *
     * @return collect of students
     */
    @Operation(summary = "Get the list of students", description = "Get the list of students enrolled in a class for a particular semester", tags = {
            "student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Student.class))))})
    @GetMapping("/fetchStudents")
    @ResponseStatus(HttpStatus.OK)
    public Set<Student> fetchStudents(
            @Parameter(description = "Course name") @RequestParam(name = "course", required = false) final String course,
            @Parameter(description = "Semester name") @RequestParam(name = "semester", required = false) String semester,
            @Parameter(description = "Page number, default is 0") @RequestParam(defaultValue = "0") Integer pageNo,
            @Parameter(description = "Page size, default is 100") @RequestParam(defaultValue = "100") Integer pageSize,
            @Parameter(description = "Direction, default is ASC") @RequestParam(defaultValue = "ASC") String direction,
            @Parameter(description = "Page number, default is id") @RequestParam(defaultValue = "id") String sortBy) {
        SearchRequest searchRequest = SearchRequest.builder()
                .course(course).semester(semester)
                .pageNo(pageNo).pageSize(pageSize).direction(direction)
                .sortBy(sortBy).build();
        return studentService.fetchStudents(searchRequest);
    }

    /**
     * API to add a new student
     *
     * @param student object
     * @return student
     */
    @Operation(summary = "Add a new student", description = "Add the basic information of a student", tags = {
            "student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created", content = @Content(schema = @Schema(implementation = Student.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "400", description = "Student already exists")})
    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(
            @Parameter(description = "Student to add. Cannot null or empty.", required = true, schema = @Schema(implementation = StudentRequest.class)) @RequestBody StudentRequest student) {
        return studentService.add(student);
    }

    /**
     * API to modify new students
     *
     * @param student object
     * @return student
     */
    @Operation(summary = "Update an existing student", description = "Update the basic information of an existing student", tags = {
            "student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Student not found")})
    @PutMapping("/students")
    @ResponseStatus(HttpStatus.OK)
    public Student updateStudent(
            @Parameter(description = "Student to update. Cannot null or empty.", required = true, schema = @Schema(implementation = StudentRequest.class)) @RequestBody StudentRequest student) {
         return studentService.update(student);
    }

    /**
     * API to get student by id
     *
     * @param id unique id
     * @return student object
     */
    @Operation(summary = "Get student by id", description = "Returns a single student", tags = {
            "student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Student.class))),
            @ApiResponse(responseCode = "404", description = "Student not found")})
    @GetMapping("/students/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Student findStudentById(
            @Parameter(description = "Id of the student to be obtained. Cannot be empty.", required = true) @PathVariable Long id) {
        return studentService.findStudentById(id);
    }

}

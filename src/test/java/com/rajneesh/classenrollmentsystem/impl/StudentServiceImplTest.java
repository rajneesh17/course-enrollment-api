package com.rajneesh.classenrollmentsystem.impl;


import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.SearchRequest;
import com.rajneesh.classenrollmentsystem.domain.modal.Student;
import com.rajneesh.classenrollmentsystem.domain.modal.StudentRequest;
import com.rajneesh.classenrollmentsystem.exception.RequestException;
import com.rajneesh.classenrollmentsystem.exception.StudentInfoNotFoundException;
import com.rajneesh.classenrollmentsystem.mapper.EnrollmentMapperImpl;
import com.rajneesh.classenrollmentsystem.mapper.StudentMapperImpl;
import com.rajneesh.classenrollmentsystem.repository.StudentRepository;
import com.rajneesh.classenrollmentsystem.service.StudentService;
import com.rajneesh.classenrollmentsystem.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.rajneesh.classenrollmentsystem.test.MockedObjects.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Tests all {@link StudentService} service methods.
 * 
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {StudentServiceImpl.class,
		StudentMapperImpl.class, EnrollmentMapperImpl.class})
class StudentServiceImplTest {

	@Autowired
	private StudentService studentService;

	@MockBean
	private StudentRepository studentRepository;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Test fetch students when the result set is empty")
	final void testFetchStudents_withEmptyResult() {
		Set<Student> fetchedStudents = studentService
				.fetchStudents(mockedSearchRequestDto());

		assertTrue(fetchedStudents.isEmpty());

	}

	@Test
	@DisplayName("Test fetch classes when the resultset is not empty")
	final void testFetchStudents_withResult() {
		SearchRequest searchRequest = mockedSearchRequestDto();
		when(studentRepository.fetchStudents(searchRequest))
				.thenReturn(new PageImpl<>(mockedStudents()));

		Set<Student> fetchedStudents = studentService.fetchStudents(searchRequest);

		assertEquals(1, fetchedStudents.size());

	}

	private List<StudentEntity> mockedStudents() {
		return Stream.of(mockedStudent()).collect(Collectors.toList());
	}

	@Test
	@DisplayName("Add a student")
	final void testAdd() {

		when(studentRepository.save(any(StudentEntity.class)))
				.then(i -> i.getArgument(0));

		StudentRequest mockedStudentRequest = mockedStudentRequest();
		Student addedStudent = studentService.add(mockedStudentRequest);

		assertNotNull(addedStudent);
		assertEquals(mockedStudentRequest.getFirstName(),
				addedStudent.getFirstName());
		assertEquals(mockedStudentRequest.getLastName(),
				addedStudent.getLastName());
		assertEquals(mockedStudentRequest.getId(), addedStudent.getId());
		assertEquals(mockedStudentRequest.getNationality(),
				addedStudent.getNationality());
	}

	@Test
	@DisplayName("Add Existing Student")
	final void testAddExistingStudent() {
		when(studentRepository.existsStudentById(1L)).thenReturn(true);
		when(studentRepository.save(any(StudentEntity.class)))
				.then(i -> i.getArgument(0));
		assertThrows(RequestException.class,
				() -> studentService.add(mockedStudentRequest()));
	}

	@Test
	@DisplayName("Update student")
	final void testUpdate() {

		when(studentRepository.existsStudentById(1L)).thenReturn(true);
		when(studentRepository.save(any(StudentEntity.class)))
				.then(i -> i.getArgument(0));

		StudentRequest mockedStudentRequest = mockedStudentRequest();
		mockedStudentRequest.setFirstName("UpdatedName");
		Student updatedStudent = studentService.update(mockedStudentRequest);

		assertNotNull(updatedStudent);
		assertEquals(mockedStudentRequest.getFirstName(),
				updatedStudent.getFirstName());
	}

	@Test
	@DisplayName("Update student when id not found")
	final void testUpdate_whenIdNotFound() {

		when(studentRepository.existsStudentById(1L)).thenReturn(false);

		StudentRequest mockedStudentRequest = mockedStudentRequest();
		mockedStudentRequest.setFirstName("UpdatedName");

		assertThrows(StudentInfoNotFoundException.class,
				() -> studentService.update(mockedStudentRequest));

	}

	@Test
	@DisplayName("Find a student by its id but student info not found")
	final void testFindById_NotFound() {
		assertThrows(StudentInfoNotFoundException.class, () -> {
			studentService.findStudentById(1L);
		});
	}

	@Test
	@DisplayName("Find a student by its id")
	final void testFindById() {
		when(studentRepository.findStudentById(anyLong()))
				.thenReturn(Optional.of(mockedStudent()));

		Student fetchedStudent = studentService.findStudentById(1L);
		assertNotNull(fetchedStudent);
		assertEquals(1L, fetchedStudent.getId());
	}

}

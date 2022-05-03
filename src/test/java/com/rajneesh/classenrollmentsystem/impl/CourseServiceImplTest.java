/**
 * 
 */
package com.rajneesh.classenrollmentsystem.impl;

import com.rajneesh.classenrollmentsystem.domain.entity.CourseEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Course;
import com.rajneesh.classenrollmentsystem.exception.RecordNotFoundException;
import com.rajneesh.classenrollmentsystem.mapper.CourseMapperImpl;
import com.rajneesh.classenrollmentsystem.repository.CourseRepository;
import com.rajneesh.classenrollmentsystem.repository.EnrollmentRepository;
import com.rajneesh.classenrollmentsystem.service.CourseService;
import com.rajneesh.classenrollmentsystem.service.impl.CourseServiceImpl;
import com.rajneesh.classenrollmentsystem.test.MockedObjects;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.rajneesh.classenrollmentsystem.test.MockedObjects.mockedCourseDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Tests all {@link CourseService} service methods.
 * 
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CourseServiceImpl.class, CourseMapperImpl.class})
class CourseServiceImplTest {

	@Autowired
	private CourseService courseService;

	@MockBean
	private EnrollmentRepository enrollmentRepository;

	@MockBean
	private CourseRepository courseClassRepository;

	/**
	 * @throws Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link CourseServiceImpl#fetchedCourses(Long, String)} .
	 */
	@Test
	@DisplayName("Test fetch classes when the resulset is empty")
	final void testFetchClasses_withEmptyResult() {
		Set<Course> fetchedClasses = courseService
				.fetchedCourses(1L, "Summer2022");

		assertTrue(fetchedClasses.isEmpty());

	}

	@Test
	@DisplayName("Test fetch classes when the resultset is not empty")
	final void testFetchClasses_withResult() {
		when(enrollmentRepository.fetchCourses(anyLong(), anyString()))
				.thenReturn(mockedCourse());

		Set<Course> fetchedClasses = courseService
				.fetchedCourses(1L, "Summer2022");

		assertEquals(1, fetchedClasses.size());

	}

	private Set<CourseEntity> mockedCourse() {
		return Stream.of(MockedObjects.mockedCourse()).collect(Collectors.toSet());
	}

	/**
	 * Test method for
	 * {@link CourseServiceImpl#add(Course)}.
	 */
	@Test
	@DisplayName("add a class")
	final void testAdd() {

		when(courseClassRepository.save(any(CourseEntity.class)))
				.then(i -> i.getArgument(0));

		Course mockedCourseClassDto = mockedCourseDto();
		Course addedClass = courseService
				.add(mockedCourseClassDto);

		assertNotNull(addedClass);
		assertEquals(mockedCourseClassDto.getName(), addedClass.getName());
	}

	/**
	 * Test method for
	 * {@link CourseServiceImpl#findByName(String)}.
	 */
	@Test
	@DisplayName("find a class by its name but record not found")
	final void testFindByName_NotFound() {
		assertThrows(RecordNotFoundException.class, () -> {
			courseService.findByName("classA");
		});
	}

	@Test
	@DisplayName("find a class by its name")
	final void testFindByName() {
		when(courseClassRepository.findByName(anyString()))
				.thenReturn(Optional.of(MockedObjects.mockedCourse()));

		String className = "classA";
		Course fetchedClass = courseService.findByName(className);
		assertNotNull(fetchedClass);
		assertEquals(className, fetchedClass.getName());
	}
}

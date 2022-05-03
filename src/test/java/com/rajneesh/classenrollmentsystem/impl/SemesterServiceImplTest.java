package com.rajneesh.classenrollmentsystem.impl;

import com.rajneesh.classenrollmentsystem.domain.entity.SemesterEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Semester;
import com.rajneesh.classenrollmentsystem.exception.RecordNotFoundException;
import com.rajneesh.classenrollmentsystem.mapper.SemesterMapperImpl;
import com.rajneesh.classenrollmentsystem.repository.SemesterRepository;
import com.rajneesh.classenrollmentsystem.service.SemesterService;
import com.rajneesh.classenrollmentsystem.service.impl.SemesterServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.rajneesh.classenrollmentsystem.test.MockedObjects.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests all {@link SemesterService} service methods.
 * 
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SemesterServiceImpl.class,
		SemesterMapperImpl.class})
class SemesterServiceImplTest {

	@Autowired
	private SemesterService semesterService;

	@MockBean
	private SemesterRepository semesterRepository;

	@Test
	@DisplayName("Add a semester")
	final void testAdd() {

		when(semesterRepository.save(any(SemesterEntity.class)))
				.then(i -> i.getArgument(0));

		Semester mockedSemesterDto = mockedSemesterDto();
		Semester addedSemester = semesterService.add(mockedSemesterDto);

		assertNotNull(addedSemester);
		assertEquals(mockedSemesterDto.getName(), addedSemester.getName());
	}

	@Test
	@DisplayName("Find a semester by its name but record not found")
	final void testFindByName_NotFound() {
		assertThrows(RecordNotFoundException.class, () -> {
			semesterService.findByName("classA");
		});
	}

	@Test
	@DisplayName("Find a semester by its name")
	final void testFindByName() {
		when(semesterRepository.findByName(anyString()))
				.thenReturn(Optional.of(mockedSemester()));

		String semester = "Summer2022";
		Semester fetchedClass = semesterService.findByName(semester);
		assertNotNull(fetchedClass);
		assertEquals(semester, fetchedClass.getName());
	}

}

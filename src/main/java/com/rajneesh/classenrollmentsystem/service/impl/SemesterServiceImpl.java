package com.rajneesh.classenrollmentsystem.service.impl;

import com.rajneesh.classenrollmentsystem.domain.entity.SemesterEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.Semester;
import com.rajneesh.classenrollmentsystem.exception.RecordNotFoundException;
import com.rajneesh.classenrollmentsystem.mapper.SemesterMapper;
import com.rajneesh.classenrollmentsystem.repository.SemesterRepository;
import com.rajneesh.classenrollmentsystem.service.SemesterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static java.text.MessageFormat.format;

/**
 * An implementation class for {@link SemesterService}.
 *
 */
@Slf4j
@Service
public class SemesterServiceImpl implements SemesterService {

	@Autowired
	private SemesterRepository semesterRepository;

	@Autowired
	private SemesterMapper semesterMapper;

	@Override
	public Semester add(Semester semester) {
		log.info("Saving the semester - {} into the database",
				semester.getName());
		return toDto(save(semester));
	}

	@CachePut(value = "semesters", key = "#semester.name")
	public SemesterEntity save(Semester semester) {
		return semesterRepository.save(toEntity(semester));
	}

	@Override
	@Cacheable(value = "semesters", key = "#name", unless = "#result==null")
	public SemesterEntity findEntityByName(String name) {
		log.info("Loading the semester - {} from the database.", name);
		return semesterRepository.findByName(name)
				.orElseThrow(() -> new RecordNotFoundException(
						format("Semester {0} not found.", name)));
	}

	@Override
	public Semester findByName(String name) {
		log.info("Loading the semester - {} from the database.", name);
		return toDto(findEntityByName(name));
	}

	private SemesterEntity toEntity(Semester semester) {
		return semesterMapper.toEntity(semester);
	}

	private Semester toDto(SemesterEntity semester) {
		return semesterMapper.toDto(semester);
	}

}

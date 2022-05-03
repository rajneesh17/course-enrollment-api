package com.rajneesh.classenrollmentsystem.service.impl;

import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.SearchRequest;
import com.rajneesh.classenrollmentsystem.domain.modal.Student;
import com.rajneesh.classenrollmentsystem.domain.modal.StudentRequest;
import com.rajneesh.classenrollmentsystem.exception.RequestException;
import com.rajneesh.classenrollmentsystem.exception.StudentInfoNotFoundException;
import com.rajneesh.classenrollmentsystem.mapper.StudentMapper;
import com.rajneesh.classenrollmentsystem.repository.StudentRepository;
import com.rajneesh.classenrollmentsystem.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static java.text.MessageFormat.format;


/**
 * An implementation class for {@link StudentService}.
 */
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentMapper studentMapper;

	@Override
	public Student add(StudentRequest student) {
		if (isExistsById(student)) {
			log.error("Student id {} already exists", student.getId());
			throw new RequestException(format("Student id {0} already exists", student.getId()));
		}
		log.info("Saving the student - {} into the database", student.getId());
		return toDto(studentRepository.save(toEntity(student)));

	}

	@Override
	public Student update(StudentRequest student) {
		if (isExistsById(student)) {
			log.info("Updating the student - {} into the database",
					student.getId());
			return toDto(studentRepository.save(toEntity(student)));
		} else {
			log.error("Student id {} not found", student.getId());
			throw new StudentInfoNotFoundException(student.getId());
		}
	}

	@Override
	public Set<Student> fetchStudents(SearchRequest searchRequest) {
		log.info("Loading the students from the database.");
		return studentMapper
				.toDtos(Optional.ofNullable(studentRepository.fetchStudents(searchRequest)).map(Page::toSet).orElse(Collections.emptySet()));
	}

	@Override
	public Student findStudentById(Long id) {
		StudentEntity fetchedStudent = findOrElseThrowEx(id);
		return toDto(fetchedStudent);

	}

	private StudentEntity toEntity(StudentRequest student) {
		return studentMapper.toEntity(student);
	}

	private Student toDto(StudentEntity entity) {
		return studentMapper.toDto(entity);
	}

	private boolean isExistsById(StudentRequest student) {
		return studentRepository.existsStudentById(student.getId());
	}

	private StudentEntity findOrElseThrowEx(Long id) {
		return studentRepository.findStudentById(id)
				.orElseThrow(() -> new StudentInfoNotFoundException(id));
	}

}

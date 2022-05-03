package com.rajneesh.classenrollmentsystem.repository.impl;

import com.rajneesh.classenrollmentsystem.domain.entity.*;
import com.rajneesh.classenrollmentsystem.domain.modal.SearchRequest;
import com.rajneesh.classenrollmentsystem.repository.StudentQueryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StudentRepositoryImpl implements StudentQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<StudentEntity> fetchStudents(SearchRequest searchRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<StudentEntity> criteriaQuery = cb.createQuery(StudentEntity.class).distinct(true);

        Root<StudentEntity> root = criteriaQuery.from(StudentEntity.class);
        Join<StudentEntity, EnrollmentEntity> enrollments = root.join("enrollments");
        Join<EnrollmentEntity, CourseEnrollmentEntity> courseEnrollments = enrollments.join("courseEnrollments");
        Join<EnrollmentEntity, SemesterEntity> semester = enrollments.join("semester");
        Join<CourseEnrollmentEntity, CourseEntity> course = courseEnrollments.join("course");
        Predicate predicate = getPredicates(searchRequest, cb, semester, course);
        criteriaQuery.where(predicate);
        setOrder(searchRequest, criteriaQuery, root, cb);
        TypedQuery<StudentEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(searchRequest.getPageNo() * searchRequest.getPageSize());
        typedQuery.setMaxResults(searchRequest.getPageSize());
        return new PageImpl<>(typedQuery.getResultList(), getPage(searchRequest), getStudentCount(cb, criteriaQuery, root, predicate));
    }

    private void setOrder(SearchRequest searchRequest, CriteriaQuery<StudentEntity> criteriaQuery, Root<StudentEntity> root, CriteriaBuilder cb) {
        if (Sort.Direction.fromString(searchRequest.getDirection()).equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(cb.asc(root.get(searchRequest.getSortBy())));
        } else {
            criteriaQuery.orderBy(cb.desc(root.get(searchRequest.getSortBy())));
        }
    }

    private Predicate getPredicates(SearchRequest searchRequest, CriteriaBuilder cb, Join<EnrollmentEntity, SemesterEntity> semester, Join<CourseEnrollmentEntity, CourseEntity> course) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(searchRequest.getSemester())) {
            predicates.add(cb.equal(semester.get("name"), searchRequest.getSemester()));
        }
        if (StringUtils.isNotBlank(searchRequest.getCourse())) {
            predicates.add(cb.equal(course.get("name"), searchRequest.getCourse()));
        }
        return cb.and(predicates.toArray(new Predicate[]{}));
    }

    private Pageable getPage(SearchRequest searchRequest) {
        return PageRequest.of(searchRequest.getPageNo(),
                searchRequest.getPageSize(),
                Sort.by(Sort.Direction.fromString(searchRequest.getDirection()),
                        searchRequest.getSortBy()));
    }


    private long getStudentCount(CriteriaBuilder cb, CriteriaQuery<StudentEntity> criteriaQuery, Root<StudentEntity> root, Predicate predicate) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<StudentEntity> countRoot = countQuery.from(criteriaQuery.getResultType());
        doJoins(root.getJoins(), countRoot);
        countQuery.select(cb.countDistinct(countRoot.get("id"))).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }


    private void doJoins(Set<? extends Join<?, ?>> joins, Root<?> root) {
        for (Join<?, ?> join : joins) {
            Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
            joined.alias(join.getAlias());
            doJoins(join.getJoins(), joined);
        }
    }

    private void doJoins(Set<? extends Join<?, ?>> joins, Join<?, ?> root) {
        for (Join<?, ?> join : joins) {
            Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
            joined.alias(join.getAlias());
            doJoins(join.getJoins(), joined);
        }
    }
}

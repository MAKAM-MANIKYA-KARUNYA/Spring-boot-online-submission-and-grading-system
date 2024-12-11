package com.result.management.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.result.management.entities.Result;
import com.result.management.entities.Student;
import com.result.management.entities.Subject;

@Repository
public interface ResultRepository extends CrudRepository<Result, Integer> {

	List<Result> findByStudent(Student student);

	List<Result> findBySubject(Subject subject);

	List<Result> findBySubjectId(int subId);

	Result findBySubjectAndStudent(Subject sub, Student student);

}

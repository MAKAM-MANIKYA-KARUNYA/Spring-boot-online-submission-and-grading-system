package com.result.management.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.result.management.entities.Class;
import com.result.management.entities.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

	public List<Student> findByClass1Id(int i);

	public List<Student> findByClass1AndRollNo(com.result.management.entities.Class class1, int rollNo);

	public Student findByRollNoAndMotherNameAndClass1(int rollNo, String mother, Class class1);
}

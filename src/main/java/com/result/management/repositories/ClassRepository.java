package com.result.management.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.result.management.entities.Teacher;

@Repository
public interface ClassRepository extends CrudRepository<com.result.management.entities.Class, Integer> {

	public List<com.result.management.entities.Class> findByName(String name);

	public List<com.result.management.entities.Class> findByClassTeacher(Teacher teacher);

	public List<com.result.management.entities.Class> findByClassTeacherId(int id);

}

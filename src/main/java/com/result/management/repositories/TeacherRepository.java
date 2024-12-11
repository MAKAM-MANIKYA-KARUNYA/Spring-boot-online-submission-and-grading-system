package com.result.management.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.result.management.entities.Teacher;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {

	public Teacher getTeacherByUsernameOrMobile(String username, String mobile);

	public List<Teacher> findByUsernameAndPassword(String username, String password);

	public List<Teacher> findByMobile(String mobile);

	public Teacher getByUsername(String username);

	public List<Teacher> findByisActive(boolean b);

}

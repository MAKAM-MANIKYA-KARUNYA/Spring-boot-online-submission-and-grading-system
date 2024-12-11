package com.result.management.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.result.management.entities.Subject;

@Repository
public interface SubjectRepo extends CrudRepository<Subject, Integer> {

	public List<Subject> findByName(String name);

}

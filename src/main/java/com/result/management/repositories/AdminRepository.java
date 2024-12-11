package com.result.management.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.result.management.entities.Admin;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {

	public Admin findByPassword(String password);

}

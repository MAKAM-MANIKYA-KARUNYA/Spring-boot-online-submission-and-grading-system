package com.result.management.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.result.management.entities.Subject;
import com.result.management.services.SubjectService;

@RestController
@RequestMapping("/subject/*")
@CrossOrigin("*")
public class SubjectController {
	@Autowired
	SubjectService service;

	// Handles POST Request to add Subject Details
	@PostMapping("/add")
	public int addSubject(@RequestBody Subject subject) {
		return service.addSubject(subject);
	}

	// Handles PUT Request to update Subject details
	@PutMapping("/update")
	public int updateSubject(@RequestBody Subject subject) {
		return service.updateSubject(subject);
	}

	// Handles DELETE Request to delete subject by subject Id
	@DeleteMapping("/delete/{sId}")
	public int deleteSubject(@PathVariable("sId") int sId) {
		return service.deleteSubject(sId);
	}

	// Handles GET Request to get list of all available subjects
	@GetMapping("/getall")
	public List<Subject> getAllSubject() {
		return service.getAllSubjects();
	}

	// Handles GET Request to get Subject by Subject Id
	@GetMapping("/get/{sId}")
	public Subject getSubjectById(@PathVariable("sId") int sId) {
		return service.getSubjectById(sId);
	}

}

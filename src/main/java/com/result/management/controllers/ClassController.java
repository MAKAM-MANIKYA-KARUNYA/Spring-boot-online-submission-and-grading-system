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

import com.result.management.entities.Class;
import com.result.management.services.ClassService;
import com.result.management.utility.SubClassWrapper;

@RestController
@RequestMapping("/class/*")
@CrossOrigin("*")
public class ClassController {
	@Autowired
	ClassService service;

	// Handle POST request to add Class
	@PostMapping("/add")
	public int addSubject(@RequestBody com.result.management.entities.Class class1) {
		return service.addClass(class1);
	}

	// Handle GET request to get Class by classId
	@GetMapping("/get/{id}")
	public Class addSubject(@PathVariable("id") int id) {
		return service.getClass(id);
	}

	// Handle PUT request to update Class
	@PutMapping("/update")
	public int updateSubject(@RequestBody com.result.management.entities.Class class1) {
		return service.updateClass(class1);
	}

	// Handle DELETE Request to delete class by Id
	@DeleteMapping("/delete/{cId}")
	public int deleteSubject(@PathVariable("cId") int cId) {
		return service.deleteClass(cId);
	}

	// Handle Get request to return list of all classes from database
	@GetMapping("/getall")
	public List<com.result.management.entities.Class> getAllClasses() {
		return service.getAllClasses();
	}

	// Handles PUT Request to update Class Teacher of Class
	@PutMapping("/classTeacher")
	public int updateClassTeacher(@RequestBody SubClassWrapper wrapper) {
		return service.updateClassTeacher(wrapper);
	}

	// Handles GET Request to get list of classes for class teacher by teacher ID
	@GetMapping("/forTeacher/{tId}")
	public List<Class> getClassesForClassTeacher(@PathVariable("tId") int teacherId) {
		return service.getClassesForTeacher(teacherId);
	}

	// Handles GET Request to get list of classes by teacher id along with classes
	// for which teacher is yet to be assigned.
	@GetMapping("/getForTeacher/{tId}")
	public List<Class> getClassesForTeacher(@PathVariable("tId") int teacherId) {
		return service.filterClassForTeacher(teacherId);
	}

	// Handles PUT Request to update published column of class to true
	@PutMapping("/publish")
	public int publish(@RequestBody com.result.management.entities.Class clas) {
		return service.publish(clas);

	}

	// Handles PUT Request to update published column of class to true
	@PutMapping("/revert")
	public int revert(@RequestBody com.result.management.entities.Class clas) {
		return service.revert(clas);
	}

}

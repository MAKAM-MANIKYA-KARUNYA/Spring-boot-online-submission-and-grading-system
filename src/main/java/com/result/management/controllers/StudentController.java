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

import com.result.management.entities.Student;
import com.result.management.services.StudentService;

@RestController
@RequestMapping("/student/*")
@CrossOrigin("*")
public class StudentController {

	@Autowired
	StudentService studentService;

	public StudentController() {
	}

	// Handles POST Request to add Student to database
	@PostMapping("/add")
	public int addStudent(@RequestBody Student student) {
		return studentService.addStudent(student);
	}

	// Handles GET Request to Get Student by Student Id
	@GetMapping("/get/{sId}")
	public Student getStudent(@PathVariable("sId") int sId) {
		return studentService.getStudent(sId);
	}

	// Handles DELETE Request to delete student by student Id
	@DeleteMapping("/delete/{sId}")
	public int deleteStudent(@PathVariable("sId") int sId) {
		return studentService.deleteStudent(sId);
	}

	// Handles POST Request to check presence of student in database
	@PostMapping("/check")
	public int checkStudent(@RequestBody Student student) {
		return studentService.checkStudent(student);
	}

	// Handles PUT Request to update student details
	@PutMapping("/update")
	public int updateStudent(@RequestBody Student student) {
		return studentService.updateStudent(student);
	}

	// Handles GET Request to get list of students by class Id
	@GetMapping("/getAllByClass/{cId}")
	public List<Student> getAllByClass(@PathVariable("cId") int classId) {
		return studentService.getStudentsByClass(classId);
	}

	// Handles POST Request to validate and login student
	@PostMapping("/login")
	public int loginStudent(@RequestBody Student student) {
		return studentService.processLogin(student);
	}

	// Handles POST Request to load student object
	@PostMapping("/load")
	public Student loadStudent(@RequestBody Student student) {
		return studentService.load(student);
	}
}

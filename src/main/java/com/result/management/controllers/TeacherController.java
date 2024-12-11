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

import com.result.management.entities.Teacher;
import com.result.management.services.TeacherService;

@RestController
@RequestMapping("/teacher/*")
@CrossOrigin("*")
public class TeacherController {

	@Autowired
	TeacherService teacherService;

	// Handles POST mapping to register Teacher
	@PostMapping("/registration")
	public boolean registerTeacher(@RequestBody Teacher teacher) {
		return teacherService.registerTeacher(teacher);
	}

	// Handles POST mapping to login teacher
	@PostMapping("/login")
	public int loginTeacher(@RequestBody Teacher teacher) {
		return teacherService.loginTeacher(teacher);
	}

	// Handles POST mapping to get teacher by user name and password
	@PostMapping("/getTeacher")
	public Teacher getTeacher(@RequestBody Teacher teacher) {
		return teacherService.getTeacherByDetails(teacher);
	}

	// Handles GET mapping to get teacher by teacher Id
	@GetMapping("/getTeacherById/{tId}")
	public Teacher getTeacherById(@PathVariable("tId") int teacherId) {
		return teacherService.getTeacherById(teacherId);
	}

	// Handles POST Request to check mobile details
	@PostMapping("/checkMobile")
	public Teacher checkMobile(@RequestBody Teacher teacher) {
		return teacherService.checkMobile(teacher);
	}

	// Handles POST Request to reset teacher password
	@PutMapping("/resetPassword")
	public boolean resetPassword(@RequestBody Teacher teacher) {
		return teacherService.resetPassword(teacher);
	}

	// Handles GET request and returns list of all Active teachers
	@GetMapping("/getAllActive")
	public List<Teacher> getActiveTeachers() {
		return teacherService.getAllActive();
	}

	// Handles GET request and returns list of all Inactive teachers
	@GetMapping("/getAllInactive")
	public List<Teacher> getInactiveTeachers() {
		return teacherService.getAllInactive();
	}

	// Handles PUT request to deactivate teacher
	@PutMapping("/deactivate")
	public int deactivateTeacher(@RequestBody Teacher teacher) {
		return teacherService.processDeactivation(teacher);
	}

	// Handles PUT request to deactivate teacher
	@PutMapping("/activate")
	public int activateTeacher(@RequestBody Teacher teacher) {
		return teacherService.processActivation(teacher);
	}

	// Handles DELETE request to delete teacher by teacherId
	@DeleteMapping("/delete/{id}")
	public int deleteTeacher(@PathVariable("id") int teacherId) {
		return teacherService.deleteTeacher(teacherId);
	}
}

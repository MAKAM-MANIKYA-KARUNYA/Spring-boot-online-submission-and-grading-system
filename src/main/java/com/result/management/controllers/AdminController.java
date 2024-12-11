package com.result.management.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.result.management.entities.Admin;
import com.result.management.entities.Class;
import com.result.management.entities.Student;
import com.result.management.entities.Subject;
import com.result.management.entities.Teacher;
import com.result.management.repositories.AdminRepository;
import com.result.management.repositories.ClassRepository;
import com.result.management.repositories.StudentRepository;
import com.result.management.repositories.SubjectRepo;
import com.result.management.repositories.TeacherRepository;

@RestController
@RequestMapping("/admin/*")
@CrossOrigin("*")
public class AdminController {

	@Autowired
	AdminRepository admRepo;
	@Autowired
	TeacherRepository tRepo;
	@Autowired
	SubjectRepo subRepo;
	@Autowired
	StudentRepository stuRepo;
	@Autowired
	ClassRepository clRepo;

	// Process login for Admin by checking its password from database
	@PostMapping("/login")
	public boolean getAdminPassword(@RequestBody Admin admin) {
		Admin admin2 = admRepo.findByPassword(admin.getPassword());
		if (admin2 != null && admin2.getPassword().equals(admin.getPassword())) {
			return true;// Password matched
		}
		return false; // Password not matched
	}

	// Get all analytical data for Analytics section in Admin Role
	@GetMapping("/analytics")
	public List<Integer> analytics() {
		List<Integer> analytics = new ArrayList<>();

		List<Subject> subjects = (List<Subject>) subRepo.findAll();
		List<Class> classes = (List<Class>) clRepo.findAll();
		List<Teacher> teachers = (List<Teacher>) tRepo.findAll();
		List<Student> students = (List<Student>) stuRepo.findAll();

		analytics.add(subjects.size());
		analytics.add(classes.size());
		analytics.add(teachers.size());
		analytics.add(students.size());

		return analytics;
	}
}

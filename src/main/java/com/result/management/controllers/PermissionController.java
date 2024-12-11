package com.result.management.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.result.management.entities.Permission;
import com.result.management.entities.Subject;
import com.result.management.services.PermissionService;
import com.result.management.utility.SubClassWrapper;

@RestController
@RequestMapping("/permission/*")
@CrossOrigin("*")
public class PermissionController {

	@Autowired
	PermissionService service;

	// Handles POST Request to assign subject to multiple classes
	@PostMapping("/assSubClass")
	public int addSubjectToClass(@RequestBody SubClassWrapper wrapper) {
		return service.addSubjectToClass(wrapper);
	}

	// Handles POST Request to assign multiple subjects to classes
	@PostMapping("/addSubToClass")
	public int addSubToClass(@RequestBody SubClassWrapper wrapper) {
		return service.addSubjectToClassAgain(wrapper);
	}

	// Handles POST Request to add new Permission
	@PostMapping("/add")
	public String addTeacherPermission(@RequestBody Permission perm) {
		return service.addTeacherPermission(perm);
	}

	// Handles GET Request to get list of permissions for teacher
	@GetMapping("/getPerForTeacher/{tId}")
	public List<Permission> loadPermissionsForTeacher(@PathVariable("tId") int teacherId) {
		return service.getPermissionsForTeacher(teacherId);
	}

	// Handles DELETE Request to delete teacher from given permission
	@DeleteMapping("/delete/{pId}")
	public int deleteTeacherFromPermission(@PathVariable("pId") int perId) {
		return service.deleteTeacherFromPermission(perId);
	}

	// Handles GET Request to get details of the teacher and subjects by class Id
	@GetMapping("/getTSMap/{cId}")
	public SubClassWrapper getTeacherAndSubjectInfoForClass(@PathVariable("cId") int classId) {
		return service.getTeacherSubjectMap(classId);
	}

	// Handles GET Request to get list of classes to add marks by Teacher Id
	@GetMapping("/getClassesToAddMarks/{tId}")
	public List<com.result.management.entities.Class> getClassesToAddMarks(@PathVariable("tId") int teacherId) {
		return service.getClassesToAddMarks(teacherId);
	}

	// Handles GET Request to get list of subjects to add marks by classId and
	// TeacherId
	@GetMapping("/getSubjectsToAddMarks/{cId}/{teacherId}")
	public List<Subject> getSubjectsToAddMarks(@PathVariable("cId") int classId,
			@PathVariable("teacherId") int teacherId) {
		return service.getSubjectsToaddMarks(classId, teacherId);
	}

	// Handles GET Request to get list of classes for given Subject Id
	@GetMapping("/getBySubId/{cId}")
	public List<com.result.management.entities.Class> getClassForSubjectId(@PathVariable("cId") int cId) {
		return service.getClassForSubjectId(cId);
	}

	// Handles GET Request to get information whether marks are confirmed by teacher
	// or not for given class and subject
	@GetMapping("/getConfirmed/{cId}/{subId}")
	public SubClassWrapper getConfirmed(@PathVariable("cId") int cId, @PathVariable("subId") int subId) {
		return service.getConfirmed(cId, subId);
	}

	// Handles GET Request to get list of permissions by class Id
	@GetMapping("/getByClassId/{cId}")
	public List<Permission> getByClassId(@PathVariable("cId") int cId) {
		return service.getByClassId(cId);
	}

	// Handles GET Request to get list of subjects by classId
	@GetMapping("/subForClass/{cId}")
	public List<Subject> getSubForClass(@PathVariable("cId") int classId) {
		return service.getSubForClass(classId);
	}

}

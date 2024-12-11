package com.result.management.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.result.management.entities.Class;
import com.result.management.entities.Permission;
import com.result.management.entities.Teacher;
import com.result.management.repositories.PermissionRepo;
import com.result.management.repositories.TeacherRepository;

@Service
public class TeacherService {

	@Autowired
	TeacherRepository repo;

	@Autowired
	PermissionRepo permRepo;

	@Autowired
	com.result.management.repositories.ClassRepository classRepo;

////////////////////////////////************************************************///////////////////////////

	/*
	 * Save details of teacher to database
	 */
	public boolean registerTeacher(Teacher teacher) {
		if (checkIfTeacherAlreadyPresent(teacher)) {
			return false;
		} else {
			teacher.setSecAns(teacher.getSecAns().trim());
			repo.save(teacher);
			return true;
		}
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * get teacher by userName and password and if it matches with records then
	 * check if teacher is active
	 */
	public int loginTeacher(Teacher teacher) {
		List<Teacher> teacherList = repo.findByUsernameAndPassword(teacher.getUsername(), teacher.getPassword());
		if (!teacherList.isEmpty()) {
			return teacherList.get(0).isActive() ? 1 : 0;
		} else {
			return -1;
		}

	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * check mobile details matches with records or not
	 */
	public Teacher checkMobile(Teacher teacher) {
		List<Teacher> teacherList = repo.findByMobile(teacher.getMobile());
		return teacherList.isEmpty() ? new Teacher() : teacherList.get(0);
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * update password
	 */
	public boolean resetPassword(Teacher teacher) {
		Teacher teacher2 = repo.getByUsername(teacher.getUsername());
		teacher2.setPassword(teacher.getPassword());
		repo.save(teacher2);
		return true;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * get list of all active teachers
	 */
	public List<Teacher> getAllActive() {
		return repo.findByisActive(true);
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * get list of all inactive teachers
	 */
	public List<Teacher> getAllInactive() {
		return repo.findByisActive(false);

	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Process deActivation of teacher
	 */
	public int processDeactivation(Teacher teacher) {
		List<Permission> permissions = permRepo.findByTeacher(teacher);

		// Set null in teacher column for permissions related with teacher
		for (Permission perm : permissions) {
			perm.setTeacher(null);
			permRepo.save(perm);
		}

		// If he/she is classTeacher then set classTeacher as null to those classes
		List<Class> classes = classRepo.findByClassTeacher(teacher);
		for (Class cl : classes) {
			cl.setClassTeacher(null);
			classRepo.save(cl);
		}

		// Set active to false
		Teacher teacher2 = repo.findById(teacher.getId()).get();
		teacher2.setActive(false);
		repo.save(teacher2);

		return 0;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Make teacher active
	 */
	public int processActivation(Teacher teacher) {
		Optional<Teacher> teacherOpt = repo.findById(teacher.getId());
		Teacher teacher2 = teacherOpt.get();
		teacher2.setActive(true);
		repo.save(teacher2);
		return 0;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * delete teacher by id
	 */
	public int deleteTeacher(int id) {
		repo.deleteById(id);
		return 0;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Get teacher by Username and Password
	 */
	public Teacher getTeacherByDetails(Teacher teacher) {
		return repo.findByUsernameAndPassword(teacher.getUsername(), teacher.getPassword()).get(0);
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Get teacher by Id
	 */
	public Teacher getTeacherById(int teacherId) {
		return repo.findById(teacherId).get();
	}

////////////////////////////////************************************************///////////////////////////

	/*
	 * Check if teacher is present in database already
	 */
	private boolean checkIfTeacherAlreadyPresent(Teacher teacher) {
		Teacher teacher2 = repo.getTeacherByUsernameOrMobile(teacher.getUsername(), teacher.getMobile());
		if (teacher2 != null) {
			return true;
		}
		return false;
	}
}
//******************************************** END OF CLASS *************************************************//

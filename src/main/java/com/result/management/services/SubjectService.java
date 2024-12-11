package com.result.management.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.result.management.entities.Permission;
import com.result.management.entities.Subject;
import com.result.management.repositories.PermissionRepo;
import com.result.management.repositories.SubjectRepo;

@Service
public class SubjectService {

	@Autowired
	SubjectRepo subRepo;

	@Autowired
	ResultService resultService;

	@Autowired
	PermissionRepo permRepo;

////////////////////////////////************************************************///////////////////////////

	/*
	 * Add subject to database if it is not already present
	 */
	public int addSubject(Subject subject) {
		synchronized (this) {
			String name = subject.getName().strip();

			List<Subject> subList = subRepo.findByName(name);
			if (subList.isEmpty()) {
				subject.setName(name);
				subRepo.save(subject);
				resultService.resultPlaceholderWhenSubjectAdded(subject);
				return 1; // Subject Added Successfully
			} else {
				return 0; // Subject Name already present
			}
		}
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Return list of all subjects
	 */
	public List<Subject> getAllSubjects() {
		return (List<Subject>) subRepo.findAll();
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Update subject
	 */
	public int updateSubject(Subject subject) {
		Optional<Subject> subjectStored = subRepo.findById(subject.getId());
		Subject subject2 = subjectStored.get();
		subject2.setName(subject.getName().strip());
		subject2.setMaxMarks(subject.getMaxMarks());
		subRepo.save(subject2);
		return 1;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Delete subject and delete entries from result table belonging to that subject
	 */
	public int deleteSubject(int sId) {

		Subject subject = subRepo.findById(sId).get();
		List<Permission> permissions = permRepo.findBySubjectId(sId);
		permissions.forEach(perm -> permRepo.delete(perm));
		resultService.resultPlaceholderWhenSubjectDeleted(subject);
		subRepo.delete(subject);
		return 1;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Get Subject by ID
	 */
	public Subject getSubjectById(int sId) {
		return subRepo.findById(sId).get();
	}

}
//******************************************** END OF CLASS *************************************************//













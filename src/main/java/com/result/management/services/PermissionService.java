package com.result.management.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.result.management.entities.Class;
import com.result.management.entities.Permission;
import com.result.management.entities.Subject;
import com.result.management.entities.Teacher;
import com.result.management.repositories.ClassRepository;
import com.result.management.repositories.PermissionRepo;
import com.result.management.repositories.TeacherRepository;
import com.result.management.utility.Constants;
import com.result.management.utility.SubClassWrapper;

@Service
public class PermissionService {
	@Autowired
	PermissionRepo repo;

	@Autowired
	ClassRepository classRepo;

	@Autowired
	TeacherRepository teacherRepo;

////////////////////////////////************************************************///////////////////////////

	/*
	 * This method updated permissions for given subject and list of classes
	 */
	public int addSubjectToClass(SubClassWrapper wrapper) {
		synchronized (this) {
			List<Class> classes = wrapper.getClasses();
			List<Subject> subjects = wrapper.getSubjects();

			// Get permissions which already had given subject and set assigned to false all
			List<Permission> alreadyHadPermissions = new ArrayList<Permission>();
			alreadyHadPermissions = repo.findPermissionBySubject(subjects.get(0));
			alreadyHadPermissions.forEach(aper -> aper.setAssigned(false));

			// Add new permissions or update existing permission by setting assigned to true
			// for given subject and list of classes coming from front
			// end
			Subject sub = subjects.get(0);// Though subjects is list here it will contain only one subject
			if (!classes.isEmpty() && !subjects.isEmpty()) {
				for (Class cl : classes) {
					Permission existingPer = repo.findPermissionByclass1AndSubject(cl, sub);
					if (existingPer == null) {
						Permission perm = new Permission();
						perm.setClass1(cl);
						perm.setSubject(sub);
						perm.setAssigned(true);
						repo.save(perm);
					} else {
						existingPer.setAssigned(true);
						repo.save(existingPer);
					}
				}
				return 1; // Class and Subjects are updated in Permissions
			} else {
				return 0; // No Class and Subject Selected
			}
		}
	}

////////////////////////////////************************************************///////////////////////////

	/*
	 * This method updated permissions table for given class and list of subjects
	 */
	public int addSubjectToClassAgain(SubClassWrapper wrapper) {
		synchronized (this) {
			Class class1 = wrapper.getClasses().get(0);
			List<Subject> subjects = wrapper.getSubjects();

			// Get permissions which already had given class and delete all
			List<Permission> alreadyHadPermissions = new ArrayList<Permission>();
			alreadyHadPermissions = repo.findPermissionByclass1Id(class1.getId());
			alreadyHadPermissions.forEach(aper -> aper.setAssigned(false));

			// Add new permissions for given class and list of subjects
			for (Subject sub : subjects) {
				Permission existingPer = repo.findPermissionByclass1AndSubject(class1, sub);
				if (existingPer == null) {
					Permission perm = new Permission();
					perm.setClass1(class1);
					perm.setSubject(sub);
					perm.setAssigned(true);
					repo.save(perm);
				} else {
					existingPer.setAssigned(true);
					repo.save(existingPer);
				}
			}
			return 0;
		}

	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Add given teacher to permission based on subject and class If same teacher is
	 * already added return ALREADY as a String If Other teacher is already added
	 * then return name of that teacher and if no teacher present already then add
	 * that teacher to permission and then return SUCCESS as a String
	 */
	public String addTeacherPermission(Permission perm) {

		Permission permission = repo.findPermissionByclass1AndSubjectAndAssigned(perm.getClass1(), perm.getSubject(),
				true);

		if (permission != null && permission.getTeacher() != null) {
			if (permission.getTeacher().getId() == perm.getTeacher().getId()) {
				return Constants.ALREADY; // same teacher already assigned
			}
			return permission.getTeacher().getName(); // other teacher already assigned
		} else {
			permission.setTeacher(perm.getTeacher());
			repo.save(permission);
			return Constants.SUCCESS; // Teacher Assigned successfully
		}
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * This returns list of permissions for given teacher id
	 */
	public List<Permission> getPermissionsForTeacher(int teacherId) {
		List<Permission> permissions = repo.findByTeacherIdAndAssigned(teacherId, true);
		return permissions.stream().sorted().collect(Collectors.toList());
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * This will set Teacher as null for given permission
	 */
	public int deleteTeacherFromPermission(int perId) {
		Permission permission = repo.findById(perId).get();
		permission.setTeacher(null);
		repo.save(permission);
		return 0;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * This method will return wrapper class DTO containing two lists of string. one
	 * contains name of teachers and other contains name of subjects
	 */
	public SubClassWrapper getTeacherSubjectMap(int classId) {
		Class class1 = classRepo.findById(classId).get();

		List<String> subNameList = new ArrayList<>();
		List<String> teacherNameList = new ArrayList<>();

		// Add Strings of subject name and class teacher
		if (class1.getClassTeacher() != null) {
			List<Permission> permissionList = repo.findPermissionByClass1AndTeacherAndAssigned(class1,
					class1.getClassTeacher(), true);
			String subjString = new String();
			subjString = getsubList(permissionList, subjString);
			teacherNameList.add(class1.getClassTeacher().getName() + "  [CLASS TEACHER] ");
			subNameList.add(subjString);
		}

		Set<Teacher> teacherSet = new HashSet<Teacher>();
		List<Teacher> teacherList = new ArrayList<>();

		// get set of teachers for given class
		List<Permission> permissionByclass1 = repo.findPermissionByclass1IdAndAssigned(class1.getId(), true);
		if (permissionByclass1 != null) {
			teacherSet = permissionByclass1.stream().map(perm -> perm.getTeacher()).collect(Collectors.toSet());
		}

		// exclude classTeacher from given set
		if (teacherSet != null && class1.getClassTeacher() != null) {
			teacherSet = teacherSet.stream().filter(t -> t != null && t.getId() != class1.getClassTeacher().getId())
					.collect(Collectors.toSet());
		}

		// Convert set to list of teachers
		teacherList.addAll(teacherSet);

		// for all teachers excluding classTeacher prepare list of subject names
		for (Teacher t : teacherList) {
			if (t != null) {
				List<Permission> permissionList2 = repo.findPermissionByClass1AndTeacherAndAssigned(class1, t, true);
				permissionList2 = permissionList2.stream().filter(p -> p.isAssigned()).collect(Collectors.toList());
				String subjString = new String();

				// prepare comma separated string of subject names for all teachers of
				// permissions
				subjString = getsubList(permissionList2, subjString);
				teacherNameList.add(t.getName());
				subNameList.add(subjString);
			}
		}

		// set prepared list of teacher names and belonging subject names for given
		// class in DTO and return it
		SubClassWrapper wrapper = new SubClassWrapper();
		wrapper.setSubjectNames(subNameList);
		wrapper.setTeacherNames(teacherNameList);
		return wrapper;
	}

////////////////////////////////************************************************///////////////////////////

	/*
	 * returns sorted list of classes for given teacher id for which that teacher
	 * have permission to add marks
	 */
	public List<Class> getClassesToAddMarks(int teacherId) {
		List<Class> classList = new ArrayList<>();
		List<Permission> permissions = repo.findByTeacherIdAndAssigned(teacherId, true);
		permissions = permissions.stream().filter(p -> p.isAssigned()).collect(Collectors.toList());
		Set<Class> classes = permissions.stream().map(perm -> perm.getClass1()).collect(Collectors.toSet());
		classList.addAll(classes);
		return classList.stream().sorted().collect(Collectors.toList());

	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * returns list of subjects for given teacher id and classId for which that
	 * teacher have permission to add marks
	 */
	public List<Subject> getSubjectsToaddMarks(int cId, int teacherId) {
		List<Permission> permissions = repo.findPermissionByclass1IdAndTeacherIdAndAssigned(cId, teacherId, true);
		return permissions.stream().map(perm -> perm.getSubject()).collect(Collectors.toList());

	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * returns list of classes from permission for given subject Id
	 */
	public List<Class> getClassForSubjectId(int subId) {
		List<Permission> permissions = repo.findBySubjectIdAndAssigned(subId, true);
		return permissions.stream().map(perm -> perm.getClass1()).collect(Collectors.toList());
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * returns true or false, whether teacher have added marks for given class and
	 * subjects and marked it as confirmed or not
	 */
	public SubClassWrapper getConfirmed(int cId, int subId) {
		Permission permission = repo.findByClass1IdAndSubjectIdAndAssigned(cId, subId, true);
		SubClassWrapper wrapper = new SubClassWrapper();
		wrapper.setConfirmed(permission.isConfirmed());
		return wrapper;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Simply returns list of permissions for given classId
	 */
	public List<Permission> getByClassId(int cId) {
		List<Permission> permissionList = repo.findPermissionByclass1IdAndAssigned(cId, true);
		permissionList.forEach(perm -> {
			if (perm.getTeacher() == null) {
				perm.setTeacher(new Teacher());
			}
		});
		return permissionList;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * returns list of subjects from permission for given class Id
	 */
	public List<Subject> getSubForClass(int classId) {

		List<Permission> permissions = repo.findPermissionByclass1IdAndAssigned(classId, true);

		List<Subject> subjectList = permissions.stream().map(p -> p.getSubject()).sorted().collect(Collectors.toList());

		return subjectList;
	}

//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*/

	/*
	 * This is private method used internally in class to prepare list of comma
	 * separated subject names for given permissions
	 */
	private String getsubList(List<Permission> permissionList, String subjString) {
		for (int i = 0; i < permissionList.size(); i++) {
			String subjName = permissionList.get(i).getSubject().getName();
			if (i == permissionList.size() - 1) {
				subjString += subjName;
			} else {
				subjString += subjName + ", ";
			}
		}
		return subjString;
	}
}
//******************************************** END OF CLASS *************************************************//

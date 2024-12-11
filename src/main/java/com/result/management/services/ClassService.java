package com.result.management.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.result.management.entities.Class;
import com.result.management.entities.Permission;
import com.result.management.entities.Result;
import com.result.management.entities.Student;
import com.result.management.entities.Subject;
import com.result.management.entities.Teacher;
import com.result.management.repositories.ClassRepository;
import com.result.management.repositories.PermissionRepo;
import com.result.management.repositories.ResultRepository;
import com.result.management.repositories.StudentRepository;
import com.result.management.utility.SubClassWrapper;

@Service
@PropertySource("application.properties")
public class ClassService {

	@Autowired
	private Environment env;

	@Autowired
	ClassRepository classRepo;

	@Autowired
	PermissionRepo permRepo;

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	StudentService studentService;

	@Autowired
	ResultRepository resRepo;

///////////////////////////////////*************************************/////////////////////////////////

	/*
	 * This method takes class as input parameter coming from front end Checks if
	 * the given class name is already present in database If it is present then
	 * return 0 otherwise add it to database and return 1
	 */
	public int addClass(com.result.management.entities.Class class1) {

		// Get class name from incoming class1 parameter and remove staring and ending
		// spaces
		String name1 = class1.getName().strip();

		// check if same name is already present in existing class names. If not
		// present, then add the new class in database with stripped class name and
		// return 0 else return 1
		if (checkIfClassAlreadyPresent(class1)) {
			return 0; // Class Name already present
		} else {
			class1.setName(name1);
			classRepo.save(class1);
			return 1; // Class Added Successfully
		}
	}

///////////////////////////////////*************************************/////////////////////////////////

	/*
	 * This method simply returns list of all classes by sorting those by names
	 */
	public List<Class> getAllClasses() {
		List<Class> classes = (List<Class>) classRepo.findAll();
		return classes.stream().sorted().collect(Collectors.toList());
	}

///////////////////////////////////*************************************/////////////////////////////////

	/*
	 * This method updates class name by checking if there is any class present
	 * already with similar name If no such class already present then update it
	 * with given name and return 1 otherwise return 0
	 */
	public int updateClass(com.result.management.entities.Class class1) {
		Class class2 = classRepo.findById(class1.getId()).get();
		String name1 = class1.getName().strip();

		if (checkIfClassAlreadyPresent(class1)) {
			return 0; // Class Name already present
		} else {
			class2.setName(name1);
			classRepo.save(class2);
			return 1; // Class Updated Successfully
		}
	}

///////////////////////////////////*************************************/////////////////////////////////

	/*
	 * This method deletes class from database along with permissions and students
	 * belong to this class
	 */
	public int deleteClass(int cId) {

		// Delete all permissions related to this Class
		List<Permission> permissionsByClass = permRepo.findPermissionByclass1Id(cId);
		permissionsByClass.forEach(perm -> permRepo.delete(perm));

		// Delete all Students related to this Class
		List<Student> studentsOfClass = studentRepo.findByClass1Id(cId);
		studentsOfClass.forEach(stu -> studentService.deleteStudent(stu.getId()));

		// Delete Class for given class Id and return 1
		classRepo.deleteById(cId);
		return 1;
	}
///////////////////////////////////*************************************/////////////////////////////////

	/*
	 * This method updates class teacher for given classes coming as input parameter
	 * in wrapper class DTO and returns 0;
	 */
	public int updateClassTeacher(SubClassWrapper wrapper) {
		List<Class> classes = wrapper.getClasses();
		Teacher teacher = wrapper.getTeacher();

		// First set class teacher to null for all classes of that teacher
		List<Class> classesLocal = classRepo.findByClassTeacher(teacher);
		classesLocal.forEach(cl -> cl.setClassTeacher(null));
		classesLocal.forEach(cl -> classRepo.save(cl));

		// Set given classTeacher to given list of classes coming from front end
		List<Class> classesFromDb = classes.stream().map(ob -> classRepo.findById(ob.getId()).get())
				.collect(Collectors.toList());
		classesFromDb.forEach(cla -> cla.setClassTeacher(teacher));
		classesFromDb.forEach(cl -> classRepo.save(cl));

		return 0;

	}
///////////////////////////////////*************************************/////////////////////////////////

	/*
	 * This method simply returns sorted list of classes for given classTeacher
	 */
	public List<Class> getClassesForTeacher(int teacherId) {
		return classRepo.findByClassTeacherId(teacherId).stream().sorted().collect(Collectors.toList());
	}
///////////////////////////////////*************************************/////////////////////////////////

	/*
	 * This method returns list of classes for given class teacher along with
	 * classes for which there is no class teacher assigned
	 */
	public List<Class> filterClassForTeacher(int teacherId) {
		List<Class> classesWithNull = classRepo.findByClassTeacher(null);
		List<Class> classesForTeacher = classRepo.findByClassTeacherId(teacherId);
		classesForTeacher.addAll(classesWithNull);

		return classesForTeacher.stream().sorted().collect(Collectors.toList());
	}
///////////////////////////////////*************************************/////////////////////////////////

	/*
	 * Get Class by Class Id
	 */
	public Class getClass(int id) {
		return classRepo.findById(id).get();
	}
///////////////////////////////////*************************************/////////////////////////////////

	/*
	 * This method sets published value of given class to true and also sets passed
	 * to true or false on result table for all students of given class based on
	 * their marks in all subjects
	 */
	public int publish(Class clas) {
		// Get Passing percentage from application.properties file
		String per = env.getProperty("passPercent");
		Integer percent = Integer.valueOf(per);

		// Set Published to True for given Class
		Class cl = classRepo.findById(clas.getId()).get();
		cl.setPublished(true);
		classRepo.save(cl);

		// Get all subjects from permissions for given Class
		List<Permission> permissions = permRepo.findPermissionByclass1IdAndAssigned(clas.getId(), true);
		List<Subject> subjects = permissions.stream().map(perm -> perm.getSubject()).collect(Collectors.toList());

		// Get all students from for given class
		List<Student> students = studentRepo.findByClass1Id(cl.getId());

		// For all students and subjects get result and if for any subject marks are
		// less than passing percentage then set passed as false in result and save the
		// result
		for (Student student : students) {
			boolean passed = true;

			for (Subject sub : subjects) {
				Result res = resRepo.findBySubjectAndStudent(sub, student);
				float actPer = Math.round(res.getMarks() * 100 / sub.getMaxMarks());
				if (actPer < percent) {
					passed = false;
					break;
				}
			}

			student.setPassed(passed);
			studentRepo.save(student);
		}
		return 0;
	}
///////////////////////////////////*************************************/////////////////////////////////

	/*
	 * This method set published value back to False
	 */
	public int revert(Class clas) {
		Class cl = classRepo.findById(clas.getId()).get();
		cl.setPublished(false);
		classRepo.save(cl);
		return 0;
	}
//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*//*/

	/*
	 * This is private method which is used as utility in this class only It checks
	 * if given class already present in database return true if present else return
	 * false
	 */
	private boolean checkIfClassAlreadyPresent(com.result.management.entities.Class class1) {
		// remove all spaces from name to compare it with existing class names without
		// considering spaces
		String name = class1.getName().replace(" ", "");
		// get list of all existing classes
		List<Class> classList = (List<Class>) classRepo.findAll();
		// remove spaces from all existing class names
		List<String> collect = classList.stream().map(cln -> cln.getName().replace(" ", ""))
				.collect(Collectors.toList());
		// return true if similar class name present else return false
		return collect.contains(name);
	}

}
//****************************************** END OF CLASS ************************************************//

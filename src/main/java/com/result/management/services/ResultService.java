package com.result.management.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.result.management.entities.Permission;
import com.result.management.entities.Result;
import com.result.management.entities.Student;
import com.result.management.entities.Subject;
import com.result.management.repositories.PermissionRepo;
import com.result.management.repositories.ResultRepository;
import com.result.management.repositories.StudentRepository;
import com.result.management.repositories.SubjectRepo;
import com.result.management.utility.Constants;
import com.result.management.utility.SubClassWrapper;

@Service
public class ResultService {

	@Autowired
	private Environment env;

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	SubjectRepo subRepo;

	@Autowired
	ResultRepository resultRepo;

	@Autowired
	PermissionRepo permRepo;

////////////////////////////////************************************************///////////////////////////

	/*
	 * Create entries in result table when subject is added
	 */
	public void resultPlaceholderWhenSubjectAdded(Subject subject) {
		List<Result> results = new ArrayList<>();

		Iterable<Student> students = studentRepo.findAll();
		for (Student stu : students) {
			Result res = new Result();
			res.setStudent(stu);
			res.setSubject(subject);
			results.add(res);
		}
		resultRepo.saveAll(results);
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Delete entries in result table when subject is deleted
	 */
	public void resultPlaceholderWhenSubjectDeleted(Subject subject) {
		resultRepo.deleteAll(resultRepo.findBySubject(subject));
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Delete entries in result table when Student is added
	 */
	public void resultPlaceholderWhenStudentAdded(Student student) {
		List<Result> results = new ArrayList<>();
		Iterable<Subject> subjects = subRepo.findAll();
		for (Subject sub : subjects) {
			Result res = new Result();
			res.setStudent(student);
			res.setSubject(sub);
			results.add(res);
		}
		resultRepo.saveAll(results);
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Delete entries in result table when student is deleted
	 */
	public void resultPlaceholderWhenStudentDeleted(Student student) {

		resultRepo.deleteAll(resultRepo.findByStudent(student));

	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * returns list of result based on class id and subject Id
	 */
	public List<Result> getForClassSubject(int cId, int subId) {

		// Get list of students for given class
		List<Student> studentsOfClass = studentRepo.findByClass1Id(cId);

		// Get list of results by subject id
		List<Result> resultsBySubject = resultRepo.findBySubjectId(subId);

		// Filter and sort all those results only for students of given class
		List<Result> sortedResultsByClassAndSubject = resultsBySubject.stream()
				.filter(res -> studentsOfClass.contains(res.getStudent())).sorted().collect(Collectors.toList());

		return sortedResultsByClassAndSubject;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Save the marks to results coming from front end and also mark confirmed as
	 * true or false based on checkBox selected by teacher
	 */
	public int saveResult(SubClassWrapper wrapper) {

		// Save results
		List<Result> results = wrapper.getResults();
		for (Result res : results) {
			Result result = resultRepo.findById(res.getId()).get();
			result.setMarks(res.getMarks());
			resultRepo.save(result);
		}

		// set IsConfirmed in Permission
		Permission classCon = permRepo.findByClass1IdAndSubjectIdAndAssigned(wrapper.getClasses().get(0).getId(),
				wrapper.getSubjects().get(0).getId(),true);
		classCon.setConfirmed(wrapper.isConfirmed());
		permRepo.save(classCon);
		return 0;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * This method returns all results for given class Id
	 */
	public SubClassWrapper getResultForClass(int classId) {

		SubClassWrapper wrapper = new SubClassWrapper();
		List<List<Integer>> finalResultForSem = new ArrayList<>();

		// Get list of subjects assigned to class
		List<Permission> permissions = permRepo.findPermissionByclass1IdAndAssigned(classId, true);
		List<Subject> subjects = permissions.stream().map(perm -> perm.getSubject()).sorted()
				.collect(Collectors.toList());

		// Get list of students of that class
		List<Student> studentsOfClass = studentRepo.findByClass1Id(classId).stream().sorted()
				.collect(Collectors.toList());

		// For each student and subjects prepare list of marks
		for (Student student : studentsOfClass) {
			List<Integer> marksOfOneStudent = new ArrayList<>();

			for (Subject sub : subjects) {
				Permission perm = permRepo.findByClass1IdAndSubjectIdAndAssigned(classId, sub.getId(),true);

				Result result = resultRepo.findBySubjectAndStudent(sub, student);
				marksOfOneStudent.add(perm.isConfirmed() ? result.getMarks() : 0);
			}

			finalResultForSem.add(marksOfOneStudent);
		}

		// set list of students, marks and results in wrapper class for data transfer
		wrapper.setResultOfClassForSem(finalResultForSem);
		wrapper.setSubjects(subjects);
		wrapper.setStudentList(studentsOfClass);

		return wrapper;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Get all subjects marks for given student
	 */
	public SubClassWrapper getForStudent(int stId) {

		int sumActual = 0;
		int sumTotal = 0;
		String perS = env.getProperty("passPercent");
		Integer percent = Integer.valueOf(perS);

		// Get list of subjects assigned to class from which given student belongs
		Student student = studentRepo.findById(stId).get();
		List<Permission> permissions = permRepo.findPermissionByclass1IdAndAssigned(student.getClass1().getId(),true);
		List<Subject> sortedSubjects = permissions.stream().map(per -> per.getSubject()).sorted()
				.collect(Collectors.toList());

		List<String> resList = new ArrayList<>();
		List<Integer> markList = new ArrayList<Integer>();

		// Prepare list of marks for each subject and also list of result as pass or
		// fail
		for (Subject sub : sortedSubjects) {
			Result result = resultRepo.findBySubjectAndStudent(sub, student);
			markList.add(result.getMarks());

			sumActual += result.getMarks();
			sumTotal += sub.getMaxMarks();
			int per = Math.round(result.getMarks() * 100 / sub.getMaxMarks());
			if (per < percent) {
				resList.add(Constants.FAIL);
			} else {
				resList.add(Constants.PASS);
			}

		}

		// append mark list with sum of actual and total marks
		markList.add(sumActual);
		markList.add(sumTotal);

		// Set all lists to wrapper class and then return it
		SubClassWrapper wrapper = new SubClassWrapper();
		wrapper.setMarkList(markList);
		wrapper.setSubjects(sortedSubjects);
		wrapper.setSubjectNames(resList);

		return wrapper;
	}
}
//******************************************** END OF CLASS *************************************************//

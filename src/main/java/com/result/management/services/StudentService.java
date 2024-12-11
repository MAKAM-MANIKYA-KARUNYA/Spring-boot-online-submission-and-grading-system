package com.result.management.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.result.management.entities.Class;
import com.result.management.entities.Student;
import com.result.management.repositories.StudentRepository;

@Service
public class StudentService {

	@Autowired
	StudentRepository repo;

	@Autowired
	ResultService resultService;

////////////////////////////////************************************************///////////////////////////

	/*
	 * Add given student object to database by stripping name and mother name and
	 * return 0 if success else 1
	 */
	public int addStudent(Student student) {
		synchronized (this) {

			if (0 == checkStudent(student)) {
				student.setFullName(student.getFullName().strip());
				student.setMotherName(student.getMotherName().strip());
				repo.save(student);
				resultService.resultPlaceholderWhenStudentAdded(student);
				return 0;
			} else {
				return -1;
			}
		}
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Update given student
	 */
	public int updateStudent(Student student) {
		Student student2 = repo.findByClass1AndRollNo(student.getClass1(), student.getRollNo()).get(0);
		student2.setFullName(student.getFullName().strip());
		student2.setMotherName(student.getMotherName().strip());
		repo.save(student2);
		return 0;

	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Get list of students by Class Id
	 */
	public List<Student> getStudentsByClass(int classId) {
		List<Student> students = repo.findByClass1Id(classId);
		return students.stream().sorted().collect(Collectors.toList());
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Check if student with same roll number is already present in class if yes
	 * return -1 or else return 0
	 */
	public int checkStudent(Student student) {

		List<Student> studentL = repo.findByClass1AndRollNo(student.getClass1(), student.getRollNo());
		if (!studentL.isEmpty()) {
			return -1;// Student already present for the class
		}
		return 0;// Student not present
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Get student by id
	 */
	public Student getStudent(int sId) {
		return repo.findById(sId).get();
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * Delete Student by Id and also delete results related to student from database
	 */
	public int deleteStudent(int sId) {
		try {
			Student student = repo.findById(sId).get();
			resultService.resultPlaceholderWhenStudentDeleted(student);
			repo.delete(student);
		} catch (Exception e) {
			return -1;
		}
		return 0;
	}
////////////////////////////////************************************************///////////////////////////

	/*
	 * First check result is published or not for given class if no return 2 and if
	 * yes then check student credentials and return 0 for valid credentials else
	 * return 1
	 */
	public int processLogin(Student student) {

		Class class1 = student.getClass1();
		if (!class1.isPublished()) {
			return 2;// Result is not yet published for this Class
		} else {
			Student student2 = repo.findByRollNoAndMotherNameAndClass1(student.getRollNo(),
					student.getMotherName().strip(), class1);
			if (student2 != null) {
				return 0;// Success
			} else {
				return 1; // Invalid Credentials
			}
		}
	}
////////////////////////////////************************************************///////////////////////////

	/**
	 * Return Student object with raw roll number and mother name
	 * 
	 * @param student
	 * @return
	 */
	public Student load(Student student) {

		Class class1 = student.getClass1();

		return repo.findByRollNoAndMotherNameAndClass1(student.getRollNo(), student.getMotherName().strip(), class1);

	}
}

//******************************************** END OF CLASS *************************************************//
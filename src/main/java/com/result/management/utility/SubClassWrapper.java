package com.result.management.utility;

import java.util.List;

import com.result.management.entities.Result;
import com.result.management.entities.Student;
import com.result.management.entities.Subject;
import com.result.management.entities.Teacher;

public class SubClassWrapper {

	private List<com.result.management.entities.Class> classes;
	private List<Subject> subjects;
	private List<Result> results;
	private List<List<Integer>> resultOfClassForSem;
	private List<Student> studentList;
	private boolean confirmed;
	private List<Integer> markList;
	private List<Integer> totalMarks;

	// Getter Setter
	public List<Integer> getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(List<Integer> totalMarks) {
		this.totalMarks = totalMarks;
	}

	public List<Integer> getMarkList() {
		return markList;
	}

	public void setMarkList(List<Integer> markList) {
		this.markList = markList;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public List<List<Integer>> getResultOfClassForSem() {
		return resultOfClassForSem;
	}

	public void setResultOfClassForSem(List<List<Integer>> resultOfClassForSem) {
		this.resultOfClassForSem = resultOfClassForSem;
	}

	public Teacher teacher;

	public List<String> teacherNames;
	public List<String> subjectNames;

	public List<String> getTeacherNames() {
		return teacherNames;
	}

	public void setTeacherNames(List<String> teacherNames) {
		this.teacherNames = teacherNames;
	}

	public List<String> getSubjectNames() {
		return subjectNames;
	}

	public void setSubjectNames(List<String> subjectNames) {
		this.subjectNames = subjectNames;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public List<com.result.management.entities.Class> getClasses() {
		return classes;
	}

	public void setClasses(List<com.result.management.entities.Class> classes) {
		this.classes = classes;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

}

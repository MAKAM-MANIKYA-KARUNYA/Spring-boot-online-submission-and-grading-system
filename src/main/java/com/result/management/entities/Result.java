package com.result.management.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Result implements Comparable<Result> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	private Student student;
	@ManyToOne
	private Subject subject;
	private int marks;

	// Getter Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}

	@Override
	public int compareTo(Result result) {
		if (this.student.getRollNo() > result.getStudent().getRollNo()) {
			return 1;
		} else if (this.student.getRollNo() < result.getStudent().getRollNo()) {
			return -1;
		} else {
			return 0;
		}
	}

}

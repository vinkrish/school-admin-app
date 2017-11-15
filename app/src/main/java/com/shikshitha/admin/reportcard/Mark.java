package com.shikshitha.admin.reportcard;

public class Mark {
	private long id;
	private long examId;
	private long subjectId;
	private long sectionId;
	private long studentId;
	private float mark;
	private String grade;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getExamId() {
		return examId;
	}

	public void setExamId(long examId) {
		this.examId = examId;
	}

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public long getSectionId() {
		return sectionId;
	}

	public void setSectionId(long sectionId) {
		this.sectionId = sectionId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public float getMark() {
		return mark;
	}

	public void setMark(float mark) {
		this.mark = mark;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

}

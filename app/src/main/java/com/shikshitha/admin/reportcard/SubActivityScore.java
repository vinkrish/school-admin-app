package com.shikshitha.admin.reportcard;

public class SubActivityScore {
	private long id;
	private long subActivityId;
	private long studentId;
	private float mark;
	private String grade;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSubActivityId() {
		return subActivityId;
	}

	public void setSubActivityId(long subActivityId) {
		this.subActivityId = subActivityId;
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

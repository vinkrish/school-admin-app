package com.shikshitha.admin.reportcard;

public class ActivityScore {
	private long id;
	private long activityId;
	private long studentId;
	private float mark;
	private String grade;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
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

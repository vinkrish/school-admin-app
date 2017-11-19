package com.shikshitha.admin.reportcard;

public class Activity {
	private long id;
	private long sectionId;
	private long examId;
	private long subjectId;
	private String activityName;
	private String type;
	private float maximumMark;
	private float weightage;
	private int calculation;
	private float activityAvg;
	private int orders;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSectionId() {
		return sectionId;
	}

	public void setSectionId(long sectionId) {
		this.sectionId = sectionId;
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

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public float getMaximumMark() {
		return maximumMark;
	}

	public void setMaximumMark(float maximumMark) {
		this.maximumMark = maximumMark;
	}

	public float getWeightage() {
		return weightage;
	}

	public void setWeightage(float weightage) {
		this.weightage = weightage;
	}

	public int getCalculation() {
		return calculation;
	}

	public void setCalculation(int calculation) {
		this.calculation = calculation;
	}

	public float getActivityAvg() {
		return activityAvg;
	}

	public void setActivityAvg(float activityAvg) {
		this.activityAvg = activityAvg;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return activityName;
	}
}

package com.shikshitha.admin.reportcard;

public class ExamSubject {
	private long id;
	private long examId;
	private long subjectId;
	private String subjectName;
	private String type;
	private float maximumMark;
	private float failMark;
	private int calculation;
	private float percentage;
	private int orders;

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

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public float getMaximumMark() {
		return maximumMark;
	}

	public void setMaximumMark(float maximumMark) {
		this.maximumMark = maximumMark;
	}

	public float getFailMark() {
		return failMark;
	}

	public void setFailMark(float failMark) {
		this.failMark = failMark;
	}

	public int getCalculation() {
		return calculation;
	}

	public void setCalculation(int calculation) {
		this.calculation = calculation;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
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

}

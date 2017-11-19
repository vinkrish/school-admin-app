package com.shikshitha.admin.reportcard;

public class Exam {
	private long id;
	private String examName;
	private long classId;
	private int term;
	private String type;
	private int calculation;
	private float percentage;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String toString() {
		return examName;
	}
}

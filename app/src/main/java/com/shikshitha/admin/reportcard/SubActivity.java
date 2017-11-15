package com.shikshitha.admin.reportcard;

public class SubActivity {
	private long id;
	private long activityId;
	private String subActivityName;
	private String type;
	private float maximumMark;
	private float weightage;
	private float subActivityAvg;
	private int orders;

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

	public String getSubActivityName() {
		return subActivityName;
	}

	public void setSubActivityName(String subActivityName) {
		this.subActivityName = subActivityName;
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

	public float getSubActivityAvg() {
		return subActivityAvg;
	}

	public void setSubActivityAvg(float subActivityAvg) {
		this.subActivityAvg = subActivityAvg;
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
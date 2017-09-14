package com.shikshitha.admin.model;

import java.io.Serializable;

/**
 * Created by Vinay on 30-07-2017.
 */

public class Evnt implements Serializable {
    private int id;
    private long schoolId;
    private String eventTitle;
    private String eventDescription;
    private String startDate;
    private String endDate;
    private long startTime;
    private long endTime;
    private int noOfDays;
    private boolean isContinuousDays;
    private boolean isFullDayEvent;
    private boolean isRecurring;
    private String createdBy;
    private String createdDate;
    private int parentEventId;
    private boolean isSchool;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public boolean isContinuousDays() {
        return isContinuousDays;
    }

    public void setContinuousDays(boolean continuousDays) {
        isContinuousDays = continuousDays;
    }

    public boolean isFullDayEvent() {
        return isFullDayEvent;
    }

    public void setFullDayEvent(boolean fullDayEvent) {
        isFullDayEvent = fullDayEvent;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(int parentEventId) {
        this.parentEventId = parentEventId;
    }

    public boolean isSchool() {
        return isSchool;
    }

    public void setSchool(boolean school) {
        isSchool = school;
    }
}

package com.shikshitha.admin.model;

/**
 * Created by Vinay on 30-07-2017.
 */

public class EventRecurring {
    private int id;
    private int eventId;
    private long schoolId;
    private String recurringType;
    private int separationCount;
    private int maxNumOfOccurrences;
    private int dayOfWeek;
    private int weekOfMonth;
    private int dayOfMonth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public String getRecurringType() {
        return recurringType;
    }

    public void setRecurringType(String recurringType) {
        this.recurringType = recurringType;
    }

    public int getSeparationCount() {
        return separationCount;
    }

    public void setSeparationCount(int separationCount) {
        this.separationCount = separationCount;
    }

    public int getMaxNumOfOccurrences() {
        return maxNumOfOccurrences;
    }

    public void setMaxNumOfOccurrences(int maxNumOfOccurrences) {
        this.maxNumOfOccurrences = maxNumOfOccurrences;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(int weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}

package com.shikshitha.admin.model;

public class Service {
    private long id;
    private long schoolId;
    private boolean isMessage;
    private boolean isSms;
    private boolean isChat;
    private boolean isAttendance;
    private boolean isHomework;
    private boolean isAttendanceSms;
    private boolean isHomeworkSms;
    private boolean isTimetable;
    private boolean isReport;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public boolean isMessage() {
        return isMessage;
    }

    public void setMessage(boolean message) {
        isMessage = message;
    }

    public boolean isSms() {
        return isSms;
    }

    public void setSms(boolean sms) {
        isSms = sms;
    }

    public boolean isChat() {
        return isChat;
    }

    public void setChat(boolean chat) {
        isChat = chat;
    }

    public boolean isAttendance() {
        return isAttendance;
    }

    public void setAttendance(boolean attendance) {
        isAttendance = attendance;
    }

    public boolean isHomework() {
        return isHomework;
    }

    public void setHomework(boolean homework) {
        isHomework = homework;
    }

    public boolean isAttendanceSms() {
        return isAttendanceSms;
    }

    public void setAttendanceSms(boolean attendanceSms) {
        isAttendanceSms = attendanceSms;
    }

    public boolean isHomeworkSms() {
        return isHomeworkSms;
    }

    public void setHomeworkSms(boolean homeworkSms) {
        isHomeworkSms = homeworkSms;
    }

    public boolean isTimetable() {
        return isTimetable;
    }

    public void setTimetable(boolean timetable) {
        isTimetable = timetable;
    }

    public boolean isReport() {
        return isReport;
    }

    public void setReport(boolean report) {
        isReport = report;
    }
}

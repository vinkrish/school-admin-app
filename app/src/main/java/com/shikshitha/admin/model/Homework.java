package com.shikshitha.admin.model;

public class Homework {
    private long id;
    private long sectionId;
    private long subjectId;
    private String subjectName;
    private String homeworkMessage;
    private String homeworkDate;

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

    public String getHomeworkMessage() {
        return homeworkMessage;
    }

    public void setHomeworkMessage(String homeworkMessage) {
        this.homeworkMessage = homeworkMessage;
    }

    public String getHomeworkDate() {
        return homeworkDate;
    }

    public void setHomeworkDate(String homeworkDate) {
        this.homeworkDate = homeworkDate;
    }

}

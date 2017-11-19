package com.shikshitha.admin.reportcard;

/**
 * Created by Vinay on 16-11-2017.
 */

public interface ReportPresenter {
    void getClassList(long teacherId);

    void getSectionList(long classId, long teacherId);

    void getExams(long classId);

    void getExamSubjects(long examId);

    void getScore(long examId, long subjectId, long sectionId);

    void getActivityList(long sectionId, long examId, long subjectId);

    void getActivityScore(long activityId);

    void onDestroy();
}

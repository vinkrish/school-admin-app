package com.shikshitha.admin.reportcard;

import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;

import java.util.List;

/**
 * Created by Vinay on 16-11-2017.
 */

public interface ReportInteractor {

    interface OnFinishedListener {
        void onError(String message);

        void onClassReceived(List<Clas> clasList);

        void onSectionReceived(List<Section> sectionList);

        void onExamReceived(List<Exam> exams);

        void onExamSubjectReceived(List<ExamSubject> examSubjects);

        void onScoreReceived(List<Mark> marks);

        void onActivityReceived(List<Activity> activityList);

        void onActivityScoreReceived(List<ActivityScore> activityScores);
    }

    void getClassList(long teacherId, ReportInteractor.OnFinishedListener listener);

    void getSectionList(long classId, long teacherId, ReportInteractor.OnFinishedListener listener);

    void getExams(long classId, ReportInteractor.OnFinishedListener listener);

    void getExamSubjects(long examId, ReportInteractor.OnFinishedListener listener);

    void getMarks(long examId, long subjectId, long sectionId, ReportInteractor.OnFinishedListener listener);

    void getActivityList(long sectionId, long examId, long subjectId, ReportInteractor.OnFinishedListener listener);

    void getActivityScore(long activityId, ReportInteractor.OnFinishedListener listener);
}

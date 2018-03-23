package com.shikshitha.admin.reportcard;

import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;

import java.util.List;

/**
 * Created by Vinay on 16-11-2017.
 */

public class ReportPresenterImpl implements ReportPresenter, ReportInteractor.OnFinishedListener {
    private ReportView mView;
    private ReportInteractor mInteractor;

    ReportPresenterImpl(ReportView reportView, ReportInteractor reportInteractor) {
        mView = reportView;
        mInteractor = reportInteractor;
    }

    @Override
    public void getClassList(long teacherId) {
        mView.showProgress();
        mInteractor.getClassList(teacherId, this);
    }

    @Override
    public void getSectionList(long classId, long teacherId) {
        mView.showProgress();
        mInteractor.getSectionList(classId, teacherId, this);
    }

    @Override
    public void getExams(long classId) {
        mView.showProgress();
        mInteractor.getExams(classId, this);
    }

    @Override
    public void getExamSubjects(long examId) {
        mView.showProgress();
        mInteractor.getExamSubjects(examId, this);
    }

    @Override
    public void getScore(long examId, long subjectId, long sectionId) {
        mView.showProgress();
        mInteractor.getMarks(examId, subjectId, sectionId, this);
    }

    @Override
    public void getActivityList(long sectionId, long examId, long subjectId) {
        mView.showProgress();
        mInteractor.getActivityList(sectionId, examId, subjectId, this);
    }

    @Override
    public void getActivityScore(long activityId) {
        mView.showProgress();
        mInteractor.getActivityScore(activityId, this);
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if (mView != null) {
            mView.hideProgress();
            mView.showError(message);
        }
    }

    @Override
    public void onClassReceived(List<Clas> clasList) {
        if (mView != null) {
            mView.showClass(clasList);
            mView.hideProgress();
        }
    }

    @Override
    public void onSectionReceived(List<Section> sectionList) {
        if (mView != null) {
            mView.showSection(sectionList);
            mView.hideProgress();
        }
    }

    @Override
    public void onExamReceived(List<Exam> exams) {
        if (mView != null) {
            mView.showExam(exams);
            mView.hideProgress();
        }
    }

    @Override
    public void onExamSubjectReceived(List<ExamSubject> examSubjects) {
        if (mView != null) {
            mView.showExamSubject(examSubjects);
            mView.hideProgress();
        }
    }

    @Override
    public void onScoreReceived(List<Mark> marks) {
        if (mView != null) {
            mView.showScore(marks);
            mView.hideProgress();
        }
    }

    @Override
    public void onActivityReceived(List<Activity> activityList) {
        if (mView != null) {
            mView.showActivity(activityList);
            mView.hideProgress();
        }
    }

    @Override
    public void onActivityScoreReceived(List<ActivityScore> activityScores) {
        if (mView != null) {
            mView.showActivityScore(activityScores);
            mView.hideProgress();
        }
    }
}

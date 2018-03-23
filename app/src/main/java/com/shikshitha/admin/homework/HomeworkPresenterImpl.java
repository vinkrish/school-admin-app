package com.shikshitha.admin.homework;

import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Homework;
import com.shikshitha.admin.model.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 21-04-2017.
 */

public class HomeworkPresenterImpl implements HomeworkPresenter,
        HomeworkInteractorImpl.OnFinishedListener {

    private HomeworkView mView;
    private HomeworkInteractor mInteractor;

    HomeworkPresenterImpl(HomeworkView view, HomeworkInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getClassList(long schoolId) {
        mView.showProgress();
        mInteractor.getClassList(schoolId, this);
    }

    @Override
    public void getSectionList(long classId) {
        mView.showProgress();
        mInteractor.getSectionList(classId, this);
    }

    @Override
    public void getHomework(long sectionId, String date) {
        mView.showProgress();
        mInteractor.getHomework(sectionId, date, this);
    }

    @Override
    public void saveHomework(Homework homework) {
        mView.showProgress();
        mInteractor.saveHomework(homework, this);
    }

    @Override
    public void updateHomework(Homework homework) {
        mView.showProgress();
        mInteractor.updateHomework(homework, this);
    }

    @Override
    public void deleteHomework(ArrayList<Homework> homeworks) {
        mView.showProgress();
        mInteractor.deleteHomework(homeworks, this);
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
    public void loadOffline(String tableName) {
        if (mView != null) {
            mView.showOffline(tableName);
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
    public void onHomeworkReceived(List<Homework> homeworks) {
        if (mView != null) {
            mView.showHomeworks(homeworks);
            mView.hideProgress();
        }
    }

    @Override
    public void onHomeworkSaved(Homework homework) {
        if (mView != null) {
            mView.homeworkSaved(homework);
            mView.hideProgress();
        }
    }

    @Override
    public void onHomeworkUpdated() {
        if (mView != null) {
            mView.homeworkUpdated();
            mView.hideProgress();
        }
    }

    @Override
    public void onHomeworkDeleted() {
        if (mView != null) {
            mView.homeworkDeleted();
            mView.hideProgress();
        }
    }
}

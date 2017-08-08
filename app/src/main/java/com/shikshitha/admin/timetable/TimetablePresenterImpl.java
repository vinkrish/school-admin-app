package com.shikshitha.admin.timetable;

import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Timetable;

import java.util.List;

/**
 * Created by Vinay on 13-06-2017.
 */

class TimetablePresenterImpl implements TimetablePresenter, TimetableInteractor.OnFinishedListener {
    private TimetableView mView;
    private TimetableInteractor mInteractor;

    TimetablePresenterImpl(TimetableView view, TimetableInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getClassList(long schoolId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getClassList(schoolId, this);
        }
    }

    @Override
    public void getSectionList(long classId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getSectionList(classId, this);
        }
    }

    @Override
    public void getTimetable(long sectionId) {
        if(mView != null) {
            mView.showProgress();
            mInteractor.getTimetable(sectionId, this);
        }
    }

    @Override
    public void onDestroy() {
        if(mView != null) {
            mView = null;
        }
    }

    @Override
    public void onError(String message) {
        if(mView != null) {
            mView.hideProgress();
            mView.showError(message);
        }
    }

    @Override
    public void loadOffline(String tableName) {
        if(mView != null) {
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
    public void onTimetableReceived(List<Timetable> timetableList) {
        if(mView != null) {
            mView.showTimetable(timetableList);
            mView.hideProgress();
        }
    }
}

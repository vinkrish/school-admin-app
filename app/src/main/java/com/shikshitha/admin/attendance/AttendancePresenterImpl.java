package com.shikshitha.admin.attendance;

import com.shikshitha.admin.model.Attendance;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Timetable;

import java.util.List;

/**
 * Created by Vinay on 21-04-2017.
 */

public class AttendancePresenterImpl implements AttendancePresenter,
        AttendanceInteractor.OnFinishedListener {

    private AttendanceView mView;
    private AttendanceInteractor mInteractor;

    AttendancePresenterImpl(AttendanceView view, AttendanceInteractor interactor) {
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
    public void getAttendance(long sectionId, String date, int session) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getAttendance(sectionId, date, session, this);
        }
    }

    @Override
    public void getTimetable(long sectionId, String dayOfWeek) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getTimetable(sectionId, dayOfWeek, this);
        }
    }

    @Override
    public void saveAttendance(List<Attendance> attendances) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.saveAttendance(attendances, this);
        }
    }

    @Override
    public void deleteAttendance(List<Attendance> attendances) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.deleteAttendance(attendances, this);
        }
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
    public void onTimetableReceived(List<Timetable> timetables) {
        if (mView != null) {
            mView.showTimetable(timetables);
            mView.hideProgress();
        }
    }

    @Override
    public void onAttendanceReceived(AttendanceSet attendanceSet) {
        if (mView != null) {
            mView.showAttendance(attendanceSet);
            mView.hideProgress();
        }
    }

    @Override
    public void onAttendanceSaved() {
        if (mView != null) {
            mView.hideProgress();
            mView.attendanceSaved();
        }
    }

    @Override
    public void onAttendanceDeleted() {
        if (mView != null) {
            mView.hideProgress();
            mView.attendanceDeleted();
        }
    }
}

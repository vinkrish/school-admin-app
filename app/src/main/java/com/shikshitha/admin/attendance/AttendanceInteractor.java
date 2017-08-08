package com.shikshitha.admin.attendance;

import com.shikshitha.admin.model.Attendance;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Timetable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 21-04-2017.
 */

interface AttendanceInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void loadOffline(String tableName);

        void onClassReceived(List<Clas> clasList);

        void onSectionReceived(List<Section> sectionList);

        void onTimetableReceived(List<Timetable> timetables);

        void onAttendanceReceived(AttendanceSet attendanceSet);

        void onAttendanceSaved();

        void onAttendanceDeleted();
    }

    void getClassList(long schoolId, AttendanceInteractor.OnFinishedListener listener);

    void getSectionList(long classId, AttendanceInteractor.OnFinishedListener listener);

    void getTimetable(long sectionId, String dayOfWeek, AttendanceInteractor.OnFinishedListener listener);

    void getAttendance(long sectionId, String date, int session, AttendanceInteractor.OnFinishedListener listener);

    void saveAttendance(ArrayList<Attendance> attendances, AttendanceInteractor.OnFinishedListener listener);

    void deleteAttendance(ArrayList<Attendance> attendances, AttendanceInteractor.OnFinishedListener listener);
}

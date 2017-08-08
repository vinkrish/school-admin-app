package com.shikshitha.admin.attendance;

import com.shikshitha.admin.model.Attendance;

import java.util.ArrayList;

/**
 * Created by Vinay on 21-04-2017.
 */

interface AttendancePresenter {
    void getClassList(long schoolId);

    void getSectionList(long classId);

    void getAttendance(long sectionId, String date, int session);

    void getTimetable(long sectionId, String dayOfWeek);

    void saveAttendance(ArrayList<Attendance> attendances);

    void deleteAttendance(ArrayList<Attendance> attendances);

    void onDestroy();
}

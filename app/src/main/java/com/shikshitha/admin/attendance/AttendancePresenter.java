package com.shikshitha.admin.attendance;

import com.shikshitha.admin.model.Attendance;

import java.util.List;

/**
 * Created by Vinay on 21-04-2017.
 */

interface AttendancePresenter {
    void getClassList(long schoolId);

    void getSectionList(long classId);

    void getAttendance(long sectionId, String date, int session);

    void getTimetable(long sectionId, String dayOfWeek);

    void saveAttendance(List<Attendance> attendances);

    void deleteAttendance(List<Attendance> attendances);

    void onDestroy();
}

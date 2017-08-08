package com.shikshitha.admin.timetable;

/**
 * Created by Vinay on 13-06-2017.
 */

interface TimetablePresenter {
    void getClassList(long schoolId);

    void getSectionList(long classId);

    void getTimetable(long sectionId);

    void onDestroy();
}

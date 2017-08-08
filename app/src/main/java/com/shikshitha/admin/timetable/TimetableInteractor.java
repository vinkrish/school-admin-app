package com.shikshitha.admin.timetable;

import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Timetable;

import java.util.List;

/**
 * Created by Vinay on 13-06-2017.
 */

interface TimetableInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void loadOffline(String tableName);

        void onClassReceived(List<Clas> clasList);

        void onSectionReceived(List<Section> sectionList);

        void onTimetableReceived(List<Timetable> timetableList);
    }
    void getClassList(long schoolId, TimetableInteractor.OnFinishedListener listener);

    void getSectionList(long classId, TimetableInteractor.OnFinishedListener listener);

    void getTimetable(long sectionId, TimetableInteractor.OnFinishedListener listener);
}

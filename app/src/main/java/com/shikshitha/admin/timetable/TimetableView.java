package com.shikshitha.admin.timetable;

import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Timetable;

import java.util.List;

/**
 * Created by Vinay on 13-06-2017.
 */

interface TimetableView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void showOffline(String tableName);

    void showClass(List<Clas> clasList);

    void showSection(List<Section> sectionList);

    void showTimetable(List<Timetable> timetableList);
}

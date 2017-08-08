package com.shikshitha.admin.homework;

import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Homework;
import com.shikshitha.admin.model.Section;

import java.util.List;

/**
 * Created by Vinay on 21-04-2017.
 */

interface HomeworkView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void showOffline(String tableName);

    void showClass(List<Clas> clasList);

    void showSection(List<Section> sectionList);

    void showHomeworks(List<Homework> homeworks);

    void homeworkSaved(Homework homework);

    void homeworkUpdated();

    void homeworkDeleted();
}

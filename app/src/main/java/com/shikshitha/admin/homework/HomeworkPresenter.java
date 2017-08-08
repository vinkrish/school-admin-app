package com.shikshitha.admin.homework;

import com.shikshitha.admin.model.Homework;

import java.util.ArrayList;

/**
 * Created by Vinay on 21-04-2017.
 */

interface HomeworkPresenter {
    void getClassList(long schoolId);

    void getSectionList(long classId);

    void getHomework(long sectionId, String date);

    void saveHomework(Homework homework);

    void updateHomework(Homework homework);

    void deleteHomework(ArrayList<Homework> homeworks);

    void onDestroy();
}

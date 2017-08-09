package com.shikshitha.admin.newchat;

import com.shikshitha.admin.model.Chat;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Student;

import java.util.List;

/**
 * Created by Vinay on 28-04-2017.
 */

interface NewChatView {
    void showProgress();

    void hideProgess();

    void showError(String message);

    void showClass(List<Clas> clasList);

    void showSection(List<Section> sectionList);

    void showStudent(List<Student> studentList);

    void chatSaved(Chat chat);
}

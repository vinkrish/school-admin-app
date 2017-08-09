package com.shikshitha.admin.newchat;

import com.shikshitha.admin.model.Chat;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Student;

import java.util.List;

/**
 * Created by Vinay on 28-04-2017.
 */

interface NewChatInteractor {

    interface OnFinishedListener {
        void onError(String message);

        void onClasReceived(List<Clas> clasList);

        void onSectionReceived(List<Section> sectionList);

        void onStudentReceived(List<Student> studentList);

        void onChatSaved(Chat chat);
    }

    void getClassList(long schoolId, NewChatInteractor.OnFinishedListener listener);

    void getSectionList(long classId, NewChatInteractor.OnFinishedListener listener);

    void getStudentList(long sectionId, NewChatInteractor.OnFinishedListener listener);

    void saveChat(Chat chat, NewChatInteractor.OnFinishedListener listener);
}

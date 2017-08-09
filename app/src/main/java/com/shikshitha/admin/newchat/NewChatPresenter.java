package com.shikshitha.admin.newchat;

import com.shikshitha.admin.model.Chat;

/**
 * Created by Vinay on 28-04-2017.
 */

interface NewChatPresenter {
    void getClassList(long schoolId);

    void getSectionList(long classId);

    void getStudentList(long sectionId);

    void saveChat(Chat chat);

    void onDestroy();
}

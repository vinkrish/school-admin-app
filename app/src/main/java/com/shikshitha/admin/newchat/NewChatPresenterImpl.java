package com.shikshitha.admin.newchat;

import com.shikshitha.admin.model.Chat;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Student;

import java.util.List;

/**
 * Created by Vinay on 28-04-2017.
 */

class NewChatPresenterImpl implements NewChatPresenter, NewChatInteractor.OnFinishedListener {

    private NewChatView mView;
    private NewChatInteractor mInteractor;

    NewChatPresenterImpl(NewChatView view, NewChatInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getClassList(long schoolId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getClassList(schoolId, this);
        }
    }

    @Override
    public void getSectionList(long classId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getSectionList(classId, this);
        }
    }

    @Override
    public void getStudentList(long sectionId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getStudentList(sectionId, this);
        }
    }

    @Override
    public void saveChat(Chat chat) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.saveChat(chat, this);
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if (mView != null) {
            mView.hideProgess();
            mView.showError(message);
        }
    }

    @Override
    public void onClasReceived(List<Clas> clasList) {
        if (mView != null) {
            mView.showClass(clasList);
            mView.hideProgess();
        }
    }

    @Override
    public void onSectionReceived(List<Section> sectionList) {
        if (mView != null) {
            mView.showSection(sectionList);
            mView.hideProgess();
        }
    }

    @Override
    public void onStudentReceived(List<Student> studentList) {
        if (mView != null) {
            mView.showStudent(studentList);
            mView.hideProgess();
        }
    }

    @Override
    public void onChatSaved(Chat chat) {
        if (mView != null) {
            mView.chatSaved(chat);
            mView.hideProgess();
        }
    }
}

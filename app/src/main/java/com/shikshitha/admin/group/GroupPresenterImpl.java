package com.shikshitha.admin.group;

import com.shikshitha.admin.dao.DeletedGroupDao;
import com.shikshitha.admin.model.Authorization;
import com.shikshitha.admin.model.DeletedGroup;
import com.shikshitha.admin.model.Groups;

import java.util.List;

/**
 * Created by Vinay on 02-04-2017.
 */

class GroupPresenterImpl implements GroupPresenter, GroupInteractor.OnFinishedListener {
    private GroupView mView;
    private GroupInteractor mInteractor;

    GroupPresenterImpl(GroupView view, GroupInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getGroupsAboveId(long teacherId, long id) {
        mInteractor.getGroupsAboveId(teacherId, id, this);
    }

    @Override
    public void getGroups(long userId) {
        mView.showProgress();
        mInteractor.getGroups(userId, this);
    }

    @Override
    public void getRecentDeletedGroups(long schoolId, long id) {
        mInteractor.getRecentDeletedGroups(schoolId, id, this);
    }

    @Override
    public void getDeletedGroups(long schoolId) {
        mInteractor.getDeletedGroups(schoolId, this);
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if (mView != null) {
            mView.hideProgress();
            mView.showError(message);
        }
    }

    @Override
    public void onRecentGroupsReceived(List<Groups> groupsList) {
        if (mView != null) {
            mView.setRecentGroups(groupsList);
            mView.hideProgress();
        }
    }

    @Override
    public void onGroupsReceived(List<Groups> groupsList) {
        if (mView != null) {
            mView.setGroups(groupsList);
            mView.hideProgress();
        }
    }

    @Override
    public void onDeletedGroupsReceived(List<DeletedGroup> deletedGroups) {
        if (mView != null) {
            DeletedGroupDao.insertDeletedGroups(deletedGroups);
        }
    }

}

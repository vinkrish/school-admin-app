package com.shikshitha.admin.usergroup;

import com.shikshitha.admin.dao.DeletedGroupDao;
import com.shikshitha.admin.model.DeletedGroup;
import com.shikshitha.admin.model.GroupUsers;
import com.shikshitha.admin.model.UserGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 01-04-2017.
 */

class UserGroupPresenterImpl implements UserGroupPresenter,
        UserGroupInteractor.OnFinishedListener {

    private UserGroupView mView;
    private UserGroupInteractor mInteractor;

    UserGroupPresenterImpl(UserGroupView view, UserGroupInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getUserGroup(long groupId) {
        mView.showProgress();
        mInteractor.getUserGroup(groupId, this);
    }

    @Override
    public void saveUserGroup(ArrayList<UserGroup> userGroups) {
        mView.showProgress();
        mInteractor.saveUserGroup(userGroups, this);
    }

    @Override
    public void deleteUsers(ArrayList<UserGroup> userGroups) {
        mView.showProgress();
        mInteractor.deleteUsers(userGroups, this);
    }

    @Override
    public void deleteGroup(DeletedGroup deletedGroup) {
        mView.showProgress();
        mInteractor.deleteGroup(deletedGroup, this);
    }

    @Override
    public void getRecentDeletedGroups(long schoolId, long id) {
        mView.showProgress();
        mInteractor.getRecentDeletedGroups(schoolId, id, this);
    }

    @Override
    public void getDeletedGroups(long schoolId) {
        mView.showProgress();
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
    public void onUserGroupReceived(GroupUsers groupUsers) {
        if (mView != null) {
            mView.showUserGroup(groupUsers);
            mView.hideProgress();
        }
    }

    @Override
    public void onUserGroupSaved() {
        if (mView != null) {
            mView.hideProgress();
            mView.userGroupSaved();
        }
    }

    @Override
    public void onUsersDeleted() {
        if (mView != null) {
            mView.hideProgress();
            mView.userGroupDeleted();
        }
    }

    @Override
    public void onGroupDeleted(DeletedGroup deletedGroup) {
        if (mView != null) {
            mView.hideProgress();
            mView.groupDeleted();
        }
    }

    @Override
    public void onDeletedGroupsReceived(List<DeletedGroup> deletedGroups) {
        if (mView != null) {
            DeletedGroupDao.insertDeletedGroups(deletedGroups);
            mView.hideProgress();
            mView.onDeletedGroupSync();
        }
    }

}

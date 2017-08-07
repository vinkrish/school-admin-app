package com.shikshitha.admin.usergroup;

import com.shikshitha.admin.model.GroupUsers;
import com.shikshitha.admin.model.UserGroup;

import java.util.ArrayList;

/**
 * Created by Vinay on 01-04-2017.
 */

public class UserGroupPresenterImpl implements UserGroupPresenter,
        UserGroupInteractor.OnFinishedListener {

    private UserGroupView mView;
    private UserGroupInteractor mInteractor;

    UserGroupPresenterImpl(UserGroupView view, UserGroupInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getUserGroup(long groupId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getUserGroup(groupId, this);
        }
    }

    @Override
    public void saveUserGroup(ArrayList<UserGroup> userGroups) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.saveUserGroup(userGroups, this);
        }
    }

    @Override
    public void deleteUsers(ArrayList<UserGroup> userGroups) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.deleteUsers(userGroups, this);
        }
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
        if(mView != null) {
            mView.hideProgress();
            mView.userGroupSaved();
        }
    }

    @Override
    public void onUsersDeleted() {
        if(mView != null) {
            mView.hideProgress();
            mView.userGroupDeleted();
        }
    }

}

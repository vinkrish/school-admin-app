package com.shikshitha.admin.usergroup;

import com.shikshitha.admin.model.DeletedGroup;
import com.shikshitha.admin.model.GroupUsers;
import com.shikshitha.admin.model.UserGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 01-04-2017.
 */

interface UserGroupInteractor {

    interface OnFinishedListener {
        void onError(String message);

        void onUserGroupReceived(GroupUsers groupUsers);

        void onUserGroupSaved();

        void onUsersDeleted();

        void onGroupDeleted(DeletedGroup deletedGroup);

        void onDeletedGroupsReceived(List<DeletedGroup> deletedGroups);
    }

    void getUserGroup(long groupId, UserGroupInteractor.OnFinishedListener listener);

    void saveUserGroup(ArrayList<UserGroup> userGroups, UserGroupInteractor.OnFinishedListener listener);

    void deleteUsers(ArrayList<UserGroup> userGroups, UserGroupInteractor.OnFinishedListener listener);

    void deleteGroup(DeletedGroup deletedGroup, UserGroupInteractor.OnFinishedListener listener);

    void getRecentDeletedGroups(long schoolId, long id, UserGroupInteractor.OnFinishedListener listener);

    void getDeletedGroups(long schoolId, UserGroupInteractor.OnFinishedListener listener);
}

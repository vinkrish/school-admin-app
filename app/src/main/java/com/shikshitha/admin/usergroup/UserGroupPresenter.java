package com.shikshitha.admin.usergroup;

import com.shikshitha.admin.model.DeletedGroup;
import com.shikshitha.admin.model.UserGroup;

import java.util.ArrayList;

/**
 * Created by Vinay on 01-04-2017.
 */

interface UserGroupPresenter {
    void getUserGroup(long groupId);

    void saveUserGroup(ArrayList<UserGroup> userGroups);

    void deleteUsers(ArrayList<UserGroup> userGroups);

    void deleteGroup(DeletedGroup deletedGroup);

    void getRecentDeletedGroups(long schoolId, long id);

    void getDeletedGroups(long schoolId);

    void onDestroy();
}

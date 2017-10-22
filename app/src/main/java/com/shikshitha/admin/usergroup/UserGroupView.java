package com.shikshitha.admin.usergroup;

import com.shikshitha.admin.model.GroupUsers;

/**
 * Created by Vinay on 01-04-2017.
 */

interface UserGroupView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void showUserGroup(GroupUsers groupUsers);

    void userGroupSaved();

    void userGroupDeleted();

    void groupDeleted();

    void onDeletedGroupSync();
}

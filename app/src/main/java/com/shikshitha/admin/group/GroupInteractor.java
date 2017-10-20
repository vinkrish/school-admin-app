package com.shikshitha.admin.group;

import com.shikshitha.admin.model.Authorization;
import com.shikshitha.admin.model.DeletedGroup;
import com.shikshitha.admin.model.Groups;

import java.util.List;

/**
 * Created by Vinay on 02-04-2017.
 */

interface GroupInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onRecentGroupsReceived(List<Groups> groupsList);

        void onGroupsReceived(List<Groups> groupsList);

        void onDeletedGroupsReceived(List<DeletedGroup> deletedGroups);
    }

    void getGroupsAboveId(long userId, long id, GroupInteractor.OnFinishedListener listener);

    void getGroups(long userId, GroupInteractor.OnFinishedListener listener);

    void getRecentDeletedGroups(long schoolId, long id, GroupInteractor.OnFinishedListener listener);

    void getDeletedGroups(long schoolId, GroupInteractor.OnFinishedListener listener);
}

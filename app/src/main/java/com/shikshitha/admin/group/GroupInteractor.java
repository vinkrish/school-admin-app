package com.shikshitha.admin.group;

import com.shikshitha.admin.model.Authorization;
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
    }

    void getGroupsAboveId(long userId, long id, GroupInteractor.OnFinishedListener listener);

    void getGroups(long userId, GroupInteractor.OnFinishedListener listener);
}

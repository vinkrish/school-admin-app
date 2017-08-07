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

        void onGroupsReceived(List<Groups> groupsList);
    }

    void getGroups(long userId, GroupInteractor.OnFinishedListener listener);

    void updateFcmToken(Authorization authorization);
}

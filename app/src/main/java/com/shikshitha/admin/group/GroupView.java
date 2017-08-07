package com.shikshitha.admin.group;

import com.shikshitha.admin.model.Groups;

import java.util.List;

/**
 * Created by Vinay on 02-04-2017.
 */

interface GroupView {
    void showProgress();

    void hideProgess();

    void showError(String message);

    void setGroups(List<Groups> groups);
}

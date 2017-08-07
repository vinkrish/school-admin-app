package com.shikshitha.admin.newgroup;

import com.shikshitha.admin.model.Groups;

/**
 * Created by Vinay on 30-03-2017.
 */

interface NewGroupPresenter {
    void getClassList(long schoolId);

    void getSectionList(long classId);

    void saveGroup(Groups groups);

    void onDestroy();
}

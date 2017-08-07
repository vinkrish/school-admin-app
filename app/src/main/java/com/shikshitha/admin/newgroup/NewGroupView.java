package com.shikshitha.admin.newgroup;

import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Groups;
import com.shikshitha.admin.model.Section;

import java.util.List;

/**
 * Created by Vinay on 30-03-2017.
 */

interface NewGroupView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void showClass(List<Clas> clasList);

    void showSection(List<Section> sectionList);

    void groupSaved(Groups groups);
}

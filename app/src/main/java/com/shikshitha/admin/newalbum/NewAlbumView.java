package com.shikshitha.admin.newalbum;

import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;

import java.util.List;

/**
 * Created by Vinay on 19-12-2017.
 */

public interface NewAlbumView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void showClass(List<Clas> clasList);

    void showSection(List<Section> sectionList);

    void albumSaved(Album album);
}

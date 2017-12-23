package com.shikshitha.admin.newalbum;

import com.shikshitha.admin.model.Album;

/**
 * Created by Vinay on 19-12-2017.
 */

public interface NewAlbumPresenter {
    void getClassList(long schoolId);

    void getSectionList(long classId);

    void saveAlbum(Album album);

    void onDestroy();
}

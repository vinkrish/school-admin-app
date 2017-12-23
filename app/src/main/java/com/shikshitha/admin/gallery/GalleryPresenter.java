package com.shikshitha.admin.gallery;

import com.shikshitha.admin.model.DeletedAlbum;

/**
 * Created by Vinay on 30-10-2017.
 */

interface GalleryPresenter {
    void getClassList(long schoolId);

    void getSectionList(long classId);

    void deleteAlbum(DeletedAlbum deletedAlbum);

    void getAlbumsAboveId(long schoolId, long id);

    void getAlbums(long schoolId);

    void getRecentDeletedAlbums(long schoolId, long id);

    void getDeletedAlbums(long schoolId);

    void onDestroy();
}

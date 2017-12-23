package com.shikshitha.admin.gallery;

import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.DeletedAlbum;
import com.shikshitha.admin.model.Section;

import java.util.List;

/**
 * Created by Vinay on 30-10-2017.
 */

interface GalleryInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onClassReceived(List<Clas> classList);

        void onSectionReceived(List<Section> sectionList);

        void onAlbumDeleted();

        void onRecentAlbumsReceived(List<Album> albumList);

        void onAlbumsReceived(List<Album> albumList);

        void onDeletedAlbumsReceived(List<DeletedAlbum> deletedAlbums);
    }

    void getClassList(long schoolId, GalleryInteractor.OnFinishedListener listener);

    void getSectionList(long classId, GalleryInteractor.OnFinishedListener listener);

    void deleteAlbum(DeletedAlbum deletedAlbum, GalleryInteractor.OnFinishedListener listener);

    void getAlbumsAboveId(long schoolId, long id, GalleryInteractor.OnFinishedListener listener);

    void getAlbums(long schoolId, GalleryInteractor.OnFinishedListener listener);

    void getRecentDeletedAlbums(long schoolId, long id, GalleryInteractor.OnFinishedListener listener);

    void getDeletedAlbums(long schoolId, GalleryInteractor.OnFinishedListener listener);
}

package com.shikshitha.admin.album;

import com.shikshitha.admin.model.AlbumImage;
import com.shikshitha.admin.model.DeletedAlbumImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 01-11-2017.
 */

interface AlbumInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onAlbumImagesSaved(List<AlbumImage> albumImages);

        void onImagesDeleted(ArrayList<DeletedAlbumImage> albumImages);

        void onRecentAlbumImagesReceived(ArrayList<AlbumImage> albumImages);

        void onAlbumImagesReceived(ArrayList<AlbumImage> albumImages);

        void onAlbumImagesDeleted(List<DeletedAlbumImage> deletedAlbumImages);
    }

    void getAlbumUpdate(long albumId, AlbumInteractor.OnFinishedListener listener);

    void saveAlbumImages(List<AlbumImage> albumImages, AlbumInteractor.OnFinishedListener listener);

    void deleteAlbumImages(ArrayList<DeletedAlbumImage> deletedAlbumImages, AlbumInteractor.OnFinishedListener listener);

    void getAlbumImagesAboveId(long albumId, long id, AlbumInteractor.OnFinishedListener listener);

    void getAlbumImages(long albumId, AlbumInteractor.OnFinishedListener listener);

    void getRecentDeletedAlbumImages(long albumId, long id, AlbumInteractor.OnFinishedListener listener);

    void getDeletedAlbumImages(long albumId, AlbumInteractor.OnFinishedListener listener);
}

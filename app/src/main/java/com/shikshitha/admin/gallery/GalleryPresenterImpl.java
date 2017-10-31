package com.shikshitha.admin.gallery;

import com.shikshitha.admin.dao.DeletedAlbumDao;
import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.DeletedAlbum;

import java.util.List;

/**
 * Created by Vinay on 30-10-2017.
 */

class GalleryPresenterImpl implements GalleryPresenter, GalleryInteractor.OnFinishedListener {
    private GalleryView mView;
    private GalleryInteractor mInteractor;

    GalleryPresenterImpl(GalleryView view, GalleryInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void saveAlbum(Album album) {
        if(mView != null) {
            mView.showProgress();
            mInteractor.saveAlbum(album, this);
        }
    }

    @Override
    public void deleteAlbum(DeletedAlbum deletedAlbum) {
        if(mView != null) {
            mView.showProgress();
            mInteractor.deleteAlbum(deletedAlbum, this);
        }
    }

    @Override
    public void getAlbumsAboveId(long schoolId, long id) {
        if (mView != null) {
            mInteractor.getAlbumsAboveId(schoolId, id, this);
        }
    }

    @Override
    public void getAlbums(long schoolId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getAlbums(schoolId, this);
        }
    }

    @Override
    public void getRecentDeletedAlbums(long schoolId, long id) {
        if (mView != null) {
            mInteractor.getRecentDeletedAlbums(schoolId, id, this);
        }
    }

    @Override
    public void getDeletedAlbums(long schoolId) {
        if (mView != null) {
            mInteractor.getDeletedAlbums(schoolId, this);
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if (mView != null) {
            mView.hideProgress();
            mView.showError(message);
        }
    }

    @Override
    public void onAlbumSaved(Album album) {
        if (mView != null) {
            mView.hideProgress();
            mView.setAlbum(album);
        }
    }

    @Override
    public void onAlbumDeleted() {
        if (mView != null) {
            mView.hideProgress();
            mView.albumDeleted();
        }
    }

    @Override
    public void onRecentAlbumsReceived(List<Album> albumList) {
        if (mView != null) {
            mView.setRecentAlbums(albumList);
            mView.hideProgress();
        }
    }

    @Override
    public void onAlbumsReceived(List<Album> groupsList) {
        if (mView != null) {
            mView.setAlbums(groupsList);
            mView.hideProgress();
        }
    }

    @Override
    public void onDeletedAlbumsReceived(List<DeletedAlbum> deletedAlbums) {
        if (mView != null) {
            DeletedAlbumDao.insertDeletedAlbums(deletedAlbums);
        }
    }
}

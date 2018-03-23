package com.shikshitha.admin.gallery;

import com.shikshitha.admin.dao.DeletedAlbumDao;
import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.DeletedAlbum;
import com.shikshitha.admin.model.Section;

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
    public void getClassList(long schoolId) {
        mView.showProgress();
        mInteractor.getClassList(schoolId, this);
    }

    @Override
    public void getSectionList(long classId) {
        mView.showProgress();
        mInteractor.getSectionList(classId, this);
    }

    @Override
    public void deleteAlbum(DeletedAlbum deletedAlbum) {
        mView.showProgress();
        mInteractor.deleteAlbum(deletedAlbum, this);
    }

    @Override
    public void getAlbumsAboveId(long schoolId, long id) {
        mInteractor.getAlbumsAboveId(schoolId, id, this);
    }

    @Override
    public void getAlbums(long schoolId) {
        mView.showProgress();
        mInteractor.getAlbums(schoolId, this);
    }

    @Override
    public void getRecentDeletedAlbums(long schoolId, long id) {
        mInteractor.getRecentDeletedAlbums(schoolId, id, this);
    }

    @Override
    public void getDeletedAlbums(long schoolId) {
        mInteractor.getDeletedAlbums(schoolId, this);
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
    public void onClassReceived(List<Clas> classList) {
        if (mView != null) {
            mView.showClass(classList);
            mView.hideProgress();
        }
    }

    @Override
    public void onSectionReceived(List<Section> sectionList) {
        if (mView != null) {
            mView.showSection(sectionList);
            mView.hideProgress();
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
    public void onAlbumsReceived(List<Album> albumList) {
        if (mView != null) {
            mView.setAlbums(albumList);
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

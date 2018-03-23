package com.shikshitha.admin.newalbum;

import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;

import java.util.List;

/**
 * Created by Vinay on 19-12-2017.
 */

public class NewAlbumPresenterImpl implements NewAlbumPresenter, NewAlbumInteractor.OnFinishedListener {
    private NewAlbumView mView;
    private NewAlbumInteractor mInteractor;

    NewAlbumPresenterImpl(NewAlbumView view, NewAlbumInteractor interactor) {
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
    public void saveAlbum(Album album) {
        mView.showProgress();
        mInteractor.saveAlbum(album, this);
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
    public void onAlbumSaved(Album album) {
        if (mView != null) {
            mView.albumSaved(album);
            mView.hideProgress();
        }
    }
}

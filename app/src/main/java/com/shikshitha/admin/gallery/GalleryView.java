package com.shikshitha.admin.gallery;

import com.shikshitha.admin.model.Album;

import java.util.List;

/**
 * Created by Vinay on 30-10-2017.
 */

interface GalleryView {

    void showProgress();

    void hideProgress();

    void showError(String message);

    void setAlbum(Album album);

    void albumDeleted();

    void setRecentAlbums(List<Album> albums);

    void setAlbums(List<Album> albums);

}

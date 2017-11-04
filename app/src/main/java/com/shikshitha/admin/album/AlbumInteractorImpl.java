package com.shikshitha.admin.album;

import com.shikshitha.admin.App;
import com.shikshitha.admin.R;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.api.GalleryApi;
import com.shikshitha.admin.dao.AlbumDao;
import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.AlbumImage;
import com.shikshitha.admin.model.DeletedAlbumImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 01-11-2017.
 */

class AlbumInteractorImpl implements AlbumInteractor {
    @Override
    public void getAlbumUpdate(long albumId, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<Album> queue = api.getAlbum(albumId);
        queue.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if(response.isSuccessful()) {
                    AlbumDao.update(response.body());
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void saveAlbumImages(final List<AlbumImage> albumImages, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<Void> queue = api.saveAlbumImages(albumImages);
        queue.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    listener.onAlbumImagesSaved(albumImages);
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void deleteAlbumImages(final ArrayList<DeletedAlbumImage> deletedAlbumImages, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<Void> queue = api.deleteAlbumImages(deletedAlbumImages);
        queue.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    listener.onImagesDeleted(deletedAlbumImages);
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getAlbumImagesAboveId(long albumId, long id, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<ArrayList<AlbumImage>> queue = api.getAlbumImagesAboveId(albumId, id);
        queue.enqueue(new Callback<ArrayList<AlbumImage>>() {
            @Override
            public void onResponse(Call<ArrayList<AlbumImage>> call, Response<ArrayList<AlbumImage>> response) {
                if(response.isSuccessful()) {
                    listener.onRecentAlbumImagesReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AlbumImage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getAlbumImages(long albumId, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<ArrayList<AlbumImage>> queue = api.getAlbumImages(albumId);
        queue.enqueue(new Callback<ArrayList<AlbumImage>>() {
            @Override
            public void onResponse(Call<ArrayList<AlbumImage>> call, Response<ArrayList<AlbumImage>> response) {
                if(response.isSuccessful()) {
                    listener.onAlbumImagesReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AlbumImage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getRecentDeletedAlbumImages(long albumId, long id, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<List<DeletedAlbumImage>> queue = api.getDeletedAlbumImagesAboveId(albumId, id);
        queue.enqueue(new Callback<List<DeletedAlbumImage>>() {
            @Override
            public void onResponse(Call<List<DeletedAlbumImage>> call, Response<List<DeletedAlbumImage>> response) {
                if(response.isSuccessful()) {
                    listener.onAlbumImagesDeleted(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<DeletedAlbumImage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getDeletedAlbumImages(long albumId, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<List<DeletedAlbumImage>> queue = api.getDeletedAlbumImages(albumId);
        queue.enqueue(new Callback<List<DeletedAlbumImage>>() {
            @Override
            public void onResponse(Call<List<DeletedAlbumImage>> call, Response<List<DeletedAlbumImage>> response) {
                if(response.isSuccessful()) {
                    listener.onAlbumImagesDeleted(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<DeletedAlbumImage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}

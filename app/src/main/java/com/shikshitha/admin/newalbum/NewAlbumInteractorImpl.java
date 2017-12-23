package com.shikshitha.admin.newalbum;

import com.shikshitha.admin.App;
import com.shikshitha.admin.R;
import com.shikshitha.admin.api.AdminApi;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.api.GalleryApi;
import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 19-12-2017.
 */

public class NewAlbumInteractorImpl implements NewAlbumInteractor {
    @Override
    public void getClassList(long schoolId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<List<Clas>> queue = api.getClassList(schoolId);
        queue.enqueue(new Callback<List<Clas>>() {
            @Override
            public void onResponse(Call<List<Clas>> call, Response<List<Clas>> response) {
                if(response.isSuccessful()) {
                    listener.onClassReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Clas>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getSectionList(long classId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<List<Section>> queue = api.getSectionList(classId);
        queue.enqueue(new Callback<List<Section>>() {
            @Override
            public void onResponse(Call<List<Section>> call, Response<List<Section>> response) {
                if(response.isSuccessful()) {
                    listener.onSectionReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Section>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void saveAlbum(Album album, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<Album> queue = api.saveAlbum(album);
        queue.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if(response.isSuccessful()) {
                    listener.onAlbumSaved(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.request_error));
            }
        });
    }
}

package com.shikshitha.admin.calendar;

import com.shikshitha.admin.App;
import com.shikshitha.admin.R;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.api.AdminApi;
import com.shikshitha.admin.model.Evnt;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 31-07-2017.
 */

class EventInteractorImpl implements EventInteractor {
    @Override
    public void getEvents(long schoolId, final OnFinishedListener listener) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<List<Evnt>> classList = api.getEvents(schoolId);
        classList.enqueue(new Callback<List<Evnt>>() {
            @Override
            public void onResponse(Call<List<Evnt>> call, Response<List<Evnt>> response) {
                if(response.isSuccessful()) {
                    listener.onEventsReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Evnt>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}

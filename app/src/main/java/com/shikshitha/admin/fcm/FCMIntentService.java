package com.shikshitha.admin.fcm;

import android.app.IntentService;
import android.content.Intent;

import com.shikshitha.admin.App;
import com.shikshitha.admin.api.AdminApi;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.util.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FCMIntentService extends IntentService {

    public FCMIntentService() {
        super("FCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<Void> classList = api.updateFcmToken(SharedPreferenceUtil.getAuthorization(getApplicationContext()));
        classList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    SharedPreferenceUtil.fcmTokenSaved(App.getInstance());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

}

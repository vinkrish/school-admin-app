package com.shikshitha.admin.util;

import android.app.IntentService;
import android.content.Intent;

import com.shikshitha.admin.App;
import com.shikshitha.admin.BuildConfig;
import com.shikshitha.admin.api.AdminApi;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.model.AppVersion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VersionIntentService extends IntentService {

    public VersionIntentService() {
        super("VersionIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

        Call<AppVersion> queue = api.getAppVersion(BuildConfig.VERSION_CODE, "admin");
        queue.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                if(response.isSuccessful()) {
                    SharedPreferenceUtil.saveAppVersion(App.getInstance(), response.body());
                }
            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
            }
        });
    }

}

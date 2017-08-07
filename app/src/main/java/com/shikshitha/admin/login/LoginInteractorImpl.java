package com.shikshitha.admin.login;

import com.shikshitha.admin.App;
import com.shikshitha.admin.R;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.api.AdminApi;
import com.shikshitha.admin.model.Credentials;
import com.shikshitha.admin.model.TeacherCredentials;
import com.shikshitha.admin.util.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 28-03-2017.
 */

class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void login(final Credentials credentials, final OnLoginFinishedListener listener) {
        AdminApi authApi = ApiClient.getClient().create(AdminApi.class);

        Call<TeacherCredentials> login = authApi.login(credentials);
        login.enqueue(new Callback<TeacherCredentials>() {
            @Override
            public void onResponse(Call<TeacherCredentials> call, Response<TeacherCredentials> response) {
                if(response.isSuccessful()) {
                    SharedPreferenceUtil.saveAuthorizedUser(App.getInstance(), credentials.getUsername());
                    listener.onSuccess(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<TeacherCredentials> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void recoverPwd(String username, final OnLoginFinishedListener listener) {
        AdminApi authApi = ApiClient.getClient().create(AdminApi.class);

        Call<Void> sendNewPwd = authApi.forgotPassword(username);
        sendNewPwd.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    listener.onPwdRecovered();
                } else {
                    //APIError error = ErrorUtils.parseError(response);
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}

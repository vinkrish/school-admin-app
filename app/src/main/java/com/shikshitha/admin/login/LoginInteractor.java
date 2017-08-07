package com.shikshitha.admin.login;

import com.shikshitha.admin.model.Credentials;
import com.shikshitha.admin.model.TeacherCredentials;

/**
 * Created by Vinay on 28-03-2017.
 */

interface LoginInteractor {
    interface OnLoginFinishedListener{

        void onSuccess(TeacherCredentials credentials);

        void onPwdRecovered();

        void onNoUser();

        void onError(String message);
    }

    void login(Credentials credentials, OnLoginFinishedListener listener);

    void recoverPwd(String username, OnLoginFinishedListener listener);
}

package com.shikshitha.admin.login;

import com.shikshitha.admin.model.TeacherCredentials;

/**
 * Created by Vinay on 28-03-2017.
 */

interface LoginView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void pwdRecovered();

    void noUser();

    void saveUser(TeacherCredentials teacherCredentials);

    void navigateToDashboard();
}

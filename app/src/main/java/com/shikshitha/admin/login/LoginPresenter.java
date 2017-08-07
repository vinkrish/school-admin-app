package com.shikshitha.admin.login;

import com.shikshitha.admin.model.Credentials;

/**
 * Created by Vinay on 28-03-2017.
 */

interface LoginPresenter {

    void validateCredentials(Credentials credentials);

    void pwdRecovery(String username);

    void onDestroy();
}

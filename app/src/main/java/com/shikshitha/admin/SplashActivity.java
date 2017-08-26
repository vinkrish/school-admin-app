package com.shikshitha.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.group.GroupActivity;
import com.shikshitha.admin.login.LoginActivity;
import com.shikshitha.admin.model.TeacherCredentials;
import com.shikshitha.admin.fcm.FCMIntentService;
import com.shikshitha.admin.util.AppGlobal;
import com.shikshitha.admin.util.SharedPreferenceUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppGlobal.setSqlDbHelper(getApplicationContext());

        TeacherCredentials credentials = SharedPreferenceUtil.getTeacher(this);
        if(!credentials.getMobileNo().equals("") && !SharedPreferenceUtil.isFcmTokenSaved(this)) {
            startService(new Intent(this, FCMIntentService.class));
        }

        if (TeacherDao.getTeacher().getId() == 0) {
            SharedPreferenceUtil.logout(this);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (credentials.getAuthToken().equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, GroupActivity.class));
            finish();
        }

    }
}

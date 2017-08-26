package com.shikshitha.admin.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.shikshitha.admin.model.Authorization;
import com.shikshitha.admin.model.TeacherCredentials;

public class SharedPreferenceUtil {

    public static void saveTeacher(Context context, TeacherCredentials credentials) {
        SharedPreferences sharedPref = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("mobileNo", credentials.getMobileNo());
        editor.putString("authToken", credentials.getAuthToken());
        editor.putLong("schoolId", credentials.getSchoolId());
        editor.putString("schoolName", credentials.getSchoolName());
        editor.apply();
    }

    public static TeacherCredentials getTeacher(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        TeacherCredentials response = new TeacherCredentials();
        response.setMobileNo(sharedPref.getString("mobileNo", ""));
        response.setAuthToken(sharedPref.getString("authToken", ""));
        response.setSchoolId(sharedPref.getLong("schoolId", 0));
        response.setSchoolName(sharedPref.getString("schoolName", ""));
        return response;
    }

    public static void logout(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("mobileNo", "");
        editor.putString("authToken", "");
        editor.putLong("schoolId", 0);
        editor.apply();
    }

    public static void saveFcmToken(Context context, String fcmToken) {
        SharedPreferences sharedPref = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fcmToken", fcmToken);
        editor.putBoolean("isSaved", false);
        editor.apply();
    }

    public static Authorization getAuthorization(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        Authorization authorization = new Authorization();
        authorization.setFcmToken(sharedPref.getString("fcmToken", ""));
        authorization.setUser(sharedPref.getString("user", ""));
        return authorization;
    }

    public static void saveAuthorizedUser(Context context, String user) {
        SharedPreferences sharedPref = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user", user);
        editor.apply();
    }

    public static boolean isFcmTokenSaved(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("isSaved", false);
    }

    public static void fcmTokenSaved(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isSaved", true);
        editor.apply();
    }

}

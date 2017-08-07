package com.shikshitha.admin.util;

import com.shikshitha.admin.App;

public class Constants {

    /*
     * You should replace these values with your own. See the README for details
     * on what to fill in.
     */
    public static final String COGNITO_POOL_ID = "us-east-1:a121f030-1a47-495f-a634-734a52c40adb";

    /*
     * Region of your Cognito identity pool ID.
     */
    public static final String COGNITO_POOL_REGION = "us-east-1";

    /*
     * Note, you must first create a bucket using the S3 console before running
     * the sample (https://console.aws.amazon.com/s3/). After creating a bucket,
     * put it's name in the field below.
     */
    public static final String BUCKET_NAME = "shikshitha-images/" + SharedPreferenceUtil.getTeacher(App.getInstance()).getSchoolId();

    /*
     * Region of your bucket.
     */
    public static final String BUCKET_REGION = "ap-south-1";
}

package com.shikshitha.admin.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vinay.
 */
public class SqlDbHelper extends SQLiteOpenHelper implements SqlConstant {
    private static SqlDbHelper dbHelper;
    public SQLiteDatabase sqliteDatabase;

    private SqlDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SqlDbHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new SqlDbHelper(context.getApplicationContext());
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ATTENDANCE);
        db.execSQL(CREATE_CLASS);
        db.execSQL(CREATE_HOMEWORK);
        db.execSQL(CREATE_SCHOOL);
        db.execSQL(CREATE_SECTION);
        db.execSQL(CREATE_STUDENT);
        db.execSQL(CREATE_TEACHER);
        db.execSQL(CREATE_TIMETABLE);
        db.execSQL(CREATE_GROUPS);
        db.execSQL(CREATE_CHAT);
        db.execSQL(CREATE_MESSAGE);
        db.execSQL(CREATE_USER_GROUP);
        db.execSQL(CREATE_SERVICE);
        db.execSQL(CREATE_EVENT);
        db.execSQL(CREATE_DELETED_MESSAGE);
        db.execSQL(CREATE_DELETED_GROUP);
        db.execSQL(CREATE_ALBUM);
        db.execSQL(CREATE_ALBUM_IMAGE);
        db.execSQL(CREATE_SUB_ALBUM);
        db.execSQL(CREATE_SUBALBUM_IMAGE);
        db.execSQL(CREATE_DELETED_ALBUM);
        db.execSQL(CREATE_DELETED_ALBUM_IMAGE);
        db.execSQL(CREATE_DELETED_SUBALBUM);
        db.execSQL(CREATE_DELETED_SUBALBUM_IMAGE);
        db.execSQL(CREATE_IMAGE_STATUS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS attendance");
        db.execSQL("DROP TABLE IF EXISTS class");
        db.execSQL("DROP TABLE IF EXISTS homework");
        db.execSQL("DROP TABLE IF EXISTS school");
        db.execSQL("DROP TABLE IF EXISTS section");
        db.execSQL("DROP TABLE IF EXISTS student");
        db.execSQL("DROP TABLE IF EXISTS teacher");
        db.execSQL("DROP TABLE IF EXISTS timetable");
        db.execSQL("DROP TABLE IF EXISTS groups");
        db.execSQL("DROP TABLE IF EXISTS chat");
        db.execSQL("DROP TABLE IF EXISTS message");
        db.execSQL("DROP TABLE IF EXISTS user_group");
        db.execSQL("DROP TABLE IF EXISTS service");
        db.execSQL("DROP TABLE IF EXISTS event");
        db.execSQL("DROP TABLE IF EXISTS deleted_message");
        db.execSQL("DROP TABLE IF EXISTS deleted_group");
        db.execSQL("DROP TABLE IF EXISTS album");
        db.execSQL("DROP TABLE IF EXISTS album_image");
        db.execSQL("DROP TABLE IF EXISTS sub_album");
        db.execSQL("DROP TABLE IF EXISTS subalbum_image");
        db.execSQL("DROP TABLE IF EXISTS deleted_album");
        db.execSQL("DROP TABLE IF EXISTS deleted_album_image");
        db.execSQL("DROP TABLE IF EXISTS deleted_subalbum");
        db.execSQL("DROP TABLE IF EXISTS deleted_subalbum_image");
        db.execSQL("DROP TABLE IF EXISTS image_status");
        onCreate(db);
    }

    public void deleteTables() {
        sqliteDatabase = dbHelper.getWritableDatabase();
        sqliteDatabase.delete("attendance", null, null);
        sqliteDatabase.delete("class", null, null);
        sqliteDatabase.delete("homework", null, null);
        sqliteDatabase.delete("school", null, null);
        sqliteDatabase.delete("section", null, null);
        sqliteDatabase.delete("student", null, null);
        sqliteDatabase.delete("teacher", null, null);
        sqliteDatabase.delete("timetable", null, null);
        sqliteDatabase.delete("groups", null, null);
        sqliteDatabase.delete("chat", null, null);
        sqliteDatabase.delete("message", null, null);
        sqliteDatabase.delete("user_group", null, null);
        sqliteDatabase.delete("service", null, null);
        sqliteDatabase.delete("event", null, null);
        sqliteDatabase.delete("deleted_message", null, null);
        sqliteDatabase.delete("deleted_group", null, null);
        sqliteDatabase.delete("album", null, null);
        sqliteDatabase.delete("sub_album", null, null);
        sqliteDatabase.delete("album_image", null, null);
        sqliteDatabase.delete("subalbum_image", null, null);
        sqliteDatabase.delete("deleted_album", null, null);
        sqliteDatabase.delete("deleted_subalbum", null, null);
        sqliteDatabase.delete("deleted_album_image", null, null);
        sqliteDatabase.delete("deleted_subalbum_image", null, null);
        sqliteDatabase.delete("image_status", null, null);
    }
}

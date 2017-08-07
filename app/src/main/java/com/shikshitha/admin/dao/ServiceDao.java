package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.Service;
import com.shikshitha.admin.util.AppGlobal;

/**
 * Created by Vinay on 17-04-2017.
 */

public class ServiceDao {
    public static int insert(Service service) {
        String sql = "insert into service(Id, SchoolId, IsMessage, IsSms, IsChat, " +
                "IsAttendance, IsAttendanceSms, IsHomework, IsHomeworkSms, isTimetable) " +
                "values(?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.bindLong(1, service.getId());
            stmt.bindLong(2, service.getSchoolId());
            stmt.bindString(3, Boolean.toString(service.getIsMessage()));
            stmt.bindString(4, Boolean.toString(service.getIsSms()));
            stmt.bindString(5, Boolean.toString(service.getIsChat()));
            stmt.bindString(6, Boolean.toString(service.getIsAttendance()));
            stmt.bindString(7, Boolean.toString(service.getIsAttendanceSms()));
            stmt.bindString(8, Boolean.toString(service.getIsHomework()));
            stmt.bindString(9, Boolean.toString(service.getIsHomeworkSms()));
            stmt.bindString(10, Boolean.toString(service.getIsTimetable()));
            stmt.executeInsert();
            stmt.clearBindings();
        } catch (Exception e) {
            db.endTransaction();
            return 0;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return 1;
    }

    public static Service getServices() {
        Service service = new Service();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from service", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            service.setId(c.getLong(c.getColumnIndex("Id")));
            service.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            service.setIsMessage(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsMessage"))));
            service.setIsSms(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsSms"))));
            service.setIsChat(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsChat"))));
            service.setIsAttendance(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsAttendance"))));
            service.setIsAttendanceSms(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsAttendanceSms"))));
            service.setIsHomework(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsHomework"))));
            service.setIsHomeworkSms(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsHomeworkSms"))));
            service.setIsTimetable(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsTimetable"))));
            c.moveToNext();
        }
        c.close();
        return service;
    }

    public static int clear() {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from service");
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }
}

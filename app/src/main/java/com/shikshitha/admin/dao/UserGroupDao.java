package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.UserGroup;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 21-05-2017.
 */

public class UserGroupDao {

    public static int insert(List<UserGroup> userGroups) {
        String sql = "insert into user_group(Id, UserId, Name, Role, GroupId, IsActive) " +
                "values(?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(UserGroup userGroup: userGroups) {
                stmt.bindLong(1, userGroup.getId());
                stmt.bindLong(2, userGroup.getUserId());
                stmt.bindString(3, userGroup.getName());
                stmt.bindString(4, userGroup.getRole());
                stmt.bindLong(5, userGroup.getGroupId());
                stmt.bindString(6, Boolean.toString(userGroup.isActive()));
                stmt.executeInsert();
                stmt.clearBindings();
            }
        } catch (Exception e) {
            db.endTransaction();
            return 0;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return 1;
    }

    public static List<UserGroup> getUserGroups(long groupId) {
        List<UserGroup> userGroups = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from user_group where GroupId = " + groupId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            UserGroup userGroup = new UserGroup();
            userGroup.setId(c.getLong(c.getColumnIndex("Id")));
            userGroup.setUserId(c.getLong(c.getColumnIndex("UserId")));
            userGroup.setName(c.getString(c.getColumnIndex("Name")));
            userGroup.setRole(c.getString(c.getColumnIndex("Role")));
            userGroup.setGroupId(c.getLong(c.getColumnIndex("GroupId")));
            userGroup.setActive(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsActive"))));
            userGroups.add(userGroup);
            c.moveToNext();
        }
        c.close();
        return userGroups;
    }

    public static int clear(long groupId) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from user_group where GroupId = " + groupId);
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }
}

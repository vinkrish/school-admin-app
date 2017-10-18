package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.DeletedGroup;
import com.shikshitha.admin.util.AppGlobal;

import java.util.List;

/**
 * Created by Vinay on 17-10-2017.
 */

public class DeletedGroupDao {

    public static DeletedGroup getNewestDeletedGroup() {
        DeletedGroup deletedGroup = new DeletedGroup();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        String query = "select * from deleted_group order by Id desc limit 1";
        Cursor c = sqliteDatabase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            deletedGroup.setId(c.getLong(c.getColumnIndex("Id")));
            deletedGroup.setSenderId(c.getLong(c.getColumnIndex("SenderId")));
            deletedGroup.setGroupId(c.getLong(c.getColumnIndex("GroupId")));
            deletedGroup.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            deletedGroup.setDeletedAt(c.getLong(c.getColumnIndex("DeletedAt")));
            c.moveToNext();
        }
        c.close();
        return deletedGroup;
    }

    public static int insertDeletedGroups(List<DeletedGroup> groups) {
        deleteGroup(groups);
        String sql = "insert into deleted_group(Id, SenderId, GroupId, SchoolId, DeletedAt) " +
                "values(?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(DeletedGroup group: groups) {
                stmt.bindLong(1, group.getId());
                stmt.bindLong(2, group.getSenderId());
                stmt.bindLong(3, group.getGroupId());
                stmt.bindLong(4, group.getSchoolId());
                stmt.bindLong(5, group.getDeletedAt());
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

    private static void deleteGroup(List<DeletedGroup> groups) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        for(DeletedGroup group: groups) {
            try {
                sqliteDb.execSQL("delete from groups where Id = " + group.getGroupId());
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

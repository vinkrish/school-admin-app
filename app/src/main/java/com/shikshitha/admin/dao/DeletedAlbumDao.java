package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.DeletedAlbum;
import com.shikshitha.admin.util.AppGlobal;

import java.util.List;

/**
 * Created by Vinay on 28-10-2017.
 */

public class DeletedAlbumDao {

    public static DeletedAlbum getNewestDeletedAlbum() {
        DeletedAlbum deletedAlbum = new DeletedAlbum();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        String query = "select * from deleted_album order by Id desc limit 1";
        Cursor c = sqliteDatabase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            deletedAlbum.setId(c.getLong(c.getColumnIndex("Id")));
            deletedAlbum.setSenderId(c.getLong(c.getColumnIndex("SenderId")));
            deletedAlbum.setAlbumId(c.getLong(c.getColumnIndex("AlbumId")));
            deletedAlbum.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            deletedAlbum.setDeletedAt(c.getLong(c.getColumnIndex("DeletedAt")));
            c.moveToNext();
        }
        c.close();
        return deletedAlbum;
    }

    public static int insertDeletedAlbums(List<DeletedAlbum> deletedAlbums) {
        deleteAlbum(deletedAlbums);
        String sql = "insert into deleted_album(Id, SenderId, GroupId, SchoolId, DeletedAt) " +
                "values(?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(DeletedAlbum deletedAlbum: deletedAlbums) {
                stmt.bindLong(1, deletedAlbum.getId());
                stmt.bindLong(2, deletedAlbum.getSenderId());
                stmt.bindLong(3, deletedAlbum.getAlbumId());
                stmt.bindLong(4, deletedAlbum.getSchoolId());
                stmt.bindLong(5, deletedAlbum.getDeletedAt());
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

    private static void deleteAlbum(List<DeletedAlbum> deletedAlbums) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        for(DeletedAlbum deletedAlbum: deletedAlbums) {
            try {
                sqliteDb.execSQL("delete from album where Id = " + deletedAlbum.getAlbumId());
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

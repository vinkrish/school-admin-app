package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.DeletedSubAlbum;
import com.shikshitha.admin.util.AppGlobal;

import java.util.List;

/**
 * Created by Vinay on 28-10-2017.
 */

public class DeletedSubAlbumDao {

    public static DeletedSubAlbum getNewestDeletedSubAlbum() {
        DeletedSubAlbum deletedSubAlbum = new DeletedSubAlbum();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        String query = "select * from deleted_subalbum order by Id desc limit 1";
        Cursor c = sqliteDatabase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            deletedSubAlbum.setId(c.getLong(c.getColumnIndex("Id")));
            deletedSubAlbum.setSenderId(c.getLong(c.getColumnIndex("SenderId")));
            deletedSubAlbum.setAlbumId(c.getLong(c.getColumnIndex("AlbumId")));
            deletedSubAlbum.setSubAlbumId(c.getLong(c.getColumnIndex("SubAlbumId")));
            deletedSubAlbum.setDeletedAt(c.getLong(c.getColumnIndex("DeletedAt")));
            c.moveToNext();
        }
        c.close();
        return deletedSubAlbum;
    }

    public static int insertDeletedSubAlbums(List<DeletedSubAlbum> deletedSubAlbums) {
        deleteSubAlbum(deletedSubAlbums);
        String sql = "insert into deleted_subalbum(Id, SenderId, AlbumId, SubAlbumId, DeletedAt) " +
                "values(?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(DeletedSubAlbum deletedSubAlbum: deletedSubAlbums) {
                stmt.bindLong(1, deletedSubAlbum.getId());
                stmt.bindLong(2, deletedSubAlbum.getSenderId());
                stmt.bindLong(3, deletedSubAlbum.getAlbumId());
                stmt.bindLong(4, deletedSubAlbum.getSubAlbumId());
                stmt.bindLong(5, deletedSubAlbum.getDeletedAt());
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

    private static void deleteSubAlbum(List<DeletedSubAlbum> deletedSubAlbums) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        for(DeletedSubAlbum deletedSubAlbum: deletedSubAlbums) {
            try {
                sqliteDb.execSQL("delete from sub_album where Id = " + deletedSubAlbum.getAlbumId());
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

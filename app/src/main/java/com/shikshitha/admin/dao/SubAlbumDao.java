package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.SubAlbum;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 28-10-2017.
 */

public class SubAlbumDao {

    public static int insert(List<SubAlbum> subAlbums) {
        String sql = "insert into sub_album(Id, Name, CoverPic, AlbumId, CreatedBy, CreatorName, CreatorRole, CreatedAt) " +
                "values(?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(SubAlbum subAlbum: subAlbums) {
                stmt.bindLong(1, subAlbum.getId());
                stmt.bindString(2, subAlbum.getName());
                stmt.bindString(3, subAlbum.getCoverPic());
                stmt.bindLong(4, subAlbum.getAlbumId());
                stmt.bindLong(5, subAlbum.getCreatedBy());
                stmt.bindString(6, subAlbum.getCreatorName());
                stmt.bindString(7, subAlbum.getCreatorRole());
                stmt.bindLong(8, subAlbum.getCreatedAt());
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

    public static List<SubAlbum> getSubAlbums(long albumId) {
        List<SubAlbum> subAlbums = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from sub_album where AlbumId = " + albumId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            SubAlbum subAlbum = new SubAlbum();
            subAlbum.setId(c.getLong(c.getColumnIndex("Id")));
            subAlbum.setName(c.getString(c.getColumnIndex("Name")));
            subAlbum.setCoverPic(c.getString(c.getColumnIndex("CoverPic")));
            subAlbum.setAlbumId(c.getLong(c.getColumnIndex("AlbumId")));
            subAlbum.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
            subAlbum.setCreatorName(c.getString(c.getColumnIndex("CreatorName")));
            subAlbum.setCreatorRole(c.getString(c.getColumnIndex("CreatorRole")));
            subAlbum.setCreatedAt(c.getLong(c.getColumnIndex("CreatedAt")));
            subAlbums.add(subAlbum);
            c.moveToNext();
        }
        c.close();
        return subAlbums;
    }

}

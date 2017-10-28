package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 28-10-2017.
 */

public class AlbumDao {

    public static int insert(List<Album> albums) {
        String sql = "insert into album(Id, Name, CreatedBy, CreatorName, CreatorRole, CreatedAt, SchoolId) " +
                "values(?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(Album album: albums) {
                stmt.bindLong(1, album.getId());
                stmt.bindString(2, album.getName());
                stmt.bindLong(3, album.getCreatedBy());
                stmt.bindString(4, album.getCreatorName());
                stmt.bindString(5, album.getCreatorRole());
                stmt.bindLong(6, album.getCreatedAt());
                stmt.bindLong(7, album.getSchoolId());
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

    public static List<Album> getAlbums(long schoolId) {
        List<Album> albums = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from album where SchoolId = " + schoolId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Album album = new Album();
            album.setId(c.getLong(c.getColumnIndex("Id")));
            album.setName(c.getString(c.getColumnIndex("Name")));
            album.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
            album.setCreatorName(c.getString(c.getColumnIndex("CreatorName")));
            album.setCreatorRole(c.getString(c.getColumnIndex("CreatorRole")));
            album.setCreatedAt(c.getLong(c.getColumnIndex("CreatedAt")));
            album.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            albums.add(album);
            c.moveToNext();
        }
        c.close();
        return albums;
    }

}

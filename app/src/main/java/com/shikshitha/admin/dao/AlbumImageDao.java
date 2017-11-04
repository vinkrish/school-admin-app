package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.AlbumImage;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 28-10-2017.
 */

public class AlbumImageDao {

    public static int insert(List<AlbumImage> albumImages) {
        String sql = "insert into album_image(Id, Name, AlbumId, CreatedBy, CreatorName, CreatorRole, CreatedAt) " +
                "values(?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(AlbumImage albumImage: albumImages) {
                stmt.bindLong(1, albumImage.getId());
                stmt.bindString(2, albumImage.getName());
                stmt.bindLong(3, albumImage.getAlbumId());
                stmt.bindLong(4, albumImage.getCreatedBy());
                stmt.bindString(5, albumImage.getCreatorName());
                stmt.bindString(6, albumImage.getCreatorRole());
                stmt.bindLong(7, albumImage.getCreatedAt());
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

    public static ArrayList<AlbumImage> getAlbumImages(long albumId) {
        ArrayList<AlbumImage> albumImages = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from album_image where AlbumId = " + albumId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            AlbumImage albumImage = new AlbumImage();
            albumImage.setId(c.getLong(c.getColumnIndex("Id")));
            albumImage.setName(c.getString(c.getColumnIndex("Name")));
            albumImage.setAlbumId(c.getLong(c.getColumnIndex("AlbumId")));
            albumImage.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
            albumImage.setCreatorName(c.getString(c.getColumnIndex("CreatorName")));
            albumImage.setCreatorRole(c.getString(c.getColumnIndex("CreatorRole")));
            albumImage.setCreatedAt(c.getLong(c.getColumnIndex("CreatedAt")));
            albumImages.add(albumImage);
            c.moveToNext();
        }
        c.close();
        return albumImages;
    }

}

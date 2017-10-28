package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.SubAlbumImage;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 28-10-2017.
 */

public class SubAlbumImageDao {

    public static int insert(List<SubAlbumImage> subAlbumImages) {
        String sql = "insert into subalbum_image(Id, Name, SubAlbumId, CreatedBy, CreatorName, CreatorRole, CreatedAt) " +
                "values(?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(SubAlbumImage subAlbumImage: subAlbumImages) {
                stmt.bindLong(1, subAlbumImage.getId());
                stmt.bindString(2, subAlbumImage.getName());
                stmt.bindLong(3, subAlbumImage.getSubAlbumId());
                stmt.bindLong(4, subAlbumImage.getCreatedBy());
                stmt.bindString(5, subAlbumImage.getCreatorName());
                stmt.bindString(6, subAlbumImage.getCreatorRole());
                stmt.bindLong(7, subAlbumImage.getCreatedAt());
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

    public static List<SubAlbumImage> getSubAlbumImages(long subAlbumId) {
        List<SubAlbumImage> subAlbumImages = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from subalbum_image where SubAlbumId = " + subAlbumId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            SubAlbumImage subAlbumImage = new SubAlbumImage();
            subAlbumImage.setId(c.getLong(c.getColumnIndex("Id")));
            subAlbumImage.setName(c.getString(c.getColumnIndex("Name")));
            subAlbumImage.setSubAlbumId(c.getLong(c.getColumnIndex("SubAlbumId")));
            subAlbumImage.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
            subAlbumImage.setCreatorName(c.getString(c.getColumnIndex("CreatorName")));
            subAlbumImage.setCreatorRole(c.getString(c.getColumnIndex("CreatorRole")));
            subAlbumImage.setCreatedAt(c.getLong(c.getColumnIndex("CreatedAt")));
            subAlbumImages.add(subAlbumImage);
            c.moveToNext();
        }
        c.close();
        return subAlbumImages;
    }

}

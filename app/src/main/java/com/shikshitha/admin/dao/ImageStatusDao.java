package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.AlbumImage;
import com.shikshitha.admin.model.DeletedAlbumImage;
import com.shikshitha.admin.model.ImageStatus;
import com.shikshitha.admin.model.SubAlbumImage;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 02-11-2017.
 */

public class ImageStatusDao {

    public static int insert(ImageStatus imageStatus) {
        String sql = "insert into image_status(Name, AlbumId, SubAlbumId, Sync) " +
                "values(?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.bindString(1, imageStatus.getName());
            stmt.bindLong(2, imageStatus.getAlbumId());
            stmt.bindLong(3, imageStatus.getSubAlbumId());
            stmt.bindLong(4, 0);
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

    public static int updateAlbumImages(List<AlbumImage> albumImages) {
        String sql = "update image_status set Sync = 1 where AlbumId = ? ";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(AlbumImage albumImage: albumImages) {
                stmt.bindLong(1, albumImage.getAlbumId());
                stmt.executeUpdateDelete();
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

    public static int updateSubAlbumImages(List<SubAlbumImage> subAlbumImages) {
        String sql = "update image_status set Sync = 1 where SubAlbumId = ? ";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(SubAlbumImage subAlbumImage: subAlbumImages) {
                stmt.bindLong(1, subAlbumImage.getSubAlbumId());
                stmt.executeUpdateDelete();
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

    public static ArrayList<ImageStatus> getAlbumImages(long albumId) {
        ArrayList<ImageStatus> albumImages = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from image_status where AlbumId = " + albumId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            ImageStatus imageStatus = new ImageStatus();
            imageStatus.setId(c.getLong(c.getColumnIndex("Id")));
            imageStatus.setName(c.getString(c.getColumnIndex("Name")));
            imageStatus.setAlbumId(c.getLong(c.getColumnIndex("AlbumId")));
            imageStatus.setSync(Boolean.parseBoolean(c.getString(c.getColumnIndex("Sync"))));
            albumImages.add(imageStatus);
            c.moveToNext();
        }
        c.close();
        return albumImages;
    }

    public static ArrayList<ImageStatus> getSubAlbumImages(long subAlbumId) {
        ArrayList<ImageStatus> albumImages = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from image_status where SubAlbumId = " + subAlbumId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            ImageStatus imageStatus = new ImageStatus();
            imageStatus.setId(c.getLong(c.getColumnIndex("Id")));
            imageStatus.setName(c.getString(c.getColumnIndex("Name")));
            imageStatus.setSubAlbumId(c.getLong(c.getColumnIndex("SubAlbumId")));
            imageStatus.setSync(Boolean.parseBoolean(c.getString(c.getColumnIndex("Sync"))));
            albumImages.add(imageStatus);
            c.moveToNext();
        }
        c.close();
        return albumImages;
    }

    public static ArrayList<ImageStatus> getLatestAlbumImages(long albumId) {
        ArrayList<ImageStatus> albumImages = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from image_status where Sync = 0 and AlbumId = " + albumId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            ImageStatus imageStatus = new ImageStatus();
            imageStatus.setId(c.getLong(c.getColumnIndex("Id")));
            imageStatus.setName(c.getString(c.getColumnIndex("Name")));
            imageStatus.setAlbumId(c.getLong(c.getColumnIndex("AlbumId")));
            imageStatus.setSync(Boolean.parseBoolean(c.getString(c.getColumnIndex("Sync"))));
            albumImages.add(imageStatus);
            c.moveToNext();
        }
        c.close();
        return albumImages;
    }

    public static ArrayList<ImageStatus> getLatestSubAlbumImages(long subAlbumId) {
        ArrayList<ImageStatus> albumImages = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from image_status where Sync = 0 and SubAlbumId = " + subAlbumId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            ImageStatus imageStatus = new ImageStatus();
            imageStatus.setId(c.getLong(c.getColumnIndex("Id")));
            imageStatus.setName(c.getString(c.getColumnIndex("Name")));
            imageStatus.setSubAlbumId(c.getLong(c.getColumnIndex("SubAlbumId")));
            imageStatus.setSync(Boolean.parseBoolean(c.getString(c.getColumnIndex("Sync"))));
            albumImages.add(imageStatus);
            c.moveToNext();
        }
        c.close();
        return albumImages;
    }

    public static void delete(String imageName) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from image_status where Name = '" + imageName + "'");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(ArrayList<DeletedAlbumImage> albumImages) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        for(DeletedAlbumImage deletedAlbumImage: albumImages) {
            try {
                sqliteDb.execSQL("delete from image_status where Name = '" + deletedAlbumImage.getName() + "'");
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

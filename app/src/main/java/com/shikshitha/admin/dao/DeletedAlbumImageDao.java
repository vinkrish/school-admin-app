package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.DeletedAlbumImage;
import com.shikshitha.admin.util.AppGlobal;

import java.util.List;

/**
 * Created by Vinay on 28-10-2017.
 */

public class DeletedAlbumImageDao {

    public static DeletedAlbumImage getNewestDeletedAlbumImage() {
        DeletedAlbumImage deletedAlbumImage = new DeletedAlbumImage();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        String query = "select * from deleted_album_image order by Id desc limit 1";
        Cursor c = sqliteDatabase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            deletedAlbumImage.setId(c.getLong(c.getColumnIndex("Id")));
            deletedAlbumImage.setSenderId(c.getLong(c.getColumnIndex("SenderId")));
            deletedAlbumImage.setAlbumId(c.getLong(c.getColumnIndex("AlbumId")));
            deletedAlbumImage.setAlbumImageId(c.getLong(c.getColumnIndex("AlbumImageId")));
            deletedAlbumImage.setDeletedAt(c.getLong(c.getColumnIndex("DeletedAt")));
            c.moveToNext();
        }
        c.close();
        return deletedAlbumImage;
    }

    public static int insertDeletedAlbumImages(List<DeletedAlbumImage> deletedAlbumImages) {
        deleteAlbumImage(deletedAlbumImages);
        String sql = "insert into deleted_album_image(Id, SenderId, AlbumId, AlbumImageId, DeletedAt) " +
                "values(?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(DeletedAlbumImage deletedAlbumImage: deletedAlbumImages) {
                stmt.bindLong(1, deletedAlbumImage.getId());
                stmt.bindLong(2, deletedAlbumImage.getSenderId());
                stmt.bindLong(3, deletedAlbumImage.getAlbumId());
                stmt.bindLong(4, deletedAlbumImage.getAlbumImageId());
                stmt.bindLong(5, deletedAlbumImage.getDeletedAt());
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

    private static void deleteAlbumImage(List<DeletedAlbumImage> deletedAlbumImages) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        for(DeletedAlbumImage deletedAlbumImage: deletedAlbumImages) {
            try {
                sqliteDb.execSQL("delete from album_image where Id = " + deletedAlbumImage.getAlbumImageId());
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

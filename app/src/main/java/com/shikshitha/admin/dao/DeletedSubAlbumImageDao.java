package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.DeletedSubAlbumImage;
import com.shikshitha.admin.util.AppGlobal;

import java.util.List;

/**
 * Created by Vinay on 28-10-2017.
 */

public class DeletedSubAlbumImageDao {

    public static DeletedSubAlbumImage getNewestDeletedSubAlbumImage() {
        DeletedSubAlbumImage deletedSubAlbumImage = new DeletedSubAlbumImage();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        String query = "select * from deleted_subalbum_image order by Id desc limit 1";
        Cursor c = sqliteDatabase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            deletedSubAlbumImage.setId(c.getLong(c.getColumnIndex("Id")));
            deletedSubAlbumImage.setSenderId(c.getLong(c.getColumnIndex("SenderId")));
            deletedSubAlbumImage.setSubAlbumId(c.getLong(c.getColumnIndex("SubAlbumId")));
            deletedSubAlbumImage.setSubAlbumImageId(c.getLong(c.getColumnIndex("SubAlbumImageId")));
            deletedSubAlbumImage.setDeletedAt(c.getLong(c.getColumnIndex("DeletedAt")));
            c.moveToNext();
        }
        c.close();
        return deletedSubAlbumImage;
    }

    public static int insertDeletedSubAlbumImages(List<DeletedSubAlbumImage> deletedSubAlbumImages) {
        deleteAlbumImage(deletedSubAlbumImages);
        String sql = "insert into deleted_subalbum_image(Id, SenderId, SubAlbumId, SubAlbumImageId, DeletedAt) " +
                "values(?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(DeletedSubAlbumImage deletedSubAlbumImage: deletedSubAlbumImages) {
                stmt.bindLong(1, deletedSubAlbumImage.getId());
                stmt.bindLong(2, deletedSubAlbumImage.getSenderId());
                stmt.bindLong(3, deletedSubAlbumImage.getSubAlbumId());
                stmt.bindLong(4, deletedSubAlbumImage.getSubAlbumImageId());
                stmt.bindLong(5, deletedSubAlbumImage.getDeletedAt());
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

    private static void deleteAlbumImage(List<DeletedSubAlbumImage> deletedSubAlbumImages) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        for(DeletedSubAlbumImage deletedSubAlbumImage: deletedSubAlbumImages) {
            try {
                sqliteDb.execSQL("delete from subalbum_image where Id = " + deletedSubAlbumImage.getSubAlbumImageId());
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

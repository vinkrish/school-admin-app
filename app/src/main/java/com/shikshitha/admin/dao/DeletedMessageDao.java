package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.DeletedMessage;
import com.shikshitha.admin.util.AppGlobal;

import java.util.List;

/**
 * Created by Vinay on 14-10-2017.
 */

public class DeletedMessageDao {

    public static DeletedMessage getNewestDeletedMessage(long groupId) {
        DeletedMessage message = new DeletedMessage();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        String query = "select * from deleted_message where GroupId=" + groupId + " order by Id desc limit 1";
        Cursor c = sqliteDatabase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            message.setId(c.getLong(c.getColumnIndex("Id")));
            message.setMessageId(c.getLong(c.getColumnIndex("MessageId")));
            message.setSenderId(c.getLong(c.getColumnIndex("SenderId")));
            message.setRecipientId(c.getLong(c.getColumnIndex("RecipientId")));
            message.setGroupId(c.getLong(c.getColumnIndex("GroupId")));
            message.setDeletedAt(c.getLong(c.getColumnIndex("DeletedAt")));
            c.moveToNext();
        }
        c.close();
        return message;
    }

    public static int insertDeletedMessages(List<DeletedMessage> messages) {
        deleteGroupMessages(messages);
        String sql = "insert into deleted_message(Id, MessageId, SenderId, RecipientId, GroupId, DeletedAt) " +
                "values(?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(DeletedMessage message: messages) {
                stmt.bindLong(1, message.getId());
                stmt.bindLong(2, message.getMessageId());
                stmt.bindLong(3, message.getSenderId());
                stmt.bindLong(4, message.getRecipientId());
                stmt.bindLong(5, message.getGroupId());
                stmt.bindLong(6, message.getDeletedAt());
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

    private static void deleteGroupMessages(List<DeletedMessage> messages) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        for(DeletedMessage message: messages) {
            try {
                sqliteDb.execSQL("delete from message where Id = " + message.getMessageId());
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

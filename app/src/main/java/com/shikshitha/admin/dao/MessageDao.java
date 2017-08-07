package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.Message;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 19-05-2017.
 */

public class MessageDao {

    public static int insertGroupMessages(List<Message> messages) {
        String sql = "insert into message(Id, SenderId, SenderRole, SenderName, RecipientId, " +
                "RecipientRole, GroupId, MessageType, MessageBody, ImageUrl, CreatedAt) " +
                "values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(Message message: messages) {
                stmt.bindLong(1, message.getId());
                stmt.bindLong(2, message.getSenderId());
                stmt.bindString(3, message.getSenderRole());
                stmt.bindString(4, message.getSenderName());
                stmt.bindLong(5, message.getRecipientId());
                stmt.bindString(6, message.getRecipientRole());
                stmt.bindLong(7, message.getGroupId());
                stmt.bindString(8, message.getMessageType());
                stmt.bindString(9, message.getMessageBody());
                stmt.bindString(10, message.getImageUrl());
                stmt.bindString(11, message.getCreatedAt());
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

    public static int insertChatMessages(List<Message> messages) {
        String sql = "insert into message(Id, SenderId, SenderRole, SenderName, RecipientId, " +
                "RecipientRole, GroupId, MessageType, MessageBody, ImageUrl, CreatedAt) " +
                "values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(Message message: messages) {
                stmt.bindLong(1, message.getId());
                stmt.bindLong(2, message.getSenderId());
                stmt.bindString(3, message.getSenderRole());
                stmt.bindString(4, "");
                stmt.bindLong(5, message.getRecipientId());
                stmt.bindString(6, message.getRecipientRole());
                stmt.bindLong(7, message.getGroupId());
                stmt.bindString(8, message.getMessageType());
                stmt.bindString(9, message.getMessageBody());
                stmt.bindString(10, message.getImageUrl());
                stmt.bindString(11, message.getCreatedAt());
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

    public static List<Message> getMessages(long senderId, String senderRole, long recipientId, String recipientRole) {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        String query = "select * from message where " +
                "((SenderId=" + senderId + " and SenderRole= '" + senderRole + "' and RecipientId=" + recipientId + " and RecipientRole='" + recipientRole + "') or " +
                "(SenderId=" + recipientId + " and SenderRole='" + recipientRole + "' and RecipientId=" + senderId + " and RecipientRole='" + senderRole + "')) " +
                "order by Id desc limit 100";
        Cursor c = sqliteDatabase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Message message = new Message();
            message.setId(c.getLong(c.getColumnIndex("Id")));
            message.setSenderId(c.getLong(c.getColumnIndex("SenderId")));
            message.setSenderRole(c.getString(c.getColumnIndex("SenderRole")));
            message.setSenderName(c.getString(c.getColumnIndex("SenderName")));
            message.setRecipientId(c.getLong(c.getColumnIndex("RecipientId")));
            message.setRecipientRole(c.getString(c.getColumnIndex("RecipientRole")));
            message.setGroupId(c.getLong(c.getColumnIndex("GroupId")));
            message.setMessageType(c.getString(c.getColumnIndex("MessageType")));
            message.setMessageBody(c.getString(c.getColumnIndex("MessageBody")));
            message.setImageUrl(c.getString(c.getColumnIndex("ImageUrl")));
            message.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
            messages.add(message);
            c.moveToNext();
        }
        c.close();
        return messages;
    }

    public static List<Message> getMessagesFromId(long senderId, String senderRole, long recipientId, String recipientRole, long messageId) {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        String query = "select * from message where " +
                "((SenderId=" + senderId + " and SenderRole= '" + senderRole + "' and RecipientId=" + recipientId + " and RecipientRole='" + recipientRole + "') or " +
                "(SenderId=" + recipientId + " and SenderRole='" + recipientRole + "' and RecipientId=" + senderId + " and RecipientRole='" + senderRole + "')) " +
                "and Id<" + messageId + " order by Id desc limit 100";
        Cursor c = sqliteDatabase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Message message = new Message();
            message.setId(c.getLong(c.getColumnIndex("Id")));
            message.setSenderId(c.getLong(c.getColumnIndex("SenderId")));
            message.setSenderRole(c.getString(c.getColumnIndex("SenderRole")));
            message.setSenderName(c.getString(c.getColumnIndex("SenderName")));
            message.setRecipientId(c.getLong(c.getColumnIndex("RecipientId")));
            message.setRecipientRole(c.getString(c.getColumnIndex("RecipientRole")));
            message.setGroupId(c.getLong(c.getColumnIndex("GroupId")));
            message.setMessageType(c.getString(c.getColumnIndex("MessageType")));
            message.setMessageBody(c.getString(c.getColumnIndex("MessageBody")));
            message.setImageUrl(c.getString(c.getColumnIndex("ImageUrl")));
            message.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
            messages.add(message);
            c.moveToNext();
        }
        c.close();
        return messages;
    }

    public static List<Message> getGroupMessages(long groupId) {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        String query = "select * from message where GroupId=" + groupId + " order by Id desc limit 50";
        Cursor c = sqliteDatabase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Message message = new Message();
            message.setId(c.getLong(c.getColumnIndex("Id")));
            message.setSenderId(c.getLong(c.getColumnIndex("SenderId")));
            message.setSenderRole(c.getString(c.getColumnIndex("SenderRole")));
            message.setSenderName(c.getString(c.getColumnIndex("SenderName")));
            message.setRecipientId(c.getLong(c.getColumnIndex("RecipientId")));
            message.setRecipientRole(c.getString(c.getColumnIndex("RecipientRole")));
            message.setGroupId(c.getLong(c.getColumnIndex("GroupId")));
            message.setMessageType(c.getString(c.getColumnIndex("MessageType")));
            message.setMessageBody(c.getString(c.getColumnIndex("MessageBody")));
            message.setImageUrl(c.getString(c.getColumnIndex("ImageUrl")));
            message.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
            messages.add(message);
            c.moveToNext();
        }
        c.close();
        return messages;
    }

    public static List<Message> getGroupMessagesFromId(long groupId, long messageId) {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        String query = "select * from message where GroupId=" + groupId + " and Id<" + messageId + " order by Id desc limit 50";
        Cursor c = sqliteDatabase.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Message message = new Message();
            message.setId(c.getLong(c.getColumnIndex("Id")));
            message.setSenderId(c.getLong(c.getColumnIndex("SenderId")));
            message.setSenderRole(c.getString(c.getColumnIndex("SenderRole")));
            message.setSenderName(c.getString(c.getColumnIndex("SenderName")));
            message.setRecipientId(c.getLong(c.getColumnIndex("RecipientId")));
            message.setRecipientRole(c.getString(c.getColumnIndex("RecipientRole")));
            message.setGroupId(c.getLong(c.getColumnIndex("GroupId")));
            message.setMessageType(c.getString(c.getColumnIndex("MessageType")));
            message.setMessageBody(c.getString(c.getColumnIndex("MessageBody")));
            message.setImageUrl(c.getString(c.getColumnIndex("ImageUrl")));
            message.setCreatedAt(c.getString(c.getColumnIndex("CreatedAt")));
            messages.add(message);
            c.moveToNext();
        }
        c.close();
        return messages;
    }

    public static int clearGroupMessages(long groupId) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from message where GroupId = " + groupId);
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }

    public static int clearChatMessages(long senderId, String senderRole, long recipientId, String recipientRole) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            String query = "delete from message where " +
                    "((SenderId=" + senderId + " and SenderRole= '" + senderRole + "' and RecipientId=" + recipientId + " and RecipientRole='" + recipientRole + "') or " +
                    "(SenderId=" + recipientId + " and SenderRole='" + recipientRole + "' and RecipientId=" + senderId + " and RecipientRole='" + senderRole + "')) ";
            sqliteDb.execSQL(query);
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }

}

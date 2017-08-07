package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.Chat;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 19-05-2017.
 */

public class ChatDao {

    public static int insertMany(List<Chat> chats) {
        String sql = "insert into chat(Id, StudentId, StudentName, ClassName, SectionName, teacherId, teacherName, CreatedBy, CreatorRole) " +
                "values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for(Chat chat: chats) {
                stmt.bindLong(1, chat.getId());
                stmt.bindLong(2, chat.getStudentId());
                stmt.bindString(3, chat.getStudentName());
                stmt.bindString(4, chat.getClassName());
                stmt.bindString(5, chat.getSectionName());
                stmt.bindLong(6, chat.getTeacherId());
                stmt.bindString(7, chat.getTeacherName());
                stmt.bindLong(8, chat.getCreatedBy());
                stmt.bindString(9, chat.getCreatorRole());
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

    public static Chat getChat(long id) {
        Chat chat = new Chat();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from chat where Id = " + id, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            chat.setId(c.getLong(c.getColumnIndex("Id")));
            chat.setStudentId(c.getLong(c.getColumnIndex("StudentId")));
            chat.setStudentName(c.getString(c.getColumnIndex("StudentName")));
            chat.setClassName(c.getString(c.getColumnIndex("ClassName")));
            chat.setSectionName(c.getString(c.getColumnIndex("SectionName")));
            chat.setTeacherId(c.getLong(c.getColumnIndex("TeacherId")));
            chat.setTeacherName(c.getString(c.getColumnIndex("TeachertName")));
            chat.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
            chat.setCreatorRole(c.getString(c.getColumnIndex("CreatorRole")));
            c.moveToNext();
        }
        c.close();
        return chat;
    }

    public static List<Chat> getChats() {
        List<Chat> chats = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from chat", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Chat chat = new Chat();
            chat.setId(c.getLong(c.getColumnIndex("Id")));
            chat.setStudentId(c.getLong(c.getColumnIndex("StudentId")));
            chat.setStudentName(c.getString(c.getColumnIndex("StudentName")));
            chat.setClassName(c.getString(c.getColumnIndex("ClassName")));
            chat.setSectionName(c.getString(c.getColumnIndex("SectionName")));
            chat.setTeacherId(c.getLong(c.getColumnIndex("TeacherId")));
            chat.setTeacherName(c.getString(c.getColumnIndex("TeacherName")));
            chat.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
            chat.setCreatorRole(c.getString(c.getColumnIndex("CreatorRole")));
            chats.add(chat);
            c.moveToNext();
        }
        c.close();
        return chats;
    }

    public static int clear() {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from chat");
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }
}

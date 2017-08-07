package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.Homework;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 15-06-2017.
 */

public class HomeworkDao {

    public static int insert(List<Homework> homeworks) {
        String sql = "insert into homework(Id, SectionId, SubjectId, SubjectName, HomeworkMessage, HomeworkDate) " +
                "values(?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for (Homework homework : homeworks) {
                stmt.bindLong(1, homework.getId());
                stmt.bindLong(2, homework.getSectionId());
                stmt.bindLong(3, homework.getSubjectId());
                stmt.bindString(4, homework.getSubjectName());
                stmt.bindString(5, homework.getHomeworkMessage());
                stmt.bindString(6, homework.getHomeworkDate());
                stmt.execute();
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

    public static List<Homework> getHomework(long sectionId, String date) {
        List<Homework> homeworkList = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from homework where SectionId = " + sectionId
                + " and HomeworkDate = '" + date + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Homework homework = new Homework();
            homework.setId(c.getLong(c.getColumnIndex("Id")));
            homework.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            homework.setSubjectId(c.getLong(c.getColumnIndex("SubjectId")));
            homework.setSubjectName(c.getString(c.getColumnIndex("SubjectName")));
            homework.setHomeworkMessage(c.getString(c.getColumnIndex("HomeworkMessage")));
            homework.setHomeworkDate(c.getString(c.getColumnIndex("HomeworkDate")));
            homeworkList.add(homework);
            c.moveToNext();
        }
        c.close();
        return homeworkList;
    }

    public static String getLastHomeworkDate(long sectionId) {
        String date = "";
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("SELECT HomeworkDate FROM homework WHERE SectionId = " +
                sectionId + " ORDER BY HomeworkDate DESC LIMIT 1", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            date = c.getString(c.getColumnIndex("HomeworkDate"));
            c.moveToNext();
        }
        c.close();
        return date;
    }

    public static int delete(long sectionId, String homeworkDate) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from homework where SectionId = " + sectionId +
                    " and HomeworkDate = '" + homeworkDate + "'");
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }

}

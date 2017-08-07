package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.Timetable;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 14-06-2017.
 */

public class TimetableDao {
    public static int insert(List<Timetable> timetableList) {
        String sql = "insert into timetable(Id, SectionId, DayOfWeek, PeriodNo, SubjectId, SubjectName, TeacherName, TimingFrom, TimingTo) " +
                "values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for (Timetable timetable : timetableList) {
                stmt.bindLong(1, timetable.getId());
                stmt.bindLong(2, timetable.getSectionId());
                stmt.bindString(3, timetable.getDayOfWeek());
                stmt.bindLong(4, timetable.getPeriodNo());
                stmt.bindLong(5, timetable.getSubjectId());
                stmt.bindString(6, timetable.getSubjectName());
                stmt.bindString(7, timetable.getTeacherName());
                stmt.bindString(8, timetable.getTimingFrom());
                stmt.bindString(9, timetable.getTimingTo());
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

    public static List<Timetable> getTimetable(long sectionId) {
        List<Timetable> timetableList = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from timetable where SectionId = " + sectionId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Timetable timetable = new Timetable();
            timetable.setId(c.getLong(c.getColumnIndex("Id")));
            timetable.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            timetable.setDayOfWeek(c.getString(c.getColumnIndex("DayOfWeek")));
            timetable.setPeriodNo(c.getInt(c.getColumnIndex("PeriodNo")));
            timetable.setSubjectId(c.getLong(c.getColumnIndex("SubjectId")));
            timetable.setSubjectName(c.getString(c.getColumnIndex("SubjectName")));
            timetable.setTeacherName(c.getString(c.getColumnIndex("TeacherName")));
            timetable.setTimingFrom(c.getString(c.getColumnIndex("TimingFrom")));
            timetable.setTimingTo(c.getString(c.getColumnIndex("TimingTo")));
            timetableList.add(timetable);
            c.moveToNext();
        }
        c.close();
        return timetableList;
    }

    public static List<Timetable> getDayTimetable(long sectionId, String day) {
        List<Timetable> timetableList = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from timetable where " +
                "SectionId = " + sectionId + " and DayOfWeek = '" + day + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Timetable timetable = new Timetable();
            timetable.setId(c.getLong(c.getColumnIndex("Id")));
            timetable.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            timetable.setDayOfWeek(c.getString(c.getColumnIndex("DayOfWeek")));
            timetable.setPeriodNo(c.getInt(c.getColumnIndex("PeriodNo")));
            timetable.setSubjectId(c.getLong(c.getColumnIndex("SubjectId")));
            timetable.setSubjectName(c.getString(c.getColumnIndex("SubjectName")));
            timetable.setTeacherName(c.getString(c.getColumnIndex("TeacherName")));
            timetable.setTimingFrom(c.getString(c.getColumnIndex("TimingFrom")));
            timetable.setTimingTo(c.getString(c.getColumnIndex("TimingTo")));
            timetableList.add(timetable);
            c.moveToNext();
        }
        c.close();
        return timetableList;
    }

    public static int delete(long sectionId) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from timetable where SectionId = " + sectionId);
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }
}

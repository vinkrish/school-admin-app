package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.TeacherTimetable;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 14-06-2017.
 */

public class TeacherTimetableDao {
    public static int insert(long teacherId, List<TeacherTimetable> timetableList) {
        String sql = "insert into teacher_timetable(Id, SectionId, ClassName, SectionName, DayOfWeek, " +
                "PeriodNo, SubjectId, SubjectName, TeacherId, TeacherName, TimingFrom, TimingTo) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for (TeacherTimetable timetable : timetableList) {
                stmt.bindLong(1, timetable.getId());
                stmt.bindLong(2, timetable.getSectionId());
                stmt.bindString(3, timetable.getClassName());
                stmt.bindString(4, timetable.getSectionName());
                stmt.bindString(5, timetable.getDayOfWeek());
                stmt.bindLong(6, timetable.getPeriodNo());
                stmt.bindLong(7, timetable.getSubjectId());
                stmt.bindString(8, timetable.getSubjectName());
                stmt.bindLong(9, teacherId);
                stmt.bindString(10, timetable.getTeacherName());
                stmt.bindString(11, timetable.getTimingFrom());
                stmt.bindString(12, timetable.getTimingTo());
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

    public static List<TeacherTimetable> getTimetable(long teacherId) {
        List<TeacherTimetable> timetableList = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from teacher_timetable where TeacherId = " + teacherId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            TeacherTimetable timetable = new TeacherTimetable();
            timetable.setId(c.getLong(c.getColumnIndex("Id")));
            timetable.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            timetable.setClassName(c.getString(c.getColumnIndex("ClassName")));
            timetable.setSectionName(c.getString(c.getColumnIndex("SectionName")));
            timetable.setDayOfWeek(c.getString(c.getColumnIndex("DayOfWeek")));
            timetable.setPeriodNo(c.getInt(c.getColumnIndex("PeriodNo")));
            timetable.setSubjectId(c.getLong(c.getColumnIndex("SubjectId")));
            timetable.setSubjectName(c.getString(c.getColumnIndex("SubjectName")));
            timetable.setTeacherId(c.getLong(c.getColumnIndex("TeacherId")));
            timetable.setTeacherName(c.getString(c.getColumnIndex("TeacherName")));
            timetable.setTimingFrom(c.getString(c.getColumnIndex("TimingFrom")));
            timetable.setTimingTo(c.getString(c.getColumnIndex("TimingTo")));
            timetableList.add(timetable);
            c.moveToNext();
        }
        c.close();
        return timetableList;
    }

    public static int delete(long teacherId) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from teacher_timetable where TeacherId = " + teacherId);
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }
}

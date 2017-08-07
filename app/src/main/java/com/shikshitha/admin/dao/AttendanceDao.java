package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.Attendance;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 15-06-2017.
 */

public class AttendanceDao {

    public static int insert(List<Attendance> attendances) {
        String sql = "insert into attendance" +
                "(Id, SectionId, StudentId, StudentName, SubjectId, Type, Session, DateAttendance, TypeOfLeave) " +
                "values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for (Attendance attendance : attendances) {
                stmt.bindLong(1, attendance.getId());
                stmt.bindLong(2, attendance.getSectionId());
                stmt.bindLong(3, attendance.getStudentId());
                stmt.bindString(4, attendance.getStudentName());
                stmt.bindLong(5, attendance.getSubjectId());
                stmt.bindString(6, attendance.getType());
                stmt.bindLong(7, attendance.getSession());
                stmt.bindString(8, attendance.getDateAttendance());
                stmt.bindString(9, attendance.getTypeOfLeave());
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

    public static List<Attendance> getAttendance(long sectionId, String date, int session) {
        List<Attendance> attendanceList = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from attendance" +
                " where DateAttendance = '" + date + "' and SectionId = " + sectionId +
                " and Session = " + session +  " order by Session ASC", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Attendance attendance = new Attendance();
            attendance.setId(c.getLong(c.getColumnIndex("Id")));
            attendance.setSectionId(c.getLong(c.getColumnIndex("SectionId")));
            attendance.setStudentId(c.getLong(c.getColumnIndex("StudentId")));
            attendance.setStudentName(c.getString(c.getColumnIndex("StudentName")));
            attendance.setSubjectId(c.getLong(c.getColumnIndex("SubjectId")));
            attendance.setType(c.getString(c.getColumnIndex("Type")));
            attendance.setSession(c.getInt(c.getColumnIndex("Session")));
            attendance.setDateAttendance(c.getString(c.getColumnIndex("DateAttendance")));
            attendance.setTypeOfLeave(c.getString(c.getColumnIndex("TypeOfLeave")));
            attendanceList.add(attendance);
            c.moveToNext();
        }
        c.close();
        return attendanceList;
    }

    public static String getLastAttendanceDate(long sectionId) {
        String date = "";
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("SELECT DateAttendance FROM attendance WHERE SectionId = " +
                sectionId + " ORDER BY DateAttendance DESC LIMIT 1", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            date = c.getString(c.getColumnIndex("DateAttendance"));
            c.moveToNext();
        }
        c.close();
        return date;
    }

    public static int delete(long sectionId, String date, int session) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from attendance where SectionId = " + sectionId +
                    " and DateAttendance = '" + date + "' and Session = " + session);
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }

}

package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 15-06-2017.
 */

public class ClassDao {

    public static int insert(List<Clas> clasList) {
        String sql = "insert into class(Id, ClassName, SchoolId, TeacherId, AttendanceType) " +
                "values(?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for (Clas clas : clasList) {
                stmt.bindLong(1, clas.getId());
                stmt.bindString(2, clas.getClassName());
                stmt.bindLong(3, clas.getSchoolId());
                stmt.bindLong(4, clas.getTeacherId());
                stmt.bindString(5, clas.getAttendanceType());
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

    public static List<Clas> getClassList(long schoolId) {
        List<Clas> clasList = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from class where SchoolId = " + schoolId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Clas clas = new Clas();
            clas.setId(c.getLong(c.getColumnIndex("Id")));
            clas.setClassName(c.getString(c.getColumnIndex("ClassName")));
            clas.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            clas.setTeacherId(c.getLong(c.getColumnIndex("TeacherId")));
            clas.setAttendanceType(c.getString(c.getColumnIndex("AttendanceType")));
            clasList.add(clas);
            c.moveToNext();
        }
        c.close();
        return clasList;
    }

    public static int delete(long schoolId) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from class where SchoolId = " + schoolId);
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }

}

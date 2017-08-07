package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 15-06-2017.
 */

public class SectionDao {

    public static int insert(List<Section> sectionList) {
        String sql = "insert into section(Id, SectionName, ClassId, TeacherId) " +
                "values(?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for (Section section : sectionList) {
                stmt.bindLong(1, section.getId());
                stmt.bindString(2, section.getSectionName());
                stmt.bindLong(3, section.getClassId());
                stmt.bindLong(4, section.getTeacherId());
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

    public static List<Section> getSectionList(long classId) {
        List<Section> sectionList = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from section where ClassId = " + classId, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Section clas = new Section();
            clas.setId(c.getLong(c.getColumnIndex("Id")));
            clas.setSectionName(c.getString(c.getColumnIndex("SectionName")));
            clas.setClassId(c.getLong(c.getColumnIndex("ClassId")));
            clas.setTeacherId(c.getLong(c.getColumnIndex("TeacherId")));
            sectionList.add(clas);
            c.moveToNext();
        }
        c.close();
        return sectionList;
    }

    public static int delete(long classId) {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from section where ClassId = " + classId);
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }

}

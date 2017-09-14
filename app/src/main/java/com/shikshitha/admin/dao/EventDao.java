package com.shikshitha.admin.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.shikshitha.admin.model.Evnt;
import com.shikshitha.admin.util.AppGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 14-09-2017.
 */

public class EventDao {
    public static int insert(List<Evnt> eventList) {
        String sql = "insert into event(Id, SchoolId, EventTitle, EventDescription, StartDate, EndDate, " +
                "StartTime, EndTime, NoOfDays, IsContinuousDays, IsFullDayEvent, IsRecurring, " +
                "CreatedBy, CreatedDate, ParentEventId, IsSchool) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AppGlobal.getSqlDbHelper().getWritableDatabase();
        db.beginTransactionNonExclusive();
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            for (Evnt event : eventList) {
                stmt.bindLong(1, event.getId());
                stmt.bindLong(2, event.getSchoolId());
                stmt.bindString(3, event.getEventTitle());
                stmt.bindString(4, event.getEventDescription());
                stmt.bindString(5, event.getStartDate());
                stmt.bindString(6, event.getEndDate());
                stmt.bindLong(7, event.getStartTime());
                stmt.bindLong(8, event.getEndTime());
                stmt.bindLong(9, event.getNoOfDays());
                stmt.bindString(10, Boolean.toString(event.isContinuousDays()));
                stmt.bindString(11, Boolean.toString(event.isFullDayEvent()));
                stmt.bindString(12, Boolean.toString(event.isRecurring()));
                stmt.bindString(13, event.getCreatedBy());
                stmt.bindString(14, event.getCreatedDate());
                stmt.bindLong(15, event.getParentEventId());
                stmt.bindString(16, Boolean.toString(event.isSchool()));
                stmt.execute();
                stmt.clearBindings();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.endTransaction();
            return 0;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return 1;
    }

    public static List<Evnt> getEventList() {
        List<Evnt> eventList = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = AppGlobal.getSqlDbHelper().getReadableDatabase();
        Cursor c = sqliteDatabase.rawQuery("select * from event", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Evnt event = new Evnt();
            event.setId(c.getInt(c.getColumnIndex("Id")));
            event.setSchoolId(c.getLong(c.getColumnIndex("SchoolId")));
            event.setEventTitle(c.getString(c.getColumnIndex("EventTitle")));
            event.setEventDescription(c.getString(c.getColumnIndex("EventDescription")));
            event.setStartDate(c.getString(c.getColumnIndex("StartDate")));
            event.setEndDate(c.getString(c.getColumnIndex("EndDate")));
            event.setStartTime(c.getLong(c.getColumnIndex("StartTime")));
            event.setEndTime(c.getLong(c.getColumnIndex("EndTime")));
            event.setNoOfDays(c.getInt(c.getColumnIndex("NoOfDays")));
            event.setContinuousDays(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsContinuousDays"))));
            event.setFullDayEvent(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsFullDayEvent"))));
            event.setRecurring(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsRecurring"))));
            event.setCreatedBy(c.getString(c.getColumnIndex("CreatedBy")));
            event.setCreatedDate(c.getString(c.getColumnIndex("CreatedDate")));
            event.setParentEventId(c.getInt(c.getColumnIndex("ParentEventId")));
            event.setSchool(Boolean.parseBoolean(c.getString(c.getColumnIndex("IsSchool"))));
            eventList.add(event);
            c.moveToNext();
        }
        c.close();
        return eventList;
    }

    public static int clear() {
        SQLiteDatabase sqliteDb = AppGlobal.getSqlDbHelper().getWritableDatabase();
        try {
            sqliteDb.execSQL("delete from event");
        } catch(SQLException e) {
            return 0;
        }
        return 1;
    }
}

package com.shikshitha.admin.model;

/**
 * Created by Vinay on 30-07-2017.
 */

public class EventDates {
    private int id;
    private int eventId;
    private String eventDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}

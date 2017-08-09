package com.shikshitha.admin.calendar;

/**
 * Created by Vinay on 31-07-2017.
 */

interface EventPresenter {
    void getEvents(long schoolId);

    void onDestroy();
}

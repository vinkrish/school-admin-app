package com.shikshitha.admin.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by Vinay on 03-03-2017.
 */

public class DatePickerFragment extends DialogFragment {
    private int year, month, day;
    DatePickerDialog.OnDateSetListener onDateSet;

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        onDateSet = ondate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(args != null) {
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
        } else {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        return new DatePickerDialog(getActivity(), onDateSet, year, month, day);
    }
}

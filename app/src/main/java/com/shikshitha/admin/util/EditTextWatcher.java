package com.shikshitha.admin.util;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * Created by Vinay on 23-10-2016.
 */

public class EditTextWatcher implements TextWatcher {
    private View view;

    public EditTextWatcher(View view){
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.length() > 0) {
            ((TextInputLayout)view).setError(null);
            ((TextInputLayout)view).setErrorEnabled(false);
        }
    }
}

package com.shikshitha.admin.model;

/**
 * Created by Vinay on 04-04-2017.
 */

public class StudentSet extends Student {
    private boolean isSelected;

    public StudentSet(long id, int rollNo, String name) {
        super(id, rollNo, name);
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

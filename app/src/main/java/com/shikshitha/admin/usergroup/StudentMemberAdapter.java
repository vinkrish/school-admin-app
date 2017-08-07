package com.shikshitha.admin.usergroup;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.StudentSet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 04-04-2017.
 */

class StudentMemberAdapter extends RecyclerView.Adapter<StudentMemberAdapter.ViewHolder> {
    private ArrayList<StudentSet> items;

    StudentMemberAdapter(ArrayList<StudentSet> items) {
        this.items = items;
    }

    public ArrayList<StudentSet> getDataSet() {
        return items;
    }

    @UiThread
    public void setDataSet(ArrayList<StudentSet> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public StudentMemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_member_item, parent, false);
        return new StudentMemberAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StudentMemberAdapter.ViewHolder holder, int position) {
        final StudentSet studentSet = items.get(position);
        holder.name.setText(studentSet.getName());
        holder.isSelected.setChecked(studentSet.isSelected());
        holder.isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                studentSet.setSelected(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.checkBox) CheckBox isSelected;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

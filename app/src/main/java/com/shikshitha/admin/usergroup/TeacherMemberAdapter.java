package com.shikshitha.admin.usergroup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.TeacherSet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 04-04-2017.
 */

class TeacherMemberAdapter extends RecyclerView.Adapter<TeacherMemberAdapter.ViewHolder>{
    private ArrayList<TeacherSet> items;

    TeacherMemberAdapter(ArrayList<TeacherSet> items) {
        this.items = items;
    }

    public ArrayList<TeacherSet> getDataSet() {
        return items;
    }

    public void setDataSet(ArrayList<TeacherSet> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public TeacherMemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_member_item, parent, false);
        return new TeacherMemberAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TeacherMemberAdapter.ViewHolder holder, int position) {
        final TeacherSet teacherSet = items.get(position);
        holder.name.setText(teacherSet.getName());
        holder.isSelected.setSelected(teacherSet.isSelected());
        holder.isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                teacherSet.setSelected(b);
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

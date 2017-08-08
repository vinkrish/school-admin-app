package com.shikshitha.admin.attendance;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.Attendance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 23-04-2017.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder>{
    private Context mContext;
    private List<Attendance> absentees;
    private List<Attendance> selected_absentees;

    AttendanceAdapter(Context context, List<Attendance> absentees, List<Attendance> selected_absentees) {
        this.mContext = context;
        this.absentees = absentees;
        this.selected_absentees = selected_absentees;
    }

    public List<Attendance> getDataSet() {
        return absentees;
    }

    @UiThread
    public void setDataSet(List<Attendance> absentees, List<Attendance> selected_absentees) {
        this.absentees = absentees;
        this.selected_absentees = selected_absentees;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_item, parent, false);
        return new AttendanceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(absentees.get(position));
        if(selected_absentees.contains(absentees.get(position)))
            holder.item_layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.item_layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
    }

    @Override
    public int getItemCount() {
        return absentees.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.absent) TextView absent;
        @BindView(R.id.ll_listitem) LinearLayout item_layout;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Attendance attendance) {
            if(attendance.getTypeOfLeave().equals("NA")) {
                name.setText("No Absentees");
            } else {
                name.setText(attendance.getStudentName());
                absent.setText("Absent");
            }
        }
    }
}

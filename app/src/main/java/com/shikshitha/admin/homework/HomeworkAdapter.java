package com.shikshitha.admin.homework;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.Homework;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 24-04-2017.
 */

class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder>{
    private Context mContext;
    private List<Homework> homeworks;
    private List<Homework> selected_homeworks;

    HomeworkAdapter(Context context, List<Homework> homeworks, List<Homework> selected_homeworks) {
        mContext = context;
        this.homeworks = homeworks;
        this.selected_homeworks = selected_homeworks;
    }

    public List<Homework> getDataSet() {
        return homeworks;
    }

    @UiThread
    public void setDataSet(List<Homework> homeworks, List<Homework> selected_homeworks) {
        this.homeworks = homeworks;
        this.selected_homeworks = selected_homeworks;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.homework_item, parent, false);
        return new HomeworkAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(homeworks.get(position));
    }

    @Override
    public int getItemCount() {
        return homeworks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subject_name) TextView subjectName;
        @BindView(R.id.hw_message) TextView hwMessage;
        @BindView(R.id.card_view) CardView cardView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Homework homework) {
            subjectName.setText(homework.getSubjectName());
            hwMessage.setText(homework.getHomeworkMessage());
            if(selected_homeworks.contains(homework))
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
            else
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
        }
    }
}

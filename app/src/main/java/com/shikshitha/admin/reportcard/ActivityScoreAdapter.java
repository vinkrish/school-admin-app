package com.shikshitha.admin.reportcard;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shikshitha.admin.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 19-11-2017.
 */

public class ActivityScoreAdapter extends RecyclerView.Adapter<ActivityScoreAdapter.ViewHolder> {
    private List<ActivityScore> items;

    ActivityScoreAdapter(List<ActivityScore> items) {
        this.items = items;
    }

    List<ActivityScore> getDataSet() {
        return items;
    }

    @UiThread
    void setDataSet(List<ActivityScore> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ActivityScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ActivityScoreAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ActivityScoreAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.roll_no) TextView rollNo;
        @BindView(R.id.student_name) TextView studentName;
        @BindView(R.id.score) TextView scoreTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final ActivityScore score) {
            rollNo.setText(String.format(Locale.ENGLISH, "%d", score.getRollNo()));
            studentName.setText(score.getStudentName());
            StringBuilder sbScore = new StringBuilder();
            if (String.valueOf(score.getMark()).equals("0.0")) {
                if (score.getGrade().equals("")) sbScore.append("- ");
                else sbScore.append(score.getGrade());
            } else {
                DecimalFormat format = new DecimalFormat("#.#");
                sbScore.append(String.valueOf(format.format(score.getMark())));
                if (!score.getGrade().equals("")) sbScore.append(" / ").append(score.getGrade());
            }
            scoreTv.setText(sbScore.toString());
        }
    }
}

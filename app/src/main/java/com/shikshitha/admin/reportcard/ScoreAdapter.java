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
 * Created by Vinay on 16-11-2017.
 */

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {
    private List<Mark> items;

    ScoreAdapter(List<Mark> items) {
        this.items = items;
    }

    List<Mark> getDataSet() {
        return items;
    }

    @UiThread
    void setDataSet(List<Mark> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ScoreAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ScoreAdapter.ViewHolder holder, int position) {
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

        void bind(final Mark mark) {
            rollNo.setText(String.format(Locale.ENGLISH, "%d", mark.getRollNo()));
            studentName.setText(mark.getStudentName());
            StringBuilder sbScore = new StringBuilder();
            if (String.valueOf(mark.getMark()).equals("0.0")) {
                if (mark.getGrade().equals("")) sbScore.append("- ");
                else sbScore.append(mark.getGrade());
            } else {
                DecimalFormat format = new DecimalFormat("#.#");
                sbScore.append(String.valueOf(format.format(mark.getMark())));
                if (!mark.getGrade().equals("")) sbScore.append(" / ").append(mark.getGrade());
            }
            scoreTv.setText(sbScore.toString());
        }
    }
}

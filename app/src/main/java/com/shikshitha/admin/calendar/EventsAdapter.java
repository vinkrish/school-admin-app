package com.shikshitha.admin.calendar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.Evnt;
import com.shikshitha.admin.util.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 28-04-2017.
 */

class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{
    private Context mContext;
    private List<Evnt> items;

    EventsAdapter(Context context, List<Evnt> items) {
        this.mContext = context;
        this.items = items;
    }

    @UiThread
    void setDataSet(List<Evnt> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.evnt_item, parent, false);
        return new EventsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        if(items == null) {
            return 0;
        } else return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.event_title)
        TextView eventTitle;
        @BindView(R.id.date_time)
        TextView dateTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Evnt evnt) {
            eventTitle.setText(evnt.getEventTitle());
            dateTime.setText(DateUtil.getDisplayFormattedDate(evnt.getStartDate()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayEventDialog(evnt);
                }
            });
        }
    }

    private Dialog displayEventDialog(final Evnt evnt) {
        final Dialog dialog = new Dialog(mContext, R.style.DialogFadeAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_event_item);

        TextView eventTitle = (TextView) dialog.findViewById(R.id.event_title);
        TextView startDate = (TextView) dialog.findViewById(R.id.start_date);
        ImageView closeWindow = (ImageView) dialog.findViewById(R.id.close_window);
        TextView description = (TextView) dialog.findViewById(R.id.description);

        eventTitle.setText(evnt.getEventTitle());
        startDate.setText(DateUtil.getDisplayFormattedDate(evnt.getStartDate()));
        description.setText(evnt.getEventDescription());

        closeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

        //Grab the window of the dialog, and change the width and height
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());

        //This makes the dialog take up the full width and height
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        return dialog;
    }
}

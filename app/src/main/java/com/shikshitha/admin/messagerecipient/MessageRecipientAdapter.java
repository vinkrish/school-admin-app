package com.shikshitha.admin.messagerecipient;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.MessageRecipient;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 25-08-2017.
 */

class MessageRecipientAdapter extends RecyclerView.Adapter<MessageRecipientAdapter.ViewHolder> {
    private List<MessageRecipient> items;

    ColorGenerator generator = ColorGenerator.MATERIAL;
    TextDrawable.IBuilder builder = TextDrawable.builder()
            .beginConfig()
            .withBorder(4)
            .endConfig()
            .round();

    MessageRecipientAdapter(List<MessageRecipient> items) {
        this.items = items;
    }

    @UiThread
    void setDataSet(List<MessageRecipient> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recipient_item, parent, false);
        return new MessageRecipientAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.student_name)
        TextView studentName;
        @BindView(R.id.image_view)
        ImageView studentImage;
        @BindView(R.id.read_at)
        TextView readAt;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final MessageRecipient messageRecipient) {
            int color = generator.getColor(messageRecipient.getRecipientName());
            TextDrawable drawable = builder.build(messageRecipient.getRecipientName().substring(0, 1), color);
            studentImage.setImageDrawable(drawable);
            studentName.setText(messageRecipient.getRecipientName());
            if(messageRecipient.isRead()) {
                readAt.setText(DateTimeFormat.forPattern("dd-MMM, HH:mm").print(new DateTime(messageRecipient.getReadAt())));
            } else {
                readAt.setText("-");
            }
        }

    }
}

package com.shikshitha.admin.chat;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 28-04-2017.
 */

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> messages;

    private static final int ITEM_TYPE_SENDER = 0;
    private static final int ITEM_TYPE_RECEIVER = 1;

    ChatAdapter(Context context, List<Message> messages) {
        this.mContext = context;
        this.messages = messages;
    }

    List<Message> getDataSet() {
        return messages;
    }

    @UiThread
    void setDataSet(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @UiThread
    void updateDataSet(List<Message> messages) {
        int pos = this.messages.size();
        this.messages.addAll(messages);
        notifyItemRangeInserted(pos, this.messages.size() - 1);
    }

    @UiThread
    void insertDataSet(List<Message> messages) {
        this.messages.addAll(0, messages);
        notifyItemRangeInserted(0, messages.size());
    }

    @UiThread
    void insertDataSet(Message message) {
        this.messages.add(0, message);
        notifyItemInserted(0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_SENDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sender_item, parent, false);
            return new TextHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_receiver_item, parent, false);
            return new TextHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_SENDER) {
            ((ChatAdapter.TextHolder)holder).bind(messages.get(position));
        } else if (itemType == ITEM_TYPE_RECEIVER) {
            ((ChatAdapter.TextHolder)holder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderRole().equals("principal")) {
            return ITEM_TYPE_SENDER;
        } else {
            return ITEM_TYPE_RECEIVER;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View v) {
            super(v);
        }
    }

    class TextHolder extends ChatAdapter.ViewHolder {
        @BindView(R.id.message_text) TextView messageTv;

        TextHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Message message) {
            messageTv.setText(message.getMessageBody());
        }

    }
}

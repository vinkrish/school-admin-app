package com.shikshitha.admin.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.Message;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 28-04-2017.
 */

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> messages;
    private long schoolId;

    private static final int ITEM_TYPE_SENDER = 0;
    private static final int ITEM_TYPE_RECEIVER = 1;

    ChatAdapter(Context context, List<Message> messages, long schoolId) {
        this.mContext = context;
        this.messages = messages;
        this.schoolId = schoolId;
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
            return new SenderHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_receiver_item, parent, false);
            return new ReceiverHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_SENDER) {
            ((ChatAdapter.SenderHolder)holder).bind(messages.get(position));
        } else if (itemType == ITEM_TYPE_RECEIVER) {
            ((ChatAdapter.ReceiverHolder)holder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderRole().equals("admin")) {
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

    class SenderHolder extends ChatAdapter.ViewHolder {
        @BindView(R.id.message_text) TextView messageTv;
        @BindView(R.id.shared_image) ImageView sharedImage;

        SenderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Message message) {
            if(message.getMessageType().equals("image")) {
                setSharedImage(message.getImageUrl());
            } else {
                messageTv.setText(message.getMessageBody());
            }
        }

        private void setSharedImage(String imagetUrl) {
            sharedImage.setVisibility(View.VISIBLE);
            File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Teacher/" + schoolId);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, imagetUrl);
            if (file.exists()) {
                sharedImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } else {
                Picasso.with(mContext)
                        .load("https://s3.ap-south-1.amazonaws.com/shikshitha-images/" + schoolId + "/" + imagetUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(sharedImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable) sharedImage.getDrawable()).getBitmap();
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError() {
                                sharedImage.setImageResource(R.drawable.placeholder);
                            }
                        });
            }
        }

    }

    class ReceiverHolder extends ChatAdapter.ViewHolder {
        @BindView(R.id.message_text) TextView messageTv;

        ReceiverHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Message message) {
            messageTv.setText(message.getMessageBody());
        }
    }


}

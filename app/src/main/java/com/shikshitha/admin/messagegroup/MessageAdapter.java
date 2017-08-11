package com.shikshitha.admin.messagegroup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
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
import com.shikshitha.admin.model.Message;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 07-04-2017.
 */

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> messages;
    private long schoolId;
    private final OnItemClickListener listener;

    private static final int ITEM_TYPE_TEXT = 0;
    private static final int ITEM_TYPE_IMAGE = 1;

    ColorGenerator generator = ColorGenerator.MATERIAL;
    TextDrawable.IBuilder builder = TextDrawable.builder()
            .beginConfig()
            .withBorder(4)
            .endConfig()
            .roundRect(10);

    MessageAdapter(Context context, List<Message> messages, long schoolId, OnItemClickListener listener) {
        this.mContext = context;
        this.messages = messages;
        this.schoolId = schoolId;
        this.listener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(Message message);
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
    void updateDataSet(List<Message> msgs) {
        int pos = messages.size();
        this.messages.addAll(msgs);
        notifyItemRangeInserted(pos, messages.size() - 1);
    }

    @UiThread
    void insertDataSet(Message message) {
        this.messages.add(0, message);
        notifyItemInserted(0);
    }

    @UiThread
    void insertDataSet(List<Message> messages) {
        this.messages.addAll(0, messages);
        notifyItemRangeInserted(0, messages.size());
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_TEXT) {
            View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_text_item, parent, false);
            return new TextHolder(textView);
        } else {
            View imgView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_image_item, parent, false);
            return new ImageHolder(imgView);
        }
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_TEXT) {
            ((TextHolder)holder).bind(messages.get(position));
        } else if (itemType == ITEM_TYPE_IMAGE) {
            ((ImageHolder)holder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getMessageType().equals("text")) {
            return ITEM_TYPE_TEXT;
        } else {
            return ITEM_TYPE_IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View v) {
            super(v);
        }
    }

    class TextHolder extends ViewHolder {
        @BindView(R.id.image_view) ImageView senderImage;
        @BindView(R.id.sender_name) TextView senderName;
        @BindView(R.id.created_date) TextView createdDate;
        @BindView(R.id.message) TextView messageTV;

        TextHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Message message) {
            int color = generator.getColor(message.getSenderName());
            TextDrawable drawable = builder.build(message.getSenderName().substring(0,1), color);
            senderImage.setImageDrawable(drawable);
            senderName.setText(message.getSenderName());
            DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(message.getCreatedAt());
            createdDate.setText(DateTimeFormat.forPattern("dd-MMM, HH:mm").print(dateTime));
            messageTV.setText(message.getMessageBody());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(message);
                }
            });
        }

    }

    class ImageHolder extends ViewHolder {
        @BindView(R.id.image_view) ImageView senderImage;
        @BindView(R.id.sender_name) TextView senderName;
        @BindView(R.id.created_date) TextView createdDate;
        @BindView(R.id.shared_image) ImageView sharedImage;
        @BindView(R.id.message) TextView messageTV;

        ImageHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Message message) {
            int color = generator.getColor(message.getSenderName());
            TextDrawable drawable = builder.build(message.getSenderName().substring(0,1), color);
            senderImage.setImageDrawable(drawable);
            senderName.setText(message.getSenderName());
            DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(message.getCreatedAt());
            createdDate.setText(DateTimeFormat.forPattern("dd-MMM, HH:mm").print(dateTime));
            messageTV.setText(message.getMessageBody());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(message);
                }
            });

            //sharedImage.setImageResource(R.drawable.books);
            File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Teacher/" + schoolId);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, message.getImageUrl());
            if(file.exists()) {
                /*BitmapFactory.Options bitoption = new BitmapFactory.Options();
                bitoption.inSampleSize = 4;
                sharedImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath(), bitoption));*/
                sharedImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } else {
                Picasso.with(mContext)
                        .load("https://s3.ap-south-1.amazonaws.com/shikshitha-images/" + schoolId + "/" + message.getImageUrl())
                        .placeholder(R.drawable.splash_image)
                        .into(sharedImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable)sharedImage.getDrawable()).getBitmap();
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
        }

    }

    private Dialog displayTextDialog(final Message message) {
        final Dialog dialog = new Dialog(mContext, R.style.DialogFadeAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_text_item);

        TextView sender = (TextView) dialog.findViewById(R.id.sender_name);
        TextView createdTime = (TextView) dialog.findViewById(R.id.created_date);
        ImageView closeWindow = (ImageView) dialog.findViewById(R.id.close_window);
        TextView messageText = (TextView) dialog.findViewById(R.id.message);

        sender.setText(message.getSenderName());
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(message.getCreatedAt());
        createdTime.setText(DateTimeFormat.forPattern("dd-MMM, HH:mm").print(dateTime));
        messageText.setText(message.getMessageBody());

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

    private Dialog displayImageDialog(final Message message) {
        final Dialog dialog = new Dialog(mContext, R.style.DialogFadeAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_item);

        TextView sender = (TextView) dialog.findViewById(R.id.sender_name);
        TextView createdTime = (TextView) dialog.findViewById(R.id.created_date);
        ImageView closeWindow = (ImageView) dialog.findViewById(R.id.close_window);
        TextView messageText = (TextView) dialog.findViewById(R.id.message);

        sender.setText(message.getSenderName());
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(message.getCreatedAt());
        createdTime.setText(DateTimeFormat.forPattern("dd-MMM, HH:mm").print(dateTime));
        messageText.setText(message.getMessageBody());

        ImageView fullImage = (ImageView) dialog.findViewById(R.id.full_image);
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Teacher/"+ schoolId + "/" + message.getImageUrl());
        if(file.exists()) {
            fullImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }

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

package com.shikshitha.admin.messagegroup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.shikshitha.admin.R;
import com.shikshitha.admin.model.Message;
import com.shikshitha.admin.util.YouTubeHelper;
import com.shikshitha.admin.util.YoutubeDeveloperKey;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 07-04-2017.
 */

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> messages;
    private Message selected_message;
    private long schoolId;
    private final ThumbnailListener thumbnailListener;

    private static final int ITEM_TYPE_TEXT = 0;
    private static final int ITEM_TYPE_IMAGE = 1;
    private static final int ITEM_TYPE_VIDEO = 2;
    private static final int ITEM_TYPE_BOTH = 3;

    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;

    ColorGenerator generator = ColorGenerator.MATERIAL;
    TextDrawable.IBuilder builder = TextDrawable.builder()
            .beginConfig()
            .withBorder(4)
            .endConfig()
            .roundRect(10);

    MessageAdapter(Context context, List<Message> messages, long schoolId) {
        this.mContext = context;
        this.messages = messages;
        this.schoolId = schoolId;
        thumbnailListener = new ThumbnailListener();
        thumbnailViewToLoaderMap = new HashMap<>();
    }

    void releaseLoaders() {
        for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
            loader.release();
        }
    }

    List<Message> getDataSet() {
        return messages;
    }

    @UiThread
    void setDataSet(List<Message> messages, Message selected_message) {
        this.messages = messages;
        this.selected_message = selected_message;
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

    @UiThread
    void deleteDataSet(int pos) {
        this.messages.remove(pos);
        notifyItemRemoved(pos);
    }

    @UiThread
    void selectedItemChanged(int newPosition, Message selected_message) {
        this.selected_message = selected_message;
        notifyItemChanged(newPosition);
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_TEXT) {
            View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_text_item, parent, false);
            return new TextHolder(textView);
        } else if (viewType == ITEM_TYPE_IMAGE){
            View imgView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_image_item, parent, false);
            return new ImageHolder(imgView);
        } else if (viewType == ITEM_TYPE_VIDEO){
            View imgView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_video_item, parent, false);
            return new VideoHolder(imgView);
        } else {
            View imgView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_video_image_item, parent, false);
            return new VideoImageHolder(imgView);
        }
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_TEXT) {
            ((TextHolder) holder).bind(messages.get(position));
        } else if (itemType == ITEM_TYPE_IMAGE) {
            ((ImageHolder) holder).bind(messages.get(position));
        } else if (itemType == ITEM_TYPE_VIDEO) {
            ((VideoHolder) holder).bind(messages.get(position));
        } else if (itemType == ITEM_TYPE_BOTH) {
            ((VideoImageHolder) holder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getMessageType().equals("text")) {
            return ITEM_TYPE_TEXT;
        } else if (messages.get(position).getMessageType().equals("image")){
            return ITEM_TYPE_IMAGE;
        } else if (messages.get(position).getMessageType().equals("video")){
            return ITEM_TYPE_VIDEO;
        } else {
            return ITEM_TYPE_BOTH;
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
        @BindView(R.id.card_view) RelativeLayout cardView;

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

            if (message.getId() == selected_message.getId()) {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.default_white));
            }
        }

    }

    class ImageHolder extends ViewHolder {
        @BindView(R.id.image_view) ImageView senderImage;
        @BindView(R.id.sender_name) TextView senderName;
        @BindView(R.id.created_date) TextView createdDate;
        @BindView(R.id.shared_image) ImageView sharedImage;
        @BindView(R.id.message) TextView messageTV;
        @BindView(R.id.card_view) RelativeLayout cardView;

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

            if(message.getMessageBody().equals("")) {
                messageTV.setVisibility(View.GONE);
            } else {
                messageTV.setText(message.getMessageBody());
            }

            if (message.getId() == selected_message.getId()) {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.default_white));
            }

            File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Admin/" + schoolId);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, message.getImageUrl());
            if(file.exists()) {
                sharedImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } else {
                Picasso.with(mContext)
                        .load("https://s3.ap-south-1.amazonaws.com/shikshitha-images/" + schoolId + "/" + message.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .into(sharedImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable)sharedImage.getDrawable()).getBitmap();
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

    class VideoHolder extends ViewHolder {
        @BindView(R.id.image_view) ImageView senderImage;
        @BindView(R.id.sender_name) TextView senderName;
        @BindView(R.id.created_date) TextView createdDate;
        @BindView(R.id.message) TextView messageTV;
        @BindView(R.id.thumbnail) YouTubeThumbnailView thumbnail;
        @BindView(R.id.card_view) RelativeLayout cardView;

        VideoHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Message message) {
            String videoId = "";
            int color = generator.getColor(message.getSenderName());
            TextDrawable drawable = builder.build(message.getSenderName().substring(0, 1), color);
            senderImage.setImageDrawable(drawable);
            senderName.setText(message.getSenderName());
            DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(message.getCreatedAt());
            createdDate.setText(DateTimeFormat.forPattern("dd-MMM, HH:mm").print(dateTime));

            if(message.getMessageBody().equals("")) {
                messageTV.setVisibility(View.GONE);
            } else {
                messageTV.setText(message.getMessageBody());
            }

            if (message.getId() == selected_message.getId()) {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.default_white));
            }

            if(message.getVideoUrl() != null && !message.getVideoUrl().equals("")) {
                YouTubeHelper youTubeHelper = new YouTubeHelper();
                videoId = youTubeHelper.extractVideoIdFromUrl(message.getVideoUrl());

                thumbnail.setTag(videoId);
                thumbnail.initialize(YoutubeDeveloperKey.DEVELOPER_KEY, thumbnailListener);
            }
        }

    }

    class VideoImageHolder extends ViewHolder {
        @BindView(R.id.image_view) ImageView senderImage;
        @BindView(R.id.sender_name) TextView senderName;
        @BindView(R.id.created_date) TextView createdDate;
        @BindView(R.id.thumbnail) YouTubeThumbnailView thumbnail;
        @BindView(R.id.shared_image) ImageView sharedImage;
        @BindView(R.id.message) TextView messageTV;
        @BindView(R.id.card_view) RelativeLayout cardView;

        VideoImageHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Message message) {
            String videoId = "";
            int color = generator.getColor(message.getSenderName());
            TextDrawable drawable = builder.build(message.getSenderName().substring(0, 1), color);
            senderImage.setImageDrawable(drawable);
            senderName.setText(message.getSenderName());
            DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(message.getCreatedAt());
            createdDate.setText(DateTimeFormat.forPattern("dd-MMM, HH:mm").print(dateTime));

            if(message.getMessageBody().equals("")) {
                messageTV.setVisibility(View.GONE);
            } else {
                messageTV.setText(message.getMessageBody());
            }

            if (message.getId() == selected_message.getId()) {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
            }else {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.default_white));
            }

            if(message.getVideoUrl() != null && !message.getVideoUrl().equals("")) {
                YouTubeHelper youTubeHelper = new YouTubeHelper();
                videoId = youTubeHelper.extractVideoIdFromUrl(message.getVideoUrl());

                thumbnail.setTag(videoId);
                thumbnail.initialize(YoutubeDeveloperKey.DEVELOPER_KEY, thumbnailListener);
            }

            //sharedImage.setImageResource(R.drawable.books);
            File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Admin/" + schoolId);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, message.getImageUrl());
            if (file.exists()) {
                sharedImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } else {
                Picasso.with(mContext)
                        .load("https://s3.ap-south-1.amazonaws.com/shikshitha-images/" + schoolId + "/" + message.getImageUrl())
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

    private final class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onInitializationSuccess(
                YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            loader.setOnThumbnailLoadedListener(this);
            thumbnailViewToLoaderMap.put(view, loader);
            view.setImageResource(R.drawable.loading_thumbnail);
            String videoId = (String) view.getTag();
            loader.setVideo(videoId);
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView view, YouTubeInitializationResult loader) {
            view.setImageResource(R.drawable.no_thumbnail);
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
            view.setImageResource(R.drawable.no_thumbnail);
        }
    }

}

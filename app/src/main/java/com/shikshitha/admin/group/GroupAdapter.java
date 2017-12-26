package com.shikshitha.admin.group;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.Groups;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 02-04-2017.
 */

class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private List<Groups> items;
    private final OnItemClickListener listener;

    private static final int ITEM_TYPE_TEXT = 0;
    private static final int ITEM_TYPE_IMAGE = 1;
    private static final int ITEM_TYPE_VIDEO = 2;
    private static final int ITEM_TYPE_BOTH = 3;

    ColorGenerator generator = ColorGenerator.MATERIAL;
    TextDrawable.IBuilder builder = TextDrawable.builder()
            .beginConfig()
            .withBorder(4)
            .endConfig()
            .roundRect(10);

    interface OnItemClickListener {
        void onItemClick(Groups group);
    }

    GroupAdapter(List<Groups> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    List<Groups> getDataSet() {
        return items;
    }

    @UiThread
    void replaceData(List<Groups> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @UiThread
    void updateDataSet(List<Groups> itms) {
        int pos = items.size();
        this.items.addAll(itms);
        notifyItemRangeInserted(pos, items.size() - 1);
    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_TEXT) {
            View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_text_item, parent, false);
            return new TextHolder(textView);
        } else if (viewType == ITEM_TYPE_IMAGE){
            View imgView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_image_item, parent, false);
            return new ImageHolder(imgView);
        } else if (viewType == ITEM_TYPE_VIDEO){
            View imgView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_video_item, parent, false);
            return new VideoHolder(imgView);
        } else {
            View imgView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_video_image_item, parent, false);
            return new VideoImageHolder(imgView);
        }
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_TEXT) {
            ((TextHolder) holder).bind(items.get(position), listener);
        } else if (itemType == ITEM_TYPE_IMAGE) {
            ((ImageHolder) holder).bind(items.get(position), listener);
        } else if (itemType == ITEM_TYPE_VIDEO) {
            ((VideoHolder) holder).bind(items.get(position), listener);
        } else if (itemType == ITEM_TYPE_BOTH) {
            ((VideoImageHolder) holder).bind(items.get(position), listener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getRecentMessage() != null && items.get(position).getRecentMessage().equals("both")) {
            return ITEM_TYPE_BOTH;
        } else if (items.get(position).getRecentMessage() != null && items.get(position).getRecentMessage().equals("image")){
            return ITEM_TYPE_IMAGE;
        } else if (items.get(position).getRecentMessage() != null && items.get(position).getRecentMessage().equals("video")){
            return ITEM_TYPE_VIDEO;
        } else {
            return ITEM_TYPE_TEXT;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View v) {
            super(v);
        }
    }

    class TextHolder extends ViewHolder {
        @BindView(R.id.group_name) TextView groupName;
        @BindView(R.id.image_view) ImageView groupImage;
        @BindView(R.id.message) TextView message;

        TextHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Groups group, final OnItemClickListener listener) {
            int color = generator.getColor(group.getName());
            TextDrawable drawable = builder.build(group.getName().substring(0,1), color);
            groupImage.setImageDrawable(drawable);
            groupName.setText(group.getName());
            if(group.getRecentMessage() != null) {
                message.setText(group.getRecentMessage());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(group);
                }
            });
        }
    }

    class ImageHolder extends ViewHolder {
        @BindView(R.id.group_name) TextView groupName;
        @BindView(R.id.image_view) ImageView groupImage;

        ImageHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Groups group, final OnItemClickListener listener) {
            int color = generator.getColor(group.getName());
            TextDrawable drawable = builder.build(group.getName().substring(0,1), color);
            groupImage.setImageDrawable(drawable);
            groupName.setText(group.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(group);
                }
            });
        }
    }

    class VideoHolder extends ViewHolder {
        @BindView(R.id.group_name) TextView groupName;
        @BindView(R.id.image_view) ImageView groupImage;

        VideoHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Groups group, final OnItemClickListener listener) {
            int color = generator.getColor(group.getName());
            TextDrawable drawable = builder.build(group.getName().substring(0,1), color);
            groupImage.setImageDrawable(drawable);
            groupName.setText(group.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(group);
                }
            });
        }
    }

    class VideoImageHolder extends ViewHolder {
        @BindView(R.id.group_name) TextView groupName;
        @BindView(R.id.image_view) ImageView groupImage;

        VideoImageHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Groups group, final OnItemClickListener listener) {
            int color = generator.getColor(group.getName());
            TextDrawable drawable = builder.build(group.getName().substring(0,1), color);
            groupImage.setImageDrawable(drawable);
            groupName.setText(group.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(group);
                }
            });
        }
    }
}

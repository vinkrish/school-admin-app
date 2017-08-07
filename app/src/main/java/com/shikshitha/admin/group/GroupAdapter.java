package com.shikshitha.admin.group;

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

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private List<Groups> items;
    private final OnItemClickListener listener;

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

    void replaceData(List<Groups> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.group_name) TextView groupName;
        @BindView(R.id.image_view) ImageView groupImage;

        ViewHolder(View view) {
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

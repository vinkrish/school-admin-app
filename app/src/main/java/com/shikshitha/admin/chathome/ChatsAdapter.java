package com.shikshitha.admin.chathome;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.Chat;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 28-04-2017.
 */

class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder>{
    private List<Chat> items;
    private final ChatsAdapter.OnItemClickListener listener;

    ColorGenerator generator = ColorGenerator.MATERIAL;
    TextDrawable.IBuilder builder = TextDrawable.builder()
            .beginConfig()
            .withBorder(4)
            .endConfig()
            .round();

    interface OnItemClickListener {
        void onItemClick(Chat chat);
    }

    ChatsAdapter(List<Chat> items, ChatsAdapter.OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    List<Chat> getDataSet() {
        return items;
    }

    @UiThread
    void setDataSet(List<Chat> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_item, parent, false);
        return new ChatsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Chat chat, final ChatsAdapter.OnItemClickListener listener) {
            int color = generator.getColor(chat.getStudentName());
            TextDrawable drawable = builder.build(chat.getStudentName().substring(0,1), color);
            studentImage.setImageDrawable(drawable);
            studentName.setText(chat.getStudentName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(chat);
                }
            });
        }

    }
}

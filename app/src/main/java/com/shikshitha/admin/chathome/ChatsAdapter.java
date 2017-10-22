package com.shikshitha.admin.chathome;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private Context mContext;
    private List<Chat> items;
    private Chat selectedChat;

    ColorGenerator generator = ColorGenerator.MATERIAL;
    TextDrawable.IBuilder builder = TextDrawable.builder()
            .beginConfig()
            .withBorder(4)
            .endConfig()
            .round();

    interface OnItemClickListener {
        void onItemClick(Chat chat);
    }

    ChatsAdapter(Context context, List<Chat> items) {
        this.mContext = context;
        this.items = items;
    }

    List<Chat> getDataSet() {
        return items;
    }

    @UiThread
    void setDataSet(List<Chat> items, Chat selectedChat) {
        this.items = items;
        this.selectedChat = selectedChat;
        notifyDataSetChanged();
    }

    @UiThread
    void selectedItemChanged(int newPosition, Chat selectedChat) {
        this.selectedChat = selectedChat;
        notifyItemChanged(newPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_item, parent, false);
        return new ChatsAdapter.ViewHolder(v);
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
        @BindView(R.id.student_name) TextView studentName;
        @BindView(R.id.image_view) ImageView studentImage;
        @BindView(R.id.card_view) LinearLayout cardView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Chat chat) {
            int color = generator.getColor(chat.getStudentName());
            TextDrawable drawable = builder.build(chat.getStudentName().substring(0,1), color);
            studentImage.setImageDrawable(drawable);
            studentName.setText(chat.getStudentName());

            if (chat.getId() == selectedChat.getId()) {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.default_white));
            }
        }

    }
}

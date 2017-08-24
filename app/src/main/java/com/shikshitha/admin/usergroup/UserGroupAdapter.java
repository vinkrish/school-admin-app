package com.shikshitha.admin.usergroup;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.UserGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 03-04-2017.
 */

class UserGroupAdapter extends RecyclerView.Adapter<UserGroupAdapter.ViewHolder> {
    private Context mContext;
    private List<UserGroup> users;
    private List<UserGroup> selected_users;

    UserGroupAdapter(Context context, List<UserGroup> users, List<UserGroup> selected_users) {
        this.mContext = context;
        this.users = users;
        this.selected_users = selected_users;
    }

    public List<UserGroup> getDataSet() {
        return users;
    }

    @UiThread
    public void setDataSet(List<UserGroup> users, List<UserGroup> selected_users) {
        this.users = users;
        this.selected_users = selected_users;
        notifyDataSetChanged();
    }

    @Override
    public UserGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_group_item, parent, false);
        return new UserGroupAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserGroupAdapter.ViewHolder holder, int position) {
        holder.bind(users.get(position));
        if(selected_users.contains(users.get(position)))
            holder.item_layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.item_layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.role) TextView role;
        @BindView(R.id.ll_listitem) LinearLayout item_layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final UserGroup userGroup) {
            name.setText(userGroup.getName());
            role.setText(userGroup.getRole());
        }
    }
}

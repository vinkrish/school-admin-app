package com.shikshitha.admin.chathome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.shikshitha.admin.R;
import com.shikshitha.admin.chat.ChatActivity;
import com.shikshitha.admin.dao.ChatDao;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.model.Chat;
import com.shikshitha.admin.newchat.NewChatActivity;
import com.shikshitha.admin.util.Conversion;
import com.shikshitha.admin.util.DividerItemDecoration;
import com.shikshitha.admin.util.NetworkUtil;
import com.shikshitha.admin.util.PaddedItemDecoration;
import com.shikshitha.admin.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatsActivity extends AppCompatActivity implements ChatsView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.no_chats) LinearLayout noChats;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ChatsPresenter presenter;
    private ChatsAdapter adapter;

    private Chat selectedChat = new Chat();
    private int selectedChatPosition, oldSelectedChatPosition;
    ActionMode mActionMode;
    boolean isChatSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new ChatsPresenterImpl(this, new ChatsInteractorImpl());

        setupRecyclerView();

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getChats(TeacherDao.getTeacher().getId());
            }
        });

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getChats(TeacherDao.getTeacher().getId());
        } else {
            showOfflineData();
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new PaddedItemDecoration(this, Conversion.dpToPx(68, getApplicationContext())));

        adapter = new ChatsAdapter(getApplicationContext(), new ArrayList<Chat>(0));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(isChatSelect) {
                    single_select(position);
                } else {
                    Chat chat = adapter.getDataSet().get(position);
                    Intent intent = new Intent(ChatsActivity.this, ChatActivity.class);
                    intent.putExtra("recipientId", chat.getStudentId());
                    intent.putExtra("recipientName", chat.getStudentName());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isChatSelect) {
                    selectedChat = new Chat();
                    isChatSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                    single_select(position);
                }
            }
        }));
    }

    private void showOfflineData() {
        List<Chat> chats = ChatDao.getChats();
        if(chats.size() == 0) {
            noChats.setVisibility(View.VISIBLE);
        } else {
            noChats.setVisibility(View.INVISIBLE);
            adapter.setDataSet(chats, selectedChat);
        }
    }

    public void newChat(View view) {
        if (NetworkUtil.isNetworkAvailable(this)) {
            startActivity(new Intent(ChatsActivity.this, NewChatActivity.class));
            finish();
        } else {
            showSnackbar("You are offline,check your internet.");
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        showSnackbar(message);
        showOfflineData();
    }

    @Override
    public void setChats(List<Chat> chats) {
        if(chats.size() == 0) {
            ChatDao.clear();
            noChats.setVisibility(View.VISIBLE);
        } else {
            noChats.setVisibility(View.INVISIBLE);
            adapter.setDataSet(chats, selectedChat);
            backupChats(chats);
        }
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onChatDeleted() {
        recreate();
    }

    private void backupChats(final List<Chat> chats) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatDao.clear();
                ChatDao.insertMany(chats);
            }
        }).start();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.delete_overflow, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatsActivity.this);
                    alertDialog.setMessage("Are you sure you want to delete?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectedChat = new Chat();
                            mActionMode.finish();
                            presenter.deleteChat(adapter.getDataSet().get(selectedChatPosition).getId());
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isChatSelect = false;
            selectedChat = new Chat();
            adapter.selectedItemChanged(selectedChatPosition, selectedChat);
        }
    };

    public void single_select(int position) {
        selectedChatPosition = position;
        if (mActionMode != null) {
            if(selectedChat.getId() == adapter.getDataSet().get(position).getId() ) {
                selectedChat = new Chat();
                mActionMode.finish();
                adapter.selectedItemChanged(position, selectedChat);
            } else {
                selectedChat = new Chat();
                adapter.selectedItemChanged(oldSelectedChatPosition, selectedChat);
                oldSelectedChatPosition = position;
                selectedChat = adapter.getDataSet().get(position);
                adapter.selectedItemChanged(position, selectedChat);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

}

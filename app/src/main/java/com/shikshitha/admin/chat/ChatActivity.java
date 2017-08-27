package com.shikshitha.admin.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.shikshitha.admin.App;
import com.shikshitha.admin.R;
import com.shikshitha.admin.dao.MessageDao;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.model.Message;
import com.shikshitha.admin.model.MessageEvent;
import com.shikshitha.admin.util.EndlessRecyclerViewScrollListener;
import com.shikshitha.admin.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements ChatView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.no_chats) LinearLayout noChats;
    @BindView(R.id.new_msg) EditText newMsg;
    @BindView(R.id.enter_msg) ImageView enterMsg;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private long recipientId;
    private String recipientName;
    private ChatPresenter presenter;
    private ChatAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipientId = getIntent().getLongExtra("recipientId", 0);
            recipientName = extras.getString("recipientName", "");
        }
        getSupportActionBar().setTitle(recipientName);

        presenter = new ChatPresenterImpl(this, new ChatInteractorImpl());

        setupRecyclerView();

        newMsg.addTextChangedListener(newMsgWatcher);

        showOfflineData();
    }

    private void showOfflineData() {
        List<Message> messages = MessageDao.getMessages(TeacherDao.getTeacher().getId(), "principal", recipientId, "student");
        if(messages.size() == 0) {
            noChats.setVisibility(View.VISIBLE);
        } else {
            noChats.setVisibility(View.INVISIBLE);
            adapter.setDataSet(messages);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        App.activityResumed();
        if(NetworkUtil.isNetworkAvailable(this)){
            if(adapter.getItemCount() == 0) {
                presenter.getMessages("admin", TeacherDao.getTeacher().getId(), "student", recipientId);
            } else {
                presenter.getRecentMessages("admin", TeacherDao.getTeacher().getId(), "student", recipientId,
                        adapter.getDataSet().get(0).getId());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.activityPaused();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ChatAdapter(this, new ArrayList<Message>(0));
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(NetworkUtil.isNetworkAvailable(ChatActivity.this)) {
                    presenter.getFollowupMessages("admin", TeacherDao.getTeacher().getId(), "student", recipientId,
                            adapter.getDataSet().get(adapter.getDataSet().size()-1).getId());
                } else {
                    List<Message> messages = MessageDao.getMessagesFromId(TeacherDao.getTeacher().getId(), "principal",
                            recipientId, "student",
                            adapter.getDataSet().get(adapter.getDataSet().size()-1).getId());
                    adapter.updateDataSet(messages);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollToPosition(0);
                        }
                    }, 100);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(NetworkUtil.isNetworkAvailable(this) && event.senderId == recipientId){
            presenter.getRecentMessages("admin", TeacherDao.getTeacher().getId(), "student", recipientId,
                    adapter.getDataSet().get(0).getId());
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        showSnackbar(message);
    }

    @Override
    public void onMessageSaved(Message message) {
        noChats.setVisibility(View.INVISIBLE);
        adapter.insertDataSet(message);
        recyclerView.smoothScrollToPosition(0);
        backupChats(Collections.singletonList(message));
    }

    @Override
    public void showRecentMessages(List<Message> messages) {
        adapter.insertDataSet(messages);
        recyclerView.smoothScrollToPosition(0);
        backupChats(messages);
    }

    @Override
    public void showMessages(List<Message> messages) {
        if(messages.size() == 0) {
            noChats.setVisibility(View.VISIBLE);
        } else {
            noChats.setVisibility(View.INVISIBLE);
            adapter.setDataSet(messages);
            backupChats(messages);
        }
    }

    private void backupChats(final List<Message> messages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //MessageDao.clearChatMessages(TeacherDao.getTeacher().getId(), "teacher", recipientId, "student");
                MessageDao.insertChatMessages(messages);
            }
        }).start();
    }

    @Override
    public void showFollowupMessages(List<Message> messages) {
        adapter.updateDataSet(messages);
        backupChats(messages);
    }

    public void newMsgSendListener (View view) {
        sendMessage("text", "");
        newMsg.setText("");
    }

    private void sendMessage(String messageType, String imgUrl) {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        if(newMsg.getText().toString().trim().isEmpty()) {
            showSnackbar("Please enter message");
        } else {
            if (NetworkUtil.isNetworkAvailable(this)) {
                Message message = new Message();
                message.setSenderId(TeacherDao.getTeacher().getId());
                message.setSenderName(TeacherDao.getTeacher().getName());
                message.setSenderRole("admin");
                message.setRecipientId(recipientId);
                message.setRecipientRole("student");
                message.setGroupId(0);
                message.setMessageType(messageType);
                message.setImageUrl(imgUrl);
                message.setVideoUrl("");
                message.setMessageBody(newMsg.getText().toString());
                message.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()));
                presenter.saveMessage(message);
            } else {
                showSnackbar("You are offline,check your internet.");
            }
        }
    }

    private final TextWatcher newMsgWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (newMsg.getText().toString().equals("")) {
            } else {
                enterMsg.setImageResource(R.drawable.ic_send_black);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0){
                enterMsg.setImageResource(R.drawable.ic_send_black);
            }else{
                enterMsg.setImageResource(R.drawable.ic_send_white);
            }
        }
    };

}

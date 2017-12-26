package com.shikshitha.admin.messagerecipient;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.shikshitha.admin.R;
import com.shikshitha.admin.dao.GroupDao;
import com.shikshitha.admin.model.Message;
import com.shikshitha.admin.model.MessageRecipient;
import com.shikshitha.admin.util.Conversion;
import com.shikshitha.admin.util.DividerItemDecoration;
import com.shikshitha.admin.util.EndlessRecyclerViewScrollListener;
import com.shikshitha.admin.util.PaddedItemDecoration;
import com.shikshitha.admin.util.SharedPreferenceUtil;
import com.shikshitha.admin.util.YouTubeHelper;
import com.shikshitha.admin.util.YoutubeDeveloperKey;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageRecipientActivity extends AppCompatActivity implements MessageRecipientView{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.read_recycler_view) RecyclerView readRecyclerView;
    @BindView(R.id.delivered_recycler_view) RecyclerView deliveredRecyclerView;
    @BindView(R.id.empty_read) TextView emptyRead;
    @BindView(R.id.empty_delivered) TextView emptyDelivered;
    @BindView(R.id.image_view) ImageView senderImage;
    @BindView(R.id.sender_name) TextView senderName;
    @BindView(R.id.created_date) TextView createdDate;
    @BindView(R.id.message) TextView messageTV;
    @BindView(R.id.shared_image_1) ImageView sharedImage1;
    @BindView(R.id.shared_image_2) ImageView sharedImage2;
    @BindView(R.id.video_thumbnail_1) YouTubeThumbnailView thumbnail1;
    @BindView(R.id.video_thumbnail_2) YouTubeThumbnailView thumbnail2;
    @BindView(R.id.video_layout) FrameLayout videoLayout;
    @BindView(R.id.img_video_layout) LinearLayout imageVideoLayout;
    @BindView(R.id.nestedScrollView) NestedScrollView nestedScrollView;
    @BindView(R.id.delivered_layout) LinearLayout deliveredLayout;

    MessageRecipientAdapter readAdapter;
    MessageRecipientAdapter deliveredAdapter;

    ColorGenerator generator = ColorGenerator.MATERIAL;
    TextDrawable.IBuilder builder = TextDrawable.builder()
            .beginConfig()
            .withBorder(4)
            .endConfig()
            .roundRect(10);

    private Message message;
    private long schoolId;
    private String videoId;

    private final ThumbnailListener thumbnailListener = new ThumbnailListener();;

    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap = new HashMap<>();

    private MessageRecipientPresenter presenter;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_recipient);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        schoolId = SharedPreferenceUtil.getTeacher(this).getSchoolId();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = (Message) extras.getSerializable("message");
        }

        presenter = new MessageRecipientPresenterImpl(this, new MessageRecipientInteractorImpl());
        if(GroupDao.getGroup(message.getGroupId()).isSchool()) {
            presenter.getSchoolRecipient(message.getGroupId(), message.getId());
        } else {
            presenter.getMessageRecipient(message.getGroupId(), message.getId());
        }

        showMessage();

        setupRecyclerView();
    }

    private void showMessage() {
        int color = generator.getColor(message.getSenderName());
        TextDrawable drawable = builder.build(message.getSenderName().substring(0, 1), color);
        senderImage.setImageDrawable(drawable);
        senderName.setText(message.getSenderName());

        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(message.getCreatedAt());
        createdDate.setText(DateTimeFormat.forPattern("dd-MMM, HH:mm").print(dateTime));

        if(!message.getMessageBody().equals("")){
            messageTV.setVisibility(View.VISIBLE);
            messageTV.setText(message.getMessageBody());
        }

        File dir = new File(Environment.getExternalStorageDirectory().getPath(),
                "Shikshitha/Admin/" + schoolId);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, message.getImageUrl());

        switch (message.getMessageType()) {
            case "image":
                sharedImage1.setVisibility(View.VISIBLE);
                if (file.exists()) {
                    sharedImage1.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }
                break;
            case "video":
                videoLayout.setVisibility(View.VISIBLE);
                if(message.getVideoUrl() != null && !message.getVideoUrl().equals("")) {
                    YouTubeHelper youTubeHelper = new YouTubeHelper();
                    videoId = youTubeHelper.extractVideoIdFromUrl(message.getVideoUrl());

                    thumbnail1.setTag(videoId);
                    thumbnail1.initialize(YoutubeDeveloperKey.DEVELOPER_KEY, thumbnailListener);
                }
                break;
            case "both":
                imageVideoLayout.setVisibility(View.VISIBLE);
                sharedImage2.setVisibility(View.VISIBLE);
                if (file.exists()) {
                    sharedImage2.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }
                if(message.getVideoUrl() != null && !message.getVideoUrl().equals("")) {
                    YouTubeHelper youTubeHelper = new YouTubeHelper();
                    videoId = youTubeHelper.extractVideoIdFromUrl(message.getVideoUrl());

                    thumbnail2.setTag(videoId);
                    thumbnail2.initialize(YoutubeDeveloperKey.DEVELOPER_KEY, thumbnailListener);
                }
                break;
            default:

                break;
        }
    }

    private void setupRecyclerView() {
        readRecyclerView.setNestedScrollingEnabled(false);
        readRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        readRecyclerView.setItemAnimator(new DefaultItemAnimator());
        readRecyclerView.addItemDecoration(new PaddedItemDecoration(this, Conversion.dpToPx(68, getApplicationContext())));
        readAdapter = new MessageRecipientAdapter(new ArrayList<MessageRecipient>(0));
        readRecyclerView.setAdapter(readAdapter);

        deliveredRecyclerView.setNestedScrollingEnabled(false);
        deliveredRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deliveredRecyclerView.setItemAnimator(new DefaultItemAnimator());
        deliveredRecyclerView.addItemDecoration(new PaddedItemDecoration(this, Conversion.dpToPx(68, getApplicationContext())));
        deliveredAdapter = new MessageRecipientAdapter(new ArrayList<MessageRecipient>(0));
        deliveredRecyclerView.setAdapter(deliveredAdapter);

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(GroupDao.getGroup(message.getGroupId()).isSchool()) {
                    presenter.getSchoolRecipient(message.getGroupId(), message.getId());
                } else {
                    presenter.getMessageRecipient(message.getGroupId(), message.getId());
                }

            }
        });

        if(GroupDao.getGroup(message.getGroupId()).isSchool()) {
            deliveredLayout.setVisibility(View.GONE);
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(v.getChildAt(v.getChildCount() - 1) != null) {
                        if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                                scrollY > oldScrollY) {
                            presenter.getSchoolRecipientFromId(message.getGroupId(), message.getId(),
                                    readAdapter.getDataSet().get(readAdapter.getDataSet().size() - 1).getId());
                        }
                    }
                }
            });
        }

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
        refreshLayout.setRefreshing(false);
        showSnackbar(message);
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessageRecipient(List<MessageRecipient> messageRecipient) {
        List<MessageRecipient> readMessages = new ArrayList<>();
        List<MessageRecipient> deliveredMessages = new ArrayList<>();
        for(MessageRecipient mr: messageRecipient) {
            if(mr.isRead()) {
                readMessages.add(mr);
            } else {
                deliveredMessages.add(mr);
            }
        }
        readAdapter.setDataSet(readMessages);
        deliveredAdapter.setDataSet(deliveredMessages);

        if(readMessages.size() == 0){
            emptyRead.setVisibility(View.VISIBLE);
        } else {
            emptyRead.setVisibility(View.GONE);
        }

        if(deliveredMessages.size() == 0) {
            emptyDelivered.setVisibility(View.VISIBLE);
        } else {
            emptyDelivered.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSchoolRecipient(List<MessageRecipient> messageRecipient) {
        readAdapter.setDataSet(messageRecipient);

        if(messageRecipient.size() == 0){
            emptyRead.setVisibility(View.VISIBLE);
        } else {
            emptyRead.setVisibility(View.GONE);
        }

        emptyDelivered.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFollowUpRecipient(List<MessageRecipient> messageRecipient) {
        readAdapter.updateDataSet(messageRecipient);
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

    void releaseLoaders() {
        for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
            loader.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseLoaders();
    }

}

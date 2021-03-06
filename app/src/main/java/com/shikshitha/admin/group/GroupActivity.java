package com.shikshitha.admin.group;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.attendance.AttendanceActivity;
import com.shikshitha.admin.calendar.CalendarActivity;
import com.shikshitha.admin.chathome.ChatsActivity;
import com.shikshitha.admin.dao.DeletedGroupDao;
import com.shikshitha.admin.dao.GroupDao;
import com.shikshitha.admin.dao.ServiceDao;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.gallery.GalleryActivity;
import com.shikshitha.admin.homework.HomeworkActivity;
import com.shikshitha.admin.login.LoginActivity;
import com.shikshitha.admin.messagegroup.MessageActivity;
import com.shikshitha.admin.model.DeletedGroup;
import com.shikshitha.admin.model.Groups;
import com.shikshitha.admin.model.Service;
import com.shikshitha.admin.model.Teacher;
import com.shikshitha.admin.newgroup.NewGroupActivity;
import com.shikshitha.admin.reportcard.ReportActivity;
import com.shikshitha.admin.settings.SettingsActivity;
import com.shikshitha.admin.sqlite.SqlDbHelper;
import com.shikshitha.admin.timetable.TimetableActivity;
import com.shikshitha.admin.util.Conversion;
import com.shikshitha.admin.util.DividerItemDecoration;
import com.shikshitha.admin.util.NetworkUtil;
import com.shikshitha.admin.util.PaddedItemDecoration;
import com.shikshitha.admin.util.PermissionUtil;
import com.shikshitha.admin.util.SharedPreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity extends AppCompatActivity implements GroupView{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.noGroups) LinearLayout noGroups;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private GroupPresenter presenter;
    private GroupAdapter adapter;
    private Teacher teacher;

    final static int REQ_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setupDrawerContent(navigationView);

        teacher = TeacherDao.getTeacher();

        toolbar.setTitle(teacher.getName());
        toolbar.setSubtitle("Admin");
        setSupportActionBar(toolbar);

        presenter = new GroupPresenterImpl(this, new GroupInteractorImpl());

        ActionBarDrawerToggle actionBarDrawerToggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setProfile();

        hideDrawerItem();

        setupRecyclerView();

        loadOfflineData();

        if(NetworkUtil.isNetworkAvailable(this)) {
            if(adapter.getDataSet().size() == 0) {
                presenter.getGroups(teacher.getSchoolId());
            } else {
                presenter.getGroupsAboveId(teacher.getSchoolId(), adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
            }
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new PaddedItemDecoration(this, Conversion.dpToPx(70, getApplicationContext())));

        adapter = new GroupAdapter(new ArrayList<Groups>(0), mItemListener);
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(adapter.getDataSet().size() == 0) {
                    presenter.getGroups(teacher.getSchoolId());
                } else {
                    presenter.getGroupsAboveId(teacher.getSchoolId(), adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
                }
            }
        });
    }

    private void loadOfflineData() {
        List<Groups> groups = GroupDao.getGroups();
        if(groups.size() == 0) {
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
            adapter.replaceData(groups);
        }
    }

    private void setProfile() {
        View hView = navigationView.inflateHeaderView(R.layout.header);
        final ImageView imageView = hView.findViewById(R.id.user_image);
        TextView tv = hView.findViewById(R.id.name);
        tv.setText(teacher.getName());

        if(PermissionUtil.getStoragePermissionStatus(this)) {
            File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Admin/" + teacher.getSchoolId());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, teacher.getImage());
            if(file.exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } else {
                Picasso.with(this)
                        .load("https://s3.ap-south-1.amazonaws.com/shikshitha-images/" + teacher.getSchoolId() + "/" + teacher.getImage())
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError() {
                                imageView.setImageResource(R.drawable.ic_account_black);
                            }
                        });
            }
        }
    }

    protected boolean isNavDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void hideDrawerItem() {
        Menu menu = navigationView.getMenu();
        Service service = ServiceDao.getServices();
        if (!service.isAttendance()) menu.findItem(R.id.attendance_item).setVisible(false);
        if (!service.isHomework()) menu.findItem(R.id.homework_item).setVisible(false);
        if (!service.isTimetable()) menu.findItem(R.id.timetable_item).setVisible(false);
        if (!service.isReport())menu.findItem(R.id.result_item).setVisible(false);
        if (!service.isGallery())menu.findItem(R.id.gallery_item).setVisible(false);
        if (!service.isChat()) menu.findItem(R.id.chat_item).setVisible(false);
    }

    public void addGroup(View view) {
        if (NetworkUtil.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, NewGroupActivity.class);
            startActivityForResult(intent, REQ_CODE);
            overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        } else {
            showSnackbar("You are offline,check your internet.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if(adapter.getDataSet().size() == 0) {
                presenter.getGroups(teacher.getId());
            } else {
                presenter.getGroupsAboveId(teacher.getId(), adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
            }
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
        loadOfflineData();
    }

    @Override
    public void setRecentGroups(List<Groups> groups) {
        adapter.updateDataSet(groups);
        backupGroups(groups);
        syncDeletedGroups();
    }

    @Override
    public void setGroups(List<Groups> groups) {
        if(groups.size() == 0) {
            recyclerView.invalidate();
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
            adapter.replaceData(groups);
            backupGroups(groups);
        }
        syncDeletedGroups();
    }

    private void backupGroups(final List<Groups> groups) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GroupDao.insertMany(groups);
            }
        }).start();
    }

    private void syncDeletedGroups() {
        DeletedGroup deletedGroup = DeletedGroupDao.getNewestDeletedGroup();
        if(deletedGroup.getId() == 0) {
            presenter.getDeletedGroups(teacher.getSchoolId());
        } else {
            presenter.getRecentDeletedGroups(teacher.getSchoolId(), deletedGroup.getId());
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard_item:
                        menuItem.setChecked(true);
                        startActivity(new Intent(GroupActivity.this, GroupActivity.class));
                        finish();
                        break;
                    case R.id.attendance_item:
                        menuItem.setChecked(true);
                        startActivity(new Intent(GroupActivity.this, AttendanceActivity.class));
                        finish();
                        break;
                    case R.id.homework_item:
                        menuItem.setChecked(true);
                        startActivity(new Intent(GroupActivity.this, HomeworkActivity.class));
                        finish();
                        break;
                    case R.id.timetable_item:
                        menuItem.setChecked(true);
                        startActivity(new Intent(GroupActivity.this, TimetableActivity.class));
                        finish();
                        break;
                    case R.id.event_item:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(GroupActivity.this, CalendarActivity.class));
                        break;
                    case R.id.result_item:
                        menuItem.setChecked(true);
                        startActivity(new Intent(GroupActivity.this, ReportActivity.class));
                        finish();
                        break;
                    case R.id.gallery_item:
                        menuItem.setChecked(true);
                        startActivity(new Intent(GroupActivity.this, GalleryActivity.class));
                        finish();
                        break;
                    case R.id.chat_item:
                        menuItem.setChecked(true);
                        startActivity(new Intent(GroupActivity.this, ChatsActivity.class));
                        finish();
                        break;
                    case R.id.settings_item:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(GroupActivity.this, SettingsActivity.class));
                        break;
                    case R.id.logout_item:
                        logout();
                        break;
                }
                overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
                return false;
            }
        });
    }

    private void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GroupActivity.this);
        alertDialog.setMessage("Are you sure you want to logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferenceUtil.logout(GroupActivity.this);
                SqlDbHelper.getInstance(GroupActivity.this).deleteTables();
                startActivity(new Intent(GroupActivity.this, LoginActivity.class));
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    GroupAdapter.OnItemClickListener mItemListener = new GroupAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Groups group) {
            Intent intent = new Intent(GroupActivity.this, MessageActivity.class);
            Bundle args = new Bundle();
            if(group != null){
                args.putSerializable("group", group);
            }
            intent.putExtras(args);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        }
    }

}

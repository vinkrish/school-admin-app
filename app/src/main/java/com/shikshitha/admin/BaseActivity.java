package com.shikshitha.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shikshitha.admin.attendance.AttendanceActivity;
import com.shikshitha.admin.calendar.CalendarActivity;
import com.shikshitha.admin.chathome.ChatsActivity;
import com.shikshitha.admin.dao.ServiceDao;
import com.shikshitha.admin.gallery.GalleryActivity;
import com.shikshitha.admin.group.GroupActivity;
import com.shikshitha.admin.homework.HomeworkActivity;
import com.shikshitha.admin.login.LoginActivity;
import com.shikshitha.admin.model.Service;
import com.shikshitha.admin.model.Teacher;
import com.shikshitha.admin.reportcard.ReportActivity;
import com.shikshitha.admin.sqlite.SqlDbHelper;
import com.shikshitha.admin.timetable.TimetableActivity;
import com.shikshitha.admin.util.PermissionUtil;
import com.shikshitha.admin.util.SharedPreferenceUtil;

import java.io.File;

public class BaseActivity extends AppCompatActivity {
    public NavigationView navigationView;
    public DrawerLayout drawerLayout;
    public Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResId) {
        drawerLayout = (DrawerLayout)getLayoutInflater().inflate(R.layout.activity_base, null);
        navigationView = drawerLayout.findViewById(R.id.navigation_view);
        toolbar = drawerLayout.findViewById(R.id.toolbar);
        FrameLayout activityContainer = drawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(drawerLayout);

        setupDrawerContent(navigationView);

        setUpDrawerToggle();

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        hideDrawerItem();
    }

    private void setUpDrawerToggle() {
        actionBarDrawerToggle = new
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
    }

    protected void setNavigationItem(int position) {
        navigationView.getMenu().getItem(position).setChecked(true);
    }

    protected void setProfile(Teacher teacher) {
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
        if(!service.isAttendance()) menu.findItem(R.id.attendance_item).setVisible(false);
        if(!service.isHomework()) menu.findItem(R.id.homework_item).setVisible(false);
        if(!service.isReport())menu.findItem(R.id.result_item).setVisible(false);
        if(!service.isChat()) menu.findItem(R.id.chat_item).setVisible(false);
        if (!service.isTimetable()) menu.findItem(R.id.timetable_item).setVisible(false);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard_item:
                        startActivity(new Intent(BaseActivity.this, GroupActivity.class));
                        finish();
                        break;
                    case R.id.attendance_item:
                        startActivity(new Intent(BaseActivity.this, AttendanceActivity.class));
                        finish();
                        break;
                    case R.id.homework_item:
                        startActivity(new Intent(BaseActivity.this, HomeworkActivity.class));
                        finish();
                        break;
                    case R.id.timetable_item:
                        startActivity(new Intent(BaseActivity.this, TimetableActivity.class));
                        finish();
                        break;
                    case R.id.event_item:
                        startActivity(new Intent(BaseActivity.this, CalendarActivity.class));
                        break;
                    case R.id.result_item:
                        startActivity(new Intent(BaseActivity.this, ReportActivity.class));
                        finish();
                        break;
                    case R.id.gallery_item:
                        startActivity(new Intent(BaseActivity.this, GalleryActivity.class));
                        finish();
                        break;
                    case R.id.chat_item:
                        startActivity(new Intent(BaseActivity.this, ChatsActivity.class));
                        finish();
                        break;
                    case R.id.logout_item:
                        logout();
                        break;
                }
                overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        setUpDrawerToggle();
        actionBarDrawerToggle.syncState();
    }

    private void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);
        alertDialog.setMessage("Are you sure you want to logout?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferenceUtil.logout(BaseActivity.this);
                SqlDbHelper.getInstance(BaseActivity.this).deleteTables();
                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
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

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        }
    }
}

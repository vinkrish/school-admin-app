package com.shikshitha.admin.settings;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.shikshitha.admin.R;
import com.shikshitha.admin.api.AdminApi;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.dao.ServiceDao;
import com.shikshitha.admin.model.Service;
import com.shikshitha.admin.util.NetworkUtil;
import com.shikshitha.admin.util.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.notifications_switch) SwitchCompat notificationsSwitch;
    @BindView(R.id.attendance_switch) SwitchCompat attendanceSwitch;
    @BindView(R.id.homework_switch) SwitchCompat homeworkSwitch;
    @BindView(R.id.timetable_switch) SwitchCompat timetableSwitch;
    @BindView(R.id.result_switch) SwitchCompat resultSwitch;
    @BindView(R.id.gallery_switch) SwitchCompat gallerySwitch;
    @BindView(R.id.chat_switch) SwitchCompat chatSwitch;

    private boolean isRefreshing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        syncServiceSettings();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notificationsSwitch.setChecked(SharedPreferenceUtil.isNotifiable(getApplicationContext()));

        notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferenceUtil.setNotifiable(getApplicationContext(), b);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefreshing) {
                    syncServiceSettings();
                }
            }
        });
    }

    private void syncServiceSettings() {
        if(NetworkUtil.isNetworkAvailable(this)) {
            isRefreshing = true;
            refreshLayout.setRefreshing(true);
            AdminApi api = ApiClient.getAuthorizedClient().create(AdminApi.class);

            Call<Service> queue = api.getService(SharedPreferenceUtil.getTeacher(getApplicationContext()).getSchoolId());
            queue.enqueue(new Callback<Service>() {
                @Override
                public void onResponse(Call<Service> call, Response<Service> response) {
                    if(response.isSuccessful()) {
                        ServiceDao.update(response.body());
                        updateService(response.body());
                    }
                    isRefreshing = false;
                    refreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<Service> call, Throwable t) {
                    isRefreshing = false;
                    refreshLayout.setRefreshing(false);
                }
            });
        } else {
            updateService(ServiceDao.getServices());
        }
    }

    private void updateService(Service service) {
        attendanceSwitch.setChecked(service.isAttendance());
        homeworkSwitch.setChecked(service.isHomework());
        timetableSwitch.setChecked(service.isTimetable());
        resultSwitch.setChecked(service.isReport());
        gallerySwitch.setChecked(service.isGallery());
        chatSwitch.setChecked(service.isChat());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
}

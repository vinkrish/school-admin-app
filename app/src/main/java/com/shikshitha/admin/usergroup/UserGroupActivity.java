package com.shikshitha.admin.usergroup;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.dao.UserGroupDao;
import com.shikshitha.admin.model.GroupUsers;
import com.shikshitha.admin.model.Groups;
import com.shikshitha.admin.model.Student;
import com.shikshitha.admin.model.StudentSet;
import com.shikshitha.admin.model.Teacher;
import com.shikshitha.admin.model.TeacherSet;
import com.shikshitha.admin.model.UserGroup;
import com.shikshitha.admin.util.AlertDialogHelper;
import com.shikshitha.admin.util.DividerItemDecoration;
import com.shikshitha.admin.util.NetworkUtil;
import com.shikshitha.admin.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserGroupActivity extends AppCompatActivity implements
        UserGroupView, AlertDialogHelper.AlertDialogListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.group_name_tv)
    TextView groupName;
    @BindView(R.id.add_students_layout)
    RelativeLayout addStudentsLayout;
    @BindView(R.id.add_teacher_layout) TextView addTeacherLayout;
    @BindView(R.id.no_members)
    LinearLayout noMembers;
    @BindView(R.id.member_recycler_view)
    RecyclerView memberView;
    @BindView(R.id.student_recycler_view) RecyclerView studentView;
    @BindView(R.id.teacher_recycler_view) RecyclerView teacherView;

    private UserGroupPresenter presenter;
    private Groups group;
    private UserGroupAdapter adapter;
    private StudentMemberAdapter studentAdapter;
    private TeacherMemberAdapter teacherAdapter;
    AlertDialogHelper alertDialogHelper;

    private Menu menu;

    ActionMode mActionMode;
    boolean isMultiSelect = false;
    ArrayList<UserGroup> userGroups = new ArrayList<>();
    ArrayList<UserGroup> multiselect_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_group);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            group = new Groups();
            group.setId(extras.getLong("groupId"));
            group.setName(extras.getString("groupName"));
            groupName.setText(group.getName());
        }*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            group = (Groups) extras.getSerializable("group");
        }

        presenter = new UserGroupPresenterImpl(this, new UserGroupInteractorImpl());

        alertDialogHelper = new AlertDialogHelper(this);

        initRecyclerView();

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getUserGroup(group.getId());
            }
        });

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getUserGroup(group.getId());
        } else {
            List<UserGroup> usrGroups = UserGroupDao.getUserGroups(group.getId());
            if(usrGroups.size() == 0) {
                noMembers.setVisibility(View.VISIBLE);
            } else {
                noMembers.setVisibility(View.GONE);
                adapter.setDataSet(usrGroups, multiselect_list);
            }
            addStudentsLayout.setVisibility(View.GONE);
            addTeacherLayout.setVisibility(View.GONE);
        }

    }

    private void initRecyclerView() {
        memberView.setLayoutManager(new LinearLayoutManager(this));
        memberView.setNestedScrollingEnabled(false);
        memberView.setItemAnimator(new DefaultItemAnimator());
        memberView.addItemDecoration(new DividerItemDecoration(this));

        adapter = new UserGroupAdapter(this, new ArrayList<UserGroup>(0), new ArrayList<UserGroup>(0));
        memberView.setAdapter(adapter);

        memberView.addOnItemTouchListener(new RecyclerItemClickListener(this, memberView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) multi_select(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }
                multi_select(position);
            }
        }));

        studentView.setLayoutManager(new LinearLayoutManager(this));
        studentView.setNestedScrollingEnabled(false);
        studentView.setItemAnimator(new DefaultItemAnimator());
        studentAdapter = new StudentMemberAdapter(new ArrayList<StudentSet>(0));
        studentView.setAdapter(studentAdapter);

        teacherView.setLayoutManager(new LinearLayoutManager(this));
        teacherView.setNestedScrollingEnabled(false);
        teacherView.setItemAnimator(new DefaultItemAnimator());
        teacherAdapter = new TeacherMemberAdapter(new ArrayList<TeacherSet>(0));
        teacherView.setAdapter(teacherAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_group_overflow, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void selectAllStudents(View view) {
        showProgress();
        ArrayList<StudentSet> studentSets = studentAdapter.getDataSet();
        for(StudentSet studentSet : studentSets) {
            studentSet.setSelected(true);
        }
        studentAdapter.setDataSet(studentSets);
        hideProgress();
    }

    public void saveUsers(MenuItem item) {
        showProgress();
        ArrayList<UserGroup> usrGroups = new ArrayList<>();
        for(StudentSet studentSet : studentAdapter.getDataSet()) {
            if(studentSet.isSelected()) {
                UserGroup userGroup = new UserGroup();
                userGroup.setUserId(studentSet.getId());
                userGroup.setRole("student");
                userGroup.setGroupId(group.getId());
                userGroup.setActive(true);
                usrGroups.add(userGroup);
            }
        }
        for(TeacherSet teacherSet : teacherAdapter.getDataSet()) {
            if(teacherSet.isSelected()) {
                UserGroup userGroup = new UserGroup();
                userGroup.setUserId(teacherSet.getId());
                userGroup.setRole("teacher");
                userGroup.setGroupId(group.getId());
                userGroup.setActive(true);
                usrGroups.add(userGroup);
            }
        }
        hideProgress();
        if(usrGroups.size() == 0) {
            showSnackbar("No changes detected");
        } else {
            presenter.saveUserGroup(usrGroups);
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

    @Override
    public void showUserGroup(GroupUsers groupUsers) {
        userGroups = groupUsers.getUserGroupList();

        if(userGroups.size() == 0) {
            noMembers.setVisibility(View.VISIBLE);
        } else {
            menu.findItem(R.id.action_save).setVisible(true);
            noMembers.setVisibility(View.GONE);
            adapter.setDataSet(userGroups, multiselect_list);
        }
        ArrayList<StudentSet> studentSets = new ArrayList<>();
        for(Student s: groupUsers.getStudents()) {
            studentSets.add(new StudentSet(s.getId(), s.getRollNo(), s.getName()));
        }
        studentAdapter.setDataSet(studentSets);
        if(studentSets.size() == 0) {
            addStudentsLayout.setVisibility(View.GONE);
        }

        ArrayList<TeacherSet> teacherSets = new ArrayList<>();
        for(Teacher t: groupUsers.getTeachers()) {
            teacherSets.add(new TeacherSet(t.getId(), t.getName()));
        }
        teacherAdapter.setDataSet(teacherSets);
        if(teacherSets.size() == 0) {
            addTeacherLayout.setVisibility(View.GONE);
        }
        refreshLayout.setRefreshing(false);
        backupUserGroup(userGroups);
    }

    private void backupUserGroup(final List<UserGroup> userGroupList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserGroupDao.clear(group.getId());
                UserGroupDao.insert(userGroupList);
            }
        }).start();
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(userGroups.get(position)))
                multiselect_list.remove(userGroups.get(position));
            else
                multiselect_list.add(userGroups.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");
            refreshAdapter();
        }
    }

    public void refreshAdapter() {
        adapter.setDataSet(userGroups, multiselect_list);
    }

    @Override
    public void userGroupSaved() {
        noMembers.setVisibility(View.GONE);
        recreate();
    }

    @Override
    public void userGroupDeleted() {
        recreate();
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
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
                    alertDialogHelper.showAlertDialog("","Remove Users","DELETE","CANCEL",1,false);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<>();
            refreshAdapter();
        }
    };

    @Override
    public void onPositiveClick(int from) {
        if(from==1) {
            if(multiselect_list.size()>0) {
                presenter.deleteUsers(multiselect_list);
                if (mActionMode != null) {
                    mActionMode.finish();
                }
            }
        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }

}

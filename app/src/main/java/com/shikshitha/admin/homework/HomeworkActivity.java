package com.shikshitha.admin.homework;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shikshitha.admin.BaseActivity;
import com.shikshitha.admin.R;
import com.shikshitha.admin.dao.ClassDao;
import com.shikshitha.admin.dao.HomeworkDao;
import com.shikshitha.admin.dao.SectionDao;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Homework;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Teacher;
import com.shikshitha.admin.util.AlertDialogHelper;
import com.shikshitha.admin.util.Conversion;
import com.shikshitha.admin.util.DatePickerFragment;
import com.shikshitha.admin.util.DateUtil;
import com.shikshitha.admin.util.DividerItemDecoration;
import com.shikshitha.admin.util.NetworkUtil;
import com.shikshitha.admin.util.RecyclerItemClickListener;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeworkActivity extends BaseActivity implements HomeworkView,
        AdapterView.OnItemSelectedListener, AlertDialogHelper.AlertDialogListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.spinner_class) Spinner classSpinner;
    @BindView(R.id.spinner_section) Spinner sectionSpinner;
    @BindView(R.id.section_layout) LinearLayout sectionLayout;
    @BindView(R.id.date_tv) TextView dateView;
    @BindView(R.id.homework_recycler_view) RecyclerView homeworkRecycler;
    @BindView(R.id.subject_recycler_view) RecyclerView subjectRecycler;
    @BindView(R.id.homework_tv) TextView homeworkTv;
    @BindView(R.id.enter_homework) TextView enterHomeworkTv;

    private HomeworkPresenter presenter;
    private String homeworkDate;
    private Teacher teacher;
    ActionMode mActionMode;
    boolean isMultiSelect = false;
    AlertDialogHelper alertDialogHelper;

    ArrayList<Homework> homeworks = new ArrayList<>();
    ArrayList<Homework> multiselect_list = new ArrayList<>();

    private HomeworkAdapter homeworkAdapter;
    private NewHomeworkAdapter newHomeworkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new HomeworkPresenterImpl(this, new HomeworkInteractorImpl());

        alertDialogHelper = new AlertDialogHelper(this);

        setupRecyclerView();

        setDefaultDate();

        teacher = TeacherDao.getTeacher();

        setProfile(teacher);

        setNavigationItem(2);

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getClassList(teacher.getSchoolId());
        } else {
            showOfflineClass();
        }

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getClassList(teacher.getSchoolId());
            }
        });
    }

    private void setDefaultDate() {
        dateView.setText(DateUtil.getDisplayFormattedDate(new LocalDate().toString()));
        homeworkDate = new LocalDate().toString();
    }

    public void changeDate(View view) {
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = defaultFormat.parse(homeworkDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        bundle.putInt("day", day);
        newFragment.setCallBack(onDate);
        newFragment.setArguments(bundle);
        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            Calendar cal = Calendar.getInstance();
            cal.set(year, monthOfYear, dayOfMonth);
            Date date = cal.getTime();

            dateView.setText(DateUtil.getDisplayFormattedDate(dateFormat.format(date)));
            homeworkDate = dateFormat.format(date);
            getHomework();
        }
    };

    private void setupRecyclerView() {
        homeworkRecycler.setLayoutManager(new LinearLayoutManager(this));
        homeworkRecycler.setNestedScrollingEnabled(false);
        homeworkRecycler.setItemAnimator(new DefaultItemAnimator());

        homeworkAdapter = new HomeworkAdapter(this, new ArrayList<Homework>(0), new ArrayList<Homework>(0));
        homeworkRecycler.setAdapter(homeworkAdapter);

        homeworkRecycler.addOnItemTouchListener(new RecyclerItemClickListener(this, homeworkRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) multi_select(position);
                else {
                    final Homework homework = homeworks.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeworkActivity.this);
                    View v = getLayoutInflater().inflate(R.layout.homework_dialog, null);
                    TextView subjectName = (TextView) v.findViewById(R.id.hw_subject);
                    subjectName.setText(homework.getSubjectName());
                    final EditText homeworkText = (EditText) v.findViewById(R.id.hw_et);
                    homeworkText.setText(homework.getHomeworkMessage());
                    builder.setView(v);

                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(NetworkUtil.isNetworkAvailable(HomeworkActivity.this)) {
                                homework.setHomeworkMessage(homeworkText.getText().toString());
                                presenter.updateHomework(homework);
                            } else {
                                showSnackbar("You are offline !");
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }
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

        subjectRecycler.setLayoutManager(new LinearLayoutManager(this));
        subjectRecycler.setNestedScrollingEnabled(false);
        subjectRecycler.setItemAnimator(new DefaultItemAnimator());
        subjectRecycler.addItemDecoration(new DividerItemDecoration(this));
        newHomeworkAdapter = new NewHomeworkAdapter(new ArrayList<Homework>(0), mItemListener);
        subjectRecycler.setAdapter(newHomeworkAdapter);
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(homeworks.get(position)))
                multiselect_list.remove(homeworks.get(position));
            else
                multiselect_list.add(homeworks.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");
            refreshAdapter();
        }
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
                    alertDialogHelper.showAlertDialog("", "Delete Homework", "DELETE", "CANCEL", 1, false);
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

    public void refreshAdapter() {
        homeworkAdapter.setDataSet(homeworks, multiselect_list);
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
    }

    @Override
    public void showOffline(String tableName) {
        switch (tableName){
            case "class":
                showOfflineClass();
                break;
            case "section":
                showOfflineSection();
                break;
            case "homework":
                showOfflineHomework();
                break;
            default:
                break;
        }
    }

    @Override
    public void showClass(List<Clas> clasList) {
        ArrayAdapter<Clas> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clasList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
        classSpinner.setOnItemSelectedListener(this);
        backupClass(clasList);
    }

    private void backupClass(final List<Clas> classList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ClassDao.delete(teacher.getSchoolId());
                ClassDao.insert(classList);
            }
        }).start();
    }

    private void showOfflineClass() {
        List<Clas> clasList = ClassDao.getClassList(teacher.getSchoolId());
        ArrayAdapter<Clas> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clasList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
        classSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void showSection(List<Section> sectionList) {
        setSectionAdapter(sectionList);
        backupSection(sectionList);
    }

    private void backupSection(final List<Section> sectionList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SectionDao.delete(((Clas) classSpinner.getSelectedItem()).getId());
                SectionDao.insert(sectionList);
            }
        }).start();
    }

    private void showOfflineSection() {
        List<Section> sectionList = SectionDao.getSectionList(((Clas) classSpinner.getSelectedItem()).getId());
        setSectionAdapter(sectionList);
    }

    private void setSectionAdapter(List<Section> sectionList) {
        ArrayAdapter<Section> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectionList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
        sectionSpinner.setOnItemSelectedListener(this);
        if(sectionList.size() == 1 && sectionList.get(0).getSectionName().equals("none")) {
            sectionLayout.setVisibility(View.INVISIBLE);
            sectionLayout.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        } else {
            sectionLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            int px = Conversion.dpToPx(10, getApplicationContext());
            sectionLayout.setPadding(px, px, px, px);
            sectionLayout.setLayoutParams(params);
        }
    }

    @Override
    public void showHomeworks(List<Homework> hws) {
        refreshLayout.setRefreshing(false);
        homeworks = new ArrayList<>();
        List<Homework> newHomework = new ArrayList<>();
        for (Homework hw : hws) {
            if (hw.getId() == 0) newHomework.add(hw);
            else homeworks.add(hw);
        }
        homeworkAdapter.setDataSet(homeworks, multiselect_list);
        if (homeworks.size() == 0) homeworkTv.setVisibility(View.GONE);
        else homeworkTv.setVisibility(View.VISIBLE);

        newHomeworkAdapter.setDataSet(newHomework);
        if (newHomework.size() == 0) enterHomeworkTv.setVisibility(View.GONE);
        else enterHomeworkTv.setVisibility(View.VISIBLE);
        backupHomework(hws);
    }

    private void backupHomework(final List<Homework> homeworkList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HomeworkDao.delete(((Section)sectionSpinner.getSelectedItem()).getId(), homeworkDate);
                HomeworkDao.insert(homeworkList);
            }
        }).start();
    }

    private void showOfflineHomework() {
        List<Homework> homeworkList = HomeworkDao.getHomework(((Section) sectionSpinner.getSelectedItem()).getId(), homeworkDate);
        homeworks = new ArrayList<>();
        for (Homework hw : homeworkList) {
            if (hw.getId() != 0) homeworks.add(hw);
        }
        homeworkAdapter.setDataSet(homeworks, multiselect_list);
        enterHomeworkTv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void homeworkSaved(Homework homework) {
        recreate();
    }

    @Override
    public void homeworkUpdated() {
        recreate();
    }

    @Override
    public void homeworkDeleted() {
        recreate();
    }

    private void getHomework() {
        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getHomework(((Section) sectionSpinner.getSelectedItem()).getId(), homeworkDate);
        } else {
            showOfflineHomework();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        switch (parent.getId()) {
            case R.id.spinner_class:
                if(NetworkUtil.isNetworkAvailable(this)) {
                    presenter.getSectionList(((Clas) classSpinner.getSelectedItem()).getId());
                } else {
                    showOfflineSection();
                }
                break;
            case R.id.spinner_section:
                getHomework();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPositiveClick(int from) {
        if (from == 1) {
            if (multiselect_list.size() > 0) {
                if(NetworkUtil.isNetworkAvailable(HomeworkActivity.this)) {
                    presenter.deleteHomework(multiselect_list);
                } else {
                    showSnackbar("You are offline !");
                }
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

    NewHomeworkAdapter.OnItemClickListener mItemListener = new NewHomeworkAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(final Homework homework) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeworkActivity.this);
            View view = getLayoutInflater().inflate(R.layout.homework_dialog, null);
            TextView subjectName = view.findViewById(R.id.hw_subject);
            subjectName.setText(homework.getSubjectName());
            final EditText homeworkText = view.findViewById(R.id.hw_et);
            builder.setView(view);

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(NetworkUtil.isNetworkAvailable(HomeworkActivity.this)) {
                        homework.setHomeworkMessage(homeworkText.getText().toString());
                        presenter.saveHomework(homework);
                    } else {
                        showSnackbar("You are offline !");
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}

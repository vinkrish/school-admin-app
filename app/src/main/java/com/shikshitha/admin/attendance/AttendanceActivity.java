package com.shikshitha.admin.attendance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.dao.AttendanceDao;
import com.shikshitha.admin.dao.ClassDao;
import com.shikshitha.admin.dao.SectionDao;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.dao.TimetableDao;
import com.shikshitha.admin.model.Attendance;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Student;
import com.shikshitha.admin.model.StudentSet;
import com.shikshitha.admin.model.Timetable;
import com.shikshitha.admin.util.AlertDialogHelper;
import com.shikshitha.admin.util.DatePickerFragment;
import com.shikshitha.admin.util.DateUtil;
import com.shikshitha.admin.util.DividerItemDecoration;
import com.shikshitha.admin.util.NetworkUtil;
import com.shikshitha.admin.util.RecyclerItemClickListener;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceActivity extends AppCompatActivity implements AttendanceView,
        AdapterView.OnItemSelectedListener, AlertDialogHelper.AlertDialogListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.spinner_class) Spinner classSpinner;
    @BindView(R.id.spinner_section) Spinner sectionSpinner;
    @BindView(R.id.date_tv) TextView dateView;
    @BindView(R.id.session_spinner) Spinner sessionSpinner;
    @BindView(R.id.session_layout) LinearLayout sessionLayout;
    @BindView(R.id.period_spinner) Spinner periodSpinner;
    @BindView(R.id.period_layout) LinearLayout periodLayout;
    @BindView(R.id.absentees_recycler_view) RecyclerView absenteesRecycler;
    @BindView(R.id.student_recycler_view) RecyclerView studentRecycler;
    @BindView(R.id.absentees_tv) TextView absenteesTv;
    @BindView(R.id.mark_students) TextView markStudentsTv;

    private Menu menu;

    private AttendancePresenter presenter;
    private String attendanceDate;
    ActionMode mActionMode;
    boolean isMultiSelect = false;
    AlertDialogHelper alertDialogHelper;

    ArrayList<Attendance> absentees = new ArrayList<>();
    ArrayList<Attendance> multiselect_list = new ArrayList<>();

    private AttendanceAdapter attendanceAdapter;
    private StudentAdapter studentAdapter;

    private String[] days = {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new AttendancePresenterImpl(this, new AttendanceInteractorImpl());
        setDefaultDate();

        alertDialogHelper = new AlertDialogHelper(this);

        initRecyclerView();

        showSession();

        if(NetworkUtil.isNetworkAvailable(this)) {
            presenter.getClassList(TeacherDao.getTeacher().getSchoolId());
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
                presenter.getClassList(TeacherDao.getTeacher().getSchoolId());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attendance_overflow, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void initRecyclerView() {
        absenteesRecycler.setLayoutManager(new LinearLayoutManager(this));
        absenteesRecycler.setNestedScrollingEnabled(false);
        absenteesRecycler.setItemAnimator(new DefaultItemAnimator());
        absenteesRecycler.addItemDecoration(new DividerItemDecoration(this));

        attendanceAdapter = new AttendanceAdapter(this, new ArrayList<Attendance>(0), new ArrayList<Attendance>(0));
        absenteesRecycler.setAdapter(attendanceAdapter);

        absenteesRecycler.addOnItemTouchListener(new RecyclerItemClickListener(this, absenteesRecycler, new RecyclerItemClickListener.OnItemClickListener() {
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

        studentRecycler.setLayoutManager(new LinearLayoutManager(this));
        studentRecycler.setNestedScrollingEnabled(false);
        studentRecycler.setItemAnimator(new DefaultItemAnimator());
        studentRecycler.addItemDecoration(new DividerItemDecoration(this));
        studentAdapter = new StudentAdapter(new ArrayList<StudentSet>(0), getApplicationContext());
        studentRecycler.setAdapter(studentAdapter);
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(absentees.get(position)))
                multiselect_list.remove(absentees.get(position));
            else
                multiselect_list.add(absentees.get(position));

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
                    alertDialogHelper.showAlertDialog("","Remove Marked Attendance","DELETE","CANCEL",1,false);
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
        attendanceAdapter.setDataSet(absentees, multiselect_list);
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
            case "attendance":
                showOfflineAttendance();
                break;
            case "timetable":
                showOfflineTimetable();
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
                ClassDao.delete(TeacherDao.getTeacher().getSchoolId());
                ClassDao.insert(classList);
            }
        }).start();
    }

    private void showOfflineClass() {
        List<Clas> clasList = ClassDao.getClassList(TeacherDao.getTeacher().getSchoolId());
        ArrayAdapter<Clas> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clasList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
        classSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void showSection(List<Section> sectionList) {
        ArrayAdapter<Section> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectionList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
        sectionSpinner.setOnItemSelectedListener(this);
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
        ArrayAdapter<Section> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectionList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
        sectionSpinner.setOnItemSelectedListener(this);
    }

    private void showSession() {
        String[] sessions = {"Morning", "Afternoon"};
        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList(sessions));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionSpinner.setAdapter(adapter);
        sessionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void showTimetable(List<Timetable> timetableList) {
        ArrayAdapter<Timetable> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timetableList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(adapter);
        periodSpinner.setOnItemSelectedListener(this);
        backupTimetable(timetableList);
    }

    @Override
    public void showAttendance(AttendanceSet attendanceSet) {
        absentees = attendanceSet.getAttendanceList();
        attendanceAdapter.setDataSet(absentees, multiselect_list);
        if(absentees.size() == 0) absenteesTv.setVisibility(View.GONE);
        else absenteesTv.setVisibility(View.VISIBLE);

        ArrayList<StudentSet> studentSets = new ArrayList<>();
        for(Student s: attendanceSet.getStudents()) {
            studentSets.add(new StudentSet(s.getId(), s.getRollNo(), s.getName()));
        }
        studentAdapter.setDataSet(studentSets);
        if(studentSets.size() == 0) {
            markStudentsTv.setVisibility(View.GONE);
            menu.findItem(R.id.action_save).setVisible(false);
        } else {
            markStudentsTv.setVisibility(View.VISIBLE);
            menu.findItem(R.id.action_save).setVisible(true);
        }
        refreshLayout.setRefreshing(false);
        backupAttendance(attendanceSet.getAttendanceList());
    }

    private void backupAttendance(final List<Attendance> attendanceList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String attendanceType = ((Clas) classSpinner.getSelectedItem()).getAttendanceType();
                switch (attendanceType) {
                    case "Daily":
                        AttendanceDao.delete(((Section) sectionSpinner.getSelectedItem()).getId(), attendanceDate, 0);
                        break;
                    case "Session":
                        AttendanceDao.delete(((Section) sectionSpinner.getSelectedItem()).getId(),
                                attendanceDate,  sessionSpinner.getSelectedItem().equals("Morning") ? 0 : 1);
                        break;
                    case "Period":
                        AttendanceDao.delete(((Section) sectionSpinner.getSelectedItem()).getId(),
                                attendanceDate, ((Timetable) periodSpinner.getSelectedItem()).getPeriodNo());
                        break;
                    default:
                        break;
                }
                AttendanceDao.insert(attendanceList);
            }
        }).start();
    }

    private void showOfflineAttendance() {
        String attendanceType = ((Clas)classSpinner.getSelectedItem()).getAttendanceType();
        switch (attendanceType){
            case "Daily":
                List<Attendance> attendanceList1 = AttendanceDao.getAttendance(
                        ((Section) sectionSpinner.getSelectedItem()).getId(), attendanceDate, 0);
                viewAttendance(attendanceList1);
                break;
            case "Session":
                List<Attendance> attendanceList2 = AttendanceDao.getAttendance(
                        ((Section) sectionSpinner.getSelectedItem()).getId(), attendanceDate,
                        sessionSpinner.getSelectedItem().equals("Morning") ? 0 : 1);
                viewAttendance(attendanceList2);
                break;
            case "Period":
                showOfflineTimetable();
                break;
            default:
                break;
        }
    }

    private void showOfflineTimetable() {
        List<Timetable> timetableList = TimetableDao.getDayTimetable(((Section) sectionSpinner.getSelectedItem()).getId(),
                days[getFormattedCalendar().get(Calendar.DAY_OF_WEEK)]);
        ArrayAdapter<Timetable> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timetableList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(adapter);
        periodSpinner.setOnItemSelectedListener(this);
    }

    private void backupTimetable(final List<Timetable> timetableList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TimetableDao.delete(((Section) sectionSpinner.getSelectedItem()).getId());
                TimetableDao.insert(timetableList);
            }
        }).start();
    }

    public void saveAbsentees(MenuItem item) {
        showProgress();
        ArrayList<Attendance> attList = new ArrayList<>();
        for(StudentSet studentSet : studentAdapter.getDataSet()) {
            if(studentSet.isSelected()) {
                Attendance att = new Attendance();
                att.setStudentId(studentSet.getId());
                att.setStudentName(studentSet.getName());
                att.setSectionId(((Section)sectionSpinner.getSelectedItem()).getId());
                att.setDateAttendance(attendanceDate);
                att.setTypeOfLeave("Absent");
                String attendanceType = ((Clas)classSpinner.getSelectedItem()).getAttendanceType();
                att.setType(attendanceType);
                switch (attendanceType) {
                    case "Daily":
                        att.setSession(0);
                        break;
                    case "Session":
                        att.setSession(sessionSpinner.getSelectedItem().equals("Morning") ? 0: 1);
                        break;
                    case "Period":
                        att.setSession(((Timetable)periodSpinner.getSelectedItem()).getPeriodNo());
                        break;
                    default:
                        break;
                }
                attList.add(att);
            }
        }
        hideProgress();
        if(attList.size() == 0) showSnackbar("No changes detected");
        else presenter.saveAttendance(attList);

    }

    @Override
    public void attendanceSaved() {
        recreate();
    }

    @Override
    public void attendanceDeleted() {
        recreate();
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
                getAttendance();
                break;
            case R.id.session_spinner:
                if(NetworkUtil.isNetworkAvailable(this)) {
                    presenter.getAttendance(((Section) sectionSpinner.getSelectedItem()).getId(),
                            attendanceDate, sessionSpinner.getSelectedItem().equals("Morning") ? 0 : 1);
                } else {
                    List<Attendance> attendanceList = AttendanceDao.getAttendance(
                            ((Section) sectionSpinner.getSelectedItem()).getId(), attendanceDate,
                            sessionSpinner.getSelectedItem().equals("Morning") ? 0 : 1);
                    viewAttendance(attendanceList);
                }
                break;
            case R.id.period_spinner:
                if(NetworkUtil.isNetworkAvailable(this)) {
                    presenter.getAttendance(((Section) sectionSpinner.getSelectedItem()).getId(), attendanceDate,
                            ((Timetable) periodSpinner.getSelectedItem()).getPeriodNo());
                } else {
                    List<Attendance> attendanceList = AttendanceDao.getAttendance(
                            ((Section) sectionSpinner.getSelectedItem()).getId(), attendanceDate,
                            ((Timetable) periodSpinner.getSelectedItem()).getPeriodNo());
                    viewAttendance(attendanceList);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void viewAttendance(List<Attendance> attendanceList) {
        if (attendanceList.size() == 0) {
            //noAttendance.setVisibility(View.VISIBLE);
        } else {
            //noAttendance.setVisibility(View.INVISIBLE);
            attendanceAdapter.setDataSet(attendanceList, multiselect_list);
        }
    }

    private void setDefaultDate() {
        dateView.setText(DateUtil.getDisplayFormattedDate(new LocalDate().toString()));
        attendanceDate = new LocalDate().toString();
    }

    public void changeDate(View view) {
        Calendar cal = getFormattedCalendar();
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

    private Calendar getFormattedCalendar() {
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = defaultFormat.parse(attendanceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            Calendar cal = Calendar.getInstance();
            cal.set(year, monthOfYear, dayOfMonth);
            Date date = cal.getTime();

            Calendar tomorrowCal = Calendar.getInstance();
            Date tomorrowDate = tomorrowCal.getTime();

            if (date.after(tomorrowDate)) {
                showSnackbar(getResources().getText(R.string.future_date).toString());
            } else {
                dateView.setText(DateUtil.getDisplayFormattedDate(dateFormat.format(date)));
                attendanceDate = dateFormat.format(date);
                getAttendance();
            }
        }
    };

    private void getAttendance() {
        String attendanceType = ((Clas)classSpinner.getSelectedItem()).getAttendanceType();
        switch (attendanceType){
            case "Daily":
                if(NetworkUtil.isNetworkAvailable(this)) {
                    presenter.getAttendance(((Section) sectionSpinner.getSelectedItem()).getId(), attendanceDate, 0);
                } else {
                    List<Attendance> attendanceList = AttendanceDao.getAttendance(
                            ((Section) sectionSpinner.getSelectedItem()).getId(), attendanceDate, 0);
                    viewAttendance(attendanceList);
                }
                break;
            case "Session":
                periodLayout.setVisibility(View.GONE);
                sessionLayout.setVisibility(View.VISIBLE);
                if(NetworkUtil.isNetworkAvailable(this)) {
                    presenter.getAttendance(((Section) sectionSpinner.getSelectedItem()).getId(),
                            attendanceDate, sessionSpinner.getSelectedItem().equals("Morning") ? 0 : 1);
                } else {
                    List<Attendance> attendanceList = AttendanceDao.getAttendance(
                            ((Section) sectionSpinner.getSelectedItem()).getId(), attendanceDate,
                            sessionSpinner.getSelectedItem().equals("Morning") ? 0 : 1);
                    viewAttendance(attendanceList);
                }
                break;
            case "Period":
                sessionLayout.setVisibility(View.GONE);
                periodLayout.setVisibility(View.VISIBLE);
                if(NetworkUtil.isNetworkAvailable(this)) {
                    presenter.getTimetable(((Section) sectionSpinner.getSelectedItem()).getId(),
                            days[getFormattedCalendar().get(Calendar.DAY_OF_WEEK)]);
                } else {
                    showOfflineTimetable();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPositiveClick(int from) {
        if(from==1) {
            if(multiselect_list.size()>0) {
                presenter.deleteAttendance(multiselect_list);
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

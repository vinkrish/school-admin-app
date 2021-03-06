package com.shikshitha.admin.reportcard;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.shikshitha.admin.BaseActivity;
import com.shikshitha.admin.R;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Teacher;
import com.shikshitha.admin.util.Conversion;
import com.shikshitha.admin.util.DividerItemDecoration;
import com.shikshitha.admin.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportActivity extends BaseActivity implements ReportView,
        AdapterView.OnItemSelectedListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.spinner_class) Spinner classSpinner;
    @BindView(R.id.spinner_section) Spinner sectionSpinner;
    @BindView(R.id.section_layout) LinearLayout sectionLayout;
    @BindView(R.id.spinner_exam) Spinner examSpinner;
    @BindView(R.id.spinner_subject) Spinner subjectSpinner;
    @BindView(R.id.spinner_activity) Spinner activitySpinner;
    @BindView(R.id.score_view) RecyclerView scoreView;
    @BindView(R.id.no_score) LinearLayout noScoreLayout;
    @BindView(R.id.check_activity) Button checkActivity;
    @BindView(R.id.act_score_view) RecyclerView actScoreView;
    @BindView(R.id.no_act_score) LinearLayout noActScoreLayout;
    @BindView(R.id.score_layout) LinearLayout scoreLayout;
    @BindView(R.id.activity_score_layout) LinearLayout activityScoreLayout;
    @BindView(R.id.exam_max_score) TextView examMaxScore;
    @BindView(R.id.activity_max_score) TextView activityMaxScore;

    private ReportPresenter presenter;
    private ScoreAdapter adapter;
    private ActivityScoreAdapter activityAdapter;
    private Teacher teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new ReportPresenterImpl(this, new ReportInteractorImpl());

        teacher = TeacherDao.getTeacher();

        setupRecyclerView();

        setProfile(teacher);

        setNavigationItem(5);

        if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            presenter.getClassList(teacher.getSchoolId());
        }
    }

    private void setupRecyclerView() {
        scoreView.setLayoutManager(new LinearLayoutManager(this));
        scoreView.setNestedScrollingEnabled(false);
        scoreView.setItemAnimator(new DefaultItemAnimator());
        scoreView.addItemDecoration(new DividerItemDecoration(this));

        adapter = new ScoreAdapter(new ArrayList<Mark>(0));
        scoreView.setAdapter(adapter);

        actScoreView.setLayoutManager(new LinearLayoutManager(this));
        actScoreView.setNestedScrollingEnabled(false);
        actScoreView.setItemAnimator(new DefaultItemAnimator());
        actScoreView.addItemDecoration(new DividerItemDecoration(this));

        activityAdapter = new ActivityScoreAdapter(new ArrayList<ActivityScore>(0));
        actScoreView.setAdapter(activityAdapter);
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
    public void showClass(List<Clas> clasList) {
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
    public void showExam(List<Exam> exams) {
        ArrayAdapter<Exam> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        examSpinner.setAdapter(adapter);
        examSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void showExamSubject(List<ExamSubject> examSubjects) {
        ArrayAdapter<ExamSubject> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, examSubjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);
        subjectSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void showScore(List<Mark> marks) {
        scoreLayout.setVisibility(View.VISIBLE);
        checkActivity.setVisibility(View.VISIBLE);
        if(marks.size() > 0) {
            noScoreLayout.setVisibility(View.GONE);
            examMaxScore.setText(String.format(Locale.ENGLISH,"Max: %d", (int)((ExamSubject) subjectSpinner.getSelectedItem()).getMaximumMark()));
            adapter.setDataSet(marks);
        } else {
            noScoreLayout.setVisibility(View.VISIBLE);
            examMaxScore.setText(getText(R.string.score));
            adapter.setDataSet(new ArrayList<Mark>(0));
        }
    }

    @Override
    public void showActivity(List<Activity> activityList) {
        if(activityList.size() > 0) {
            activitySpinner.setVisibility(View.VISIBLE);
            ArrayAdapter<Activity> adapter = new
                    ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activityList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            activitySpinner.setAdapter(adapter);
            activitySpinner.setOnItemSelectedListener(this);
        } else {
            showSnackbar("No activity/rubric created!");
        }
    }

    @Override
    public void showActivityScore(List<ActivityScore> activityScores) {
        activityScoreLayout.setVisibility(View.VISIBLE);
        if(activityScores.size() > 0) {
            noActScoreLayout.setVisibility(View.GONE);
            activityMaxScore.setText(String.format(Locale.ENGLISH,"Max: %d", (int)((Activity) activitySpinner.getSelectedItem()).getMaximumMark()));
            activityAdapter.setDataSet(activityScores);
        } else {
            noActScoreLayout.setVisibility(View.VISIBLE);
            examMaxScore.setText(getText(R.string.score));
            activityAdapter.setDataSet(new ArrayList<ActivityScore>(0));
        }
    }

    public void fetchActivity(View view){
        if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            presenter.getActivityList(((Section) sectionSpinner.getSelectedItem()).getId(),
                    ((Exam) examSpinner.getSelectedItem()).getId(),
                    ((ExamSubject) subjectSpinner.getSelectedItem()).getId());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        switch (parent.getId()) {
            case R.id.spinner_class:
                if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    presenter.getSectionList(((Clas) classSpinner.getSelectedItem()).getId(),
                            teacher.getId());
                    sectionSpinner.setAdapter(null);
                    examSpinner.setAdapter(null);
                    subjectSpinner.setAdapter(null);
                    activitySpinner.setAdapter(null);
                    activityAdapter.setDataSet(new ArrayList<ActivityScore>(0));

                    hideVisibility();
                }
                break;
            case R.id.spinner_section:
                if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    presenter.getExams(((Clas) classSpinner.getSelectedItem()).getId());
                    examSpinner.setAdapter(null);
                    subjectSpinner.setAdapter(null);
                    activitySpinner.setAdapter(null);
                    activityAdapter.setDataSet(new ArrayList<ActivityScore>(0));

                    hideVisibility();
                }
                break;
            case R.id.spinner_exam:
                if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    presenter.getExamSubjects(((Exam) examSpinner.getSelectedItem()).getId());
                    subjectSpinner.setAdapter(null);
                    activitySpinner.setAdapter(null);
                    activityAdapter.setDataSet(new ArrayList<ActivityScore>(0));

                    hideVisibility();
                }
                break;
            case R.id.spinner_subject:
                if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    activitySpinner.setAdapter(null);
                    activityAdapter.setDataSet(new ArrayList<ActivityScore>(0));

                    hideVisibility();

                    presenter.getScore(((Exam) examSpinner.getSelectedItem()).getId(),
                            ((ExamSubject) subjectSpinner.getSelectedItem()).getSubjectId(),
                            ((Section) sectionSpinner.getSelectedItem()).getId());
                }
                break;
            case R.id.spinner_activity:
                if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    activityAdapter.setDataSet(new ArrayList<ActivityScore>(0));
                    presenter.getActivityScore(((Activity) activitySpinner.getSelectedItem()).getId());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void hideVisibility() {
        scoreLayout.setVisibility(View.GONE);
        checkActivity.setVisibility(View.GONE);
        activitySpinner.setVisibility(View.GONE);
        activityScoreLayout.setVisibility(View.GONE);
    }

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

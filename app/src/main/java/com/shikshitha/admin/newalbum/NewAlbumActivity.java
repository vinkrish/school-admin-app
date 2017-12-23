package com.shikshitha.admin.newalbum;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.shikshitha.admin.R;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Teacher;
import com.shikshitha.admin.util.Conversion;
import com.shikshitha.admin.util.EditTextWatcher;
import com.shikshitha.admin.util.SharedPreferenceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewAlbumActivity extends AppCompatActivity implements NewAlbumView,
        AdapterView.OnItemSelectedListener{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.album_et) EditText albumName;
    @BindView(R.id.album) TextInputLayout albumLayout;
    @BindView(R.id.album_spinner) Spinner albumSpinner;
    @BindView(R.id.class_spinner) Spinner classSpinner;
    @BindView(R.id.class_layout) LinearLayout classLayout;
    @BindView(R.id.section_spinner) Spinner sectionSpinner;
    @BindView(R.id.section_layout) LinearLayout sectionLayout;

    private Teacher teacher;
    private NewAlbumPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_album);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        teacher = TeacherDao.getTeacher();

        presenter = new NewAlbumPresenterImpl(this, new NewAlbumInteractorImpl());

        initAlbumSpinner();

        albumName.addTextChangedListener(new EditTextWatcher(albumLayout));
    }

    public void createAlbum(View view) {
        if(albumName.getText().toString().isEmpty()) {
            albumName.setError("Album name can't be empty");
        } else {
            Album album = new Album();
            album.setCoverPic("");
            album.setCreatedAt(System.currentTimeMillis());
            album.setCreatedBy(teacher.getId());
            album.setCreatorName(teacher.getName());
            album.setCreatorRole("admin");
            album.setName(albumName.getText().toString());
            switch (albumSpinner.getSelectedItem().toString()){
                case "Album for School":
                    album.setSchoolId(teacher.getSchoolId());
                    album.setClassId(0);
                    album.setSectionId(0);
                    break;
                case "Album for Class":
                    album.setSchoolId(0);
                    album.setClassId(((Clas) classSpinner.getSelectedItem()).getId());
                    album.setSectionId(0);
                    break;
                case "Album for Section":
                    album.setSchoolId(0);
                    album.setClassId(0);
                    album.setSectionId(((Section) sectionSpinner.getSelectedItem()).getId());
                    break;
            }
            presenter.saveAlbum(album);
        }
    }

    private void initAlbumSpinner() {
        String[] albumFor = {"Album for School", "Album for Class", "Album for Section"};
        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, albumFor);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        albumSpinner.setAdapter(adapter);
        albumSpinner.setOnItemSelectedListener(this);
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
    }

    @Override
    public void albumSaved(Album album) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        switch (parent.getId()) {
            case R.id.album_spinner:
                albumTargetVisibility();
                presenter.getClassList(teacher.getSchoolId());
                break;
            case R.id.class_spinner:
                presenter.getSectionList(((Clas) classSpinner.getSelectedItem()).getId());
                break;
            case R.id.section_spinner:
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void albumTargetVisibility() {
        switch (albumSpinner.getSelectedItem().toString()){
            case "Album for School":
                classLayout.setVisibility(View.GONE);
                sectionLayout.setVisibility(View.GONE);
                break;
            case "Album for Class":
                classLayout.setVisibility(View.VISIBLE);
                sectionLayout.setVisibility(View.GONE);
                break;
            case "Album for Section":
                classLayout.setVisibility(View.VISIBLE);
                sectionLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}

package com.shikshitha.admin.gallery;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.album.AlbumActivity;
import com.shikshitha.admin.dao.AlbumDao;
import com.shikshitha.admin.dao.DeletedAlbumDao;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.Clas;
import com.shikshitha.admin.model.DeletedAlbum;
import com.shikshitha.admin.model.Section;
import com.shikshitha.admin.model.Teacher;
import com.shikshitha.admin.newalbum.NewAlbumActivity;
import com.shikshitha.admin.util.NetworkUtil;
import com.shikshitha.admin.util.PermissionUtil;
import com.shikshitha.admin.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity implements GalleryView,
        AdapterView.OnItemSelectedListener,
        ActivityCompat.OnRequestPermissionsResultCallback{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.noAlbums) LinearLayout noAlbums;
    @BindView(R.id.album_spinner) Spinner albumSpinner;
    @BindView(R.id.class_spinner) Spinner classSpinner;
    @BindView(R.id.section_spinner) Spinner sectionSpinner;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Teacher teacher;
    private GalleryPresenter presenter;
    private GalleryAdapter adapter;

    private Album selectedAlbum = new Album();
    private int selectedAlbumPosition, oldSelectedAlbumPosition;
    ActionMode mActionMode;
    boolean isAlbumSelect = false;

    private static final int WRITE_STORAGE_PERMISSION = 333;
    final static int REQ_CODE = 789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        teacher = TeacherDao.getTeacher();

        presenter = new GalleryPresenterImpl(this, new GalleryInteractorImpl());

        setupRecyclerView();

        loadOfflineData();

        initAlbumSpinner();

        if (PermissionUtil.isStoragePermissionGranted(this, WRITE_STORAGE_PERMISSION)) {
            syncGallery();
        }
    }

    private void syncGallery() {
        if(NetworkUtil.isNetworkAvailable(this)) {
            if(adapter.getDataSet().size() == 0) {
                presenter.getAlbums(teacher.getSchoolId());
            } else {
                presenter.getAlbumsAboveId(teacher.getSchoolId(), adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            syncGallery();
        } else {
            showSnackbar("Permission has been denied");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_overflow, menu);
        return true;
    }

    private void loadOfflineData() {
        List<Album> albums = AlbumDao.getAlbums();
        if (albums.size() == 0) {
            noAlbums.setVisibility(View.VISIBLE);
        } else {
            noAlbums.setVisibility(View.INVISIBLE);
            adapter.replaceData(albums, selectedAlbum);
        }
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new GalleryAdapter(getApplicationContext(), teacher.getSchoolId(), new ArrayList<Album>(0));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isAlbumSelect) {
                    single_select(position);
                } else {
                    Intent intent = new Intent(GalleryActivity.this, AlbumActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("album", adapter.getDataSet().get(position));
                    intent.putExtras(args);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isAlbumSelect) {
                    selectedAlbum = new Album();
                    isAlbumSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                    single_select(position);
                }
            }
        }));
    }

    private void initAlbumSpinner() {
        String[] albumFor = {"All Albums","School Album", "Class Album", "Section Album"};
        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, albumFor);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        albumSpinner.setAdapter(adapter);
        albumSpinner.setOnItemSelectedListener(this);
    }

    public void addAlbum(MenuItem item) {
        if (NetworkUtil.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, NewAlbumActivity.class);
            startActivityForResult(intent, REQ_CODE);
        } else {
            showSnackbar("You are offline,check your internet.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (adapter.getDataSet().size() == 0) {
                presenter.getAlbums(teacher.getSchoolId());
            } else {
                presenter.getAlbumsAboveId(teacher.getSchoolId(), adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
            }
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
    public void setAlbum(Album album) {
        if (adapter.getDataSet().size() == 0) {
            presenter.getAlbums(teacher.getSchoolId());
        } else {
            presenter.getAlbumsAboveId(teacher.getSchoolId(), adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
        }
    }

    @Override
    public void albumDeleted() {
        adapter.deleteDataSet(selectedAlbumPosition);
        syncDeletedAlbums();
    }

    @Override
    public void setRecentAlbums(List<Album> albums) {
        adapter.updateDataSet(albums);
        backupAlbums(albums);
        syncDeletedAlbums();
    }

    @Override
    public void setAlbums(List<Album> albums) {
        if (albums.size() == 0) {
            recyclerView.invalidate();
            noAlbums.setVisibility(View.VISIBLE);
        } else {
            noAlbums.setVisibility(View.INVISIBLE);
            adapter.replaceData(albums, selectedAlbum);
            backupAlbums(albums);
        }
        syncDeletedAlbums();
    }

    @Override
    public void showClass(List<Clas> clasList) {
        Clas classHint = new Clas();
        classHint.setId(-1);
        classHint.setClassName("Select Class...");
        clasList.add(0, classHint);
        ArrayAdapter<Clas> adapter = new
            ArrayAdapter<Clas>(this, android.R.layout.simple_spinner_item, clasList) {
                @Override
                public boolean isEnabled(int position){
                    return position != 0;
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){// Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
        classSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void showSection(List<Section> sectionList) {
        Section sectionHint = new Section();
        sectionHint.setId(-1);
        sectionHint.setSectionName("Select Section...");
        sectionList.add(0, sectionHint);
        ArrayAdapter<Section> adapter = new
            ArrayAdapter<Section>(this, android.R.layout.simple_spinner_item, sectionList){
                @Override
                public boolean isEnabled(int position){
                    return position != 0;
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){// Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
        sectionSpinner.setOnItemSelectedListener(this);
    }

    private void backupAlbums(final List<Album> albums) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlbumDao.insert(albums);
            }
        }).start();
    }

    private void syncDeletedAlbums() {
        DeletedAlbum deletedAlbum = DeletedAlbumDao.getNewestDeletedAlbum();
        if (deletedAlbum.getId() == 0) {
            presenter.getDeletedAlbums(teacher.getSchoolId());
        } else {
            presenter.getRecentDeletedAlbums(teacher.getSchoolId(), deletedAlbum.getId());
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
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(GalleryActivity.this);
                    alertDialog.setMessage("Are you sure you want to delete?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectedAlbum = new Album();
                            mActionMode.finish();
                            DeletedAlbum deletedAlbum = new DeletedAlbum();
                            deletedAlbum.setSenderId(teacher.getId());
                            deletedAlbum.setAlbumId(adapter.getDataSet().get(selectedAlbumPosition).getId());
                            deletedAlbum.setSchoolId(teacher.getSchoolId());
                            deletedAlbum.setDeletedAt(System.currentTimeMillis());
                            presenter.deleteAlbum(deletedAlbum);
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isAlbumSelect = false;
            selectedAlbum = new Album();
            adapter.selectedItemChanged(selectedAlbumPosition, selectedAlbum);
        }
    };

    public void single_select(int position) {
        selectedAlbumPosition = position;
        if (mActionMode != null) {
            if (selectedAlbum.getId() == adapter.getDataSet().get(position).getId() ) {
                selectedAlbum = new Album();
                mActionMode.finish();
                adapter.selectedItemChanged(position, selectedAlbum);
            } else {
                selectedAlbum = new Album();
                adapter.selectedItemChanged(oldSelectedAlbumPosition, selectedAlbum);
                oldSelectedAlbumPosition = position;
                selectedAlbum = adapter.getDataSet().get(position);
                adapter.selectedItemChanged(position, selectedAlbum);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        switch (parent.getId()) {
            case R.id.album_spinner:
                albumTargetVisibility();
                break;
            case R.id.class_spinner:
                presenter.getSectionList(((Clas) classSpinner.getSelectedItem()).getId());
                loadClassAlbum();
                break;
            case R.id.section_spinner:
                loadSectionAlbum();
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
            case "All Albums":
                classSpinner.setVisibility(View.GONE);
                sectionSpinner.setVisibility(View.GONE);
                loadOfflineData();
                break;
            case "School Album":
                classSpinner.setVisibility(View.GONE);
                sectionSpinner.setVisibility(View.GONE);
                loadSchoolAlbum();
                break;
            case "Class Album":
                classSpinner.setVisibility(View.VISIBLE);
                sectionSpinner.setVisibility(View.GONE);
                presenter.getClassList(teacher.getSchoolId());
                break;
            case "Section Album":
                classSpinner.setVisibility(View.VISIBLE);
                sectionSpinner.setVisibility(View.VISIBLE);
                presenter.getClassList(teacher.getSchoolId());
                break;
        }
    }

    private void loadSchoolAlbum() {
        List<Album> albums = AlbumDao.getSchoolAlbums(teacher.getSchoolId());
        if(albums.size() == 0) {
            noAlbums.setVisibility(View.VISIBLE);
        } else {
            noAlbums.setVisibility(View.INVISIBLE);
            adapter.replaceData(albums, selectedAlbum);
        }
    }

    private void loadClassAlbum() {
        List<Album> albums = AlbumDao.getClassAlbums(((Clas) classSpinner.getSelectedItem()).getId());
        if(albums.size() == 0) {
            noAlbums.setVisibility(View.VISIBLE);
            adapter.replaceData(new ArrayList<Album>(0), selectedAlbum);
        } else {
            noAlbums.setVisibility(View.INVISIBLE);
            adapter.replaceData(albums, selectedAlbum);
        }
    }

    private void loadSectionAlbum() {
        List<Album> albums = AlbumDao.getSectionAlbums(((Section) sectionSpinner.getSelectedItem()).getId());
        if(albums.size() == 0) {
            noAlbums.setVisibility(View.VISIBLE);
            adapter.replaceData(new ArrayList<Album>(0), selectedAlbum);
        } else {
            noAlbums.setVisibility(View.INVISIBLE);
            adapter.replaceData(albums, selectedAlbum);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}

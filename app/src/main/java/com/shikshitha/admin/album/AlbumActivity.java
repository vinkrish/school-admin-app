package com.shikshitha.admin.album;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.shikshitha.admin.R;
import com.shikshitha.admin.dao.AlbumImageDao;
import com.shikshitha.admin.dao.DeletedAlbumImageDao;
import com.shikshitha.admin.dao.ImageStatusDao;
import com.shikshitha.admin.dao.TeacherDao;
import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.AlbumImage;
import com.shikshitha.admin.model.DeletedAlbumImage;
import com.shikshitha.admin.model.ImageStatus;
import com.shikshitha.admin.model.Teacher;
import com.shikshitha.admin.util.GridSpacingItemDecoration;
import com.shikshitha.admin.util.NetworkUtil;
import com.shikshitha.admin.util.RecyclerItemClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

public class AlbumActivity extends AppCompatActivity implements AlbumView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.compress_image) ImageView compressImage;
    @BindView(R.id.noGroups) LinearLayout noGroups;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Album album;
    private Teacher teacher;
    private AlbumAdapter adapter;
    private AlbumPresenter presenter;

    ActionMode mActionMode;
    boolean isMultiSelect = false;
    ArrayList<AlbumImage> localAlbumImages = new ArrayList<>();
    ArrayList<AlbumImage> multiselect_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            album = (Album) extras.getSerializable("album");
            getSupportActionBar().setTitle(album.getName());
        }

        teacher = TeacherDao.getTeacher();

        presenter = new AlbumPresenterImpl(this, new AlbumInteractorImpl());

        setupRecyclerView();

        loadOfflineData();

        if(NetworkUtil.isNetworkAvailable(this)) {
            if(adapter.getDataSet().size() == 0) {
                presenter.getAlbumImages(album.getId());
            } else {
                presenter.getAlbumImagesAboveId(album.getId(), adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
            }
        }
    }

    private void setupRecyclerView() {
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true));
        adapter = new AlbumAdapter(getApplicationContext(), teacher.getSchoolId(),
                new ArrayList<AlbumImage>(0), new ArrayList<AlbumImage>(0));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) multi_select(position);
                else {
                    Intent intent = new Intent(AlbumActivity.this, ImageSlideActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("album", album);
                    args.putInt("position", position);
                    args.putLong("schoolId", teacher.getSchoolId());
                    intent.putExtras(args);
                    startActivity(intent);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_overflow, menu);
        return true;
    }

    private void loadOfflineData() {
        ArrayList<AlbumImage> albumImages = AlbumImageDao.getAlbumImages(album.getId());
        if(albumImages.size() == 0) {
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
            adapter.setDataSet(albumImages, multiselect_list);
        }
    }

    public void addPhotos(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 12);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            String[] urls = new String[images.size()];
            noGroups.setVisibility(View.GONE);
            compressImage.setVisibility(View.VISIBLE);
            for (int i = 0, l = images.size(); i < l; i++) {
                urls[i] = images.get(i).path;
                File imgFile = new  File(images.get(i).path);
                if(imgFile.exists()){
                    compressImage.setImageBitmap(new Compressor(AlbumActivity.this).compressToBitmap(new File(imgFile.getAbsolutePath())));
                    saveBitmapToFile();
                }
            }
            prepareAlbumImages();
        }
    }

    private void prepareAlbumImages() {
        List<AlbumImage> albumImages = new ArrayList<>();
        ArrayList<ImageStatus> images = ImageStatusDao.getLatestAlbumImages(album.getId());
        for(ImageStatus image: images) {
            AlbumImage albumImage = new AlbumImage();
            albumImage.setName(image.getName());
            albumImage.setAlbumId(album.getId());
            albumImage.setCreatedBy(teacher.getId());
            albumImage.setCreatorName(teacher.getName());
            albumImage.setCreatorRole("teacher");
            albumImage.setCreatedAt(System.currentTimeMillis());
            albumImages.add(albumImage);
        }
        presenter.saveAlbumImages(albumImages);
    }

    private void saveBitmapToFile() {
        try {
            Bitmap selectedBitmap = ((BitmapDrawable) compressImage.getDrawable()).getBitmap();

            String imageName = System.currentTimeMillis() + ".jpg";

            File dir = new File(Environment.getExternalStorageDirectory().getPath(),
                    "Shikshitha/Admin/" + teacher.getSchoolId());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File newFile = new File(dir, imageName);

            newFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(newFile);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);

            ImageStatus imageStatus = new ImageStatus();
            imageStatus.setName(imageName);
            imageStatus.setAlbumId(album.getId());
            imageStatus.setSubAlbumId(0);
            ImageStatusDao.insert(imageStatus);

            compressImage.setImageBitmap(null);
            //compressImage.setImageResource(android.R.color.transparent);

        } catch (Exception e) {
            e.printStackTrace();
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
    public void albumImagesSaved(List<AlbumImage> albumImages) {
        ImageStatusDao.updateAlbumImages(albumImages);
        if(adapter.getDataSet().size() == 0) {
            presenter.getAlbumImages(album.getId());
        } else {
            presenter.getAlbumImagesAboveId(album.getId(), adapter.getDataSet().get(adapter.getItemCount() - 1).getId());
        }

        Intent i = new Intent(this, AlbumUploadService.class);
        i.putExtra("albumId", album.getId());
        startService(i);
    }

    @Override
    public void albumImagesDeleted(ArrayList<DeletedAlbumImage> albumImages) {
        adapter.deleteDataSet();
        mActionMode.setTitle("");
        mActionMode.finish();
        syncDeletedAlbums();
        ImageStatusDao.delete(albumImages);
        for(DeletedAlbumImage deletedAlbumImage: albumImages) {
            File file = new File(Environment.getExternalStorageDirectory().getPath(),
                    "Shikshitha/Admin/" + teacher.getSchoolId() + "/" + deletedAlbumImage.getName());
            if(file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public void setRecentAlbumImages(ArrayList<AlbumImage> albumImages) {
        adapter.updateDataSet(albumImages);
        localAlbumImages = adapter.getDataSet();
        backupAlbumImages(albumImages);
        syncDeletedAlbums();
    }

    @Override
    public void setAlbumImages(ArrayList<AlbumImage> albumImages) {
        localAlbumImages = albumImages;
        if(albumImages.size() == 0) {
            recyclerView.invalidate();
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.INVISIBLE);
            adapter.setDataSet(albumImages, multiselect_list);
            backupAlbumImages(albumImages);
        }
        syncDeletedAlbums();
    }

    @Override
    public void onDeletedAlbumImageSync() {
        presenter.getAlbumUpdate(album.getId());
    }

    private void backupAlbumImages(final List<AlbumImage> albumImages) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlbumImageDao.insert(albumImages);
            }
        }).start();
    }

    private void syncDeletedAlbums() {
        DeletedAlbumImage deletedAlbum = DeletedAlbumImageDao.getNewestDeletedAlbumImage();
        if(deletedAlbum.getId() == 0) {
            presenter.getDeletedAlbumImages(album.getId());
        } else {
            presenter.getRecentDeletedAlbumImages(album.getId(), deletedAlbum.getId());
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
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AlbumActivity.this);
                    alertDialog.setMessage("Are you sure you want to delete?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ArrayList<DeletedAlbumImage> deletedAlbumImages = new ArrayList<>();
                            for(AlbumImage albumImage: multiselect_list){
                                DeletedAlbumImage dai = new DeletedAlbumImage();
                                dai.setAlbumImageId(albumImage.getId());
                                dai.setName(albumImage.getName());
                                dai.setAlbumId(albumImage.getAlbumId());
                                dai.setSenderId(teacher.getId());
                                dai.setDeletedAt(System.currentTimeMillis());
                                deletedAlbumImages.add(dai);
                            }
                            presenter.deleteAlbumImages(deletedAlbumImages);
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
            isMultiSelect = false;
            multiselect_list = new ArrayList<>();
            refreshAdapter();
        }
    };

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(localAlbumImages.get(position)))
                multiselect_list.remove(localAlbumImages.get(position));
            else
                multiselect_list.add(localAlbumImages.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else {
                mActionMode.setTitle("");
                mActionMode.finish();
            }
            refreshAdapter();
        }
    }

    public void refreshAdapter() {
        adapter.setDataSet(localAlbumImages, multiselect_list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}

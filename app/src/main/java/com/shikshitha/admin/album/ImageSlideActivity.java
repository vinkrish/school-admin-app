package com.shikshitha.admin.album;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.shikshitha.admin.R;
import com.shikshitha.admin.api.ApiClient;
import com.shikshitha.admin.api.GalleryApi;
import com.shikshitha.admin.dao.AlbumDao;
import com.shikshitha.admin.dao.AlbumImageDao;
import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.AlbumImage;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageSlideActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.check_box) CheckBox checkBox;
    @BindView(R.id.pager) ViewPager viewPager;

    private Album album;
    private int startPosition;
    private ArrayList<AlbumImage> albumImages;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slide);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            album = (Album) extras.getSerializable("album");
            startPosition = extras.getInt("position");
            getSupportActionBar().setTitle(album.getName());
        }

        albumImages = AlbumImageDao.getAlbumImages(album.getId());
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(imageFragmentPagerAdapter);
        viewPager.setCurrentItem(startPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(!album.getCoverPic().equals("") &&
                        album.getCoverPic().equals(albumImages.get(position).getName())) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()) {
                    album.setCoverPic(albumImages.get(viewPager.getCurrentItem()).getName());
                    updateCoverPic(albumImages.get(viewPager.getCurrentItem()).getName());
                } else {
                    updateCoverPic("");
                }
            }
        });
    }

    private void updateCoverPic(String coverPic) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Album updatedAlbum = new Album();
        updatedAlbum.setCoverPic(coverPic);
        updatedAlbum.setId(album.getId());

        AlbumDao.update(updatedAlbum);

        Call<Void> queue = api.updateAlbum(updatedAlbum);
        queue.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Updated CoverPic", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return albumImages.size();
        }

        @Override
        public Fragment getItem(int position) {
            return SwipeFragment.newInstance(albumImages.get(position).getName(), album.getSchoolId());
        }
    }

    public static class SwipeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.image_swipe, container, false);
            PhotoView imageView = swipeView.findViewById(R.id.imageView);

            Bundle bundle = getArguments();
            String imageName = bundle.getString("imageName");
            long schoolId = bundle.getLong("schoolId");

            File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Admin/" + schoolId );
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, imageName);
            if (file.exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }

            return swipeView;
        }

        static SwipeFragment newInstance(String imageName, long schoolId) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("imageName", imageName);
            bundle.putLong("schoolId", schoolId);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
    }
}

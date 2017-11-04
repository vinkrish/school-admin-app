package com.shikshitha.admin.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.shikshitha.admin.R;
import com.shikshitha.admin.model.AlbumImage;
import com.shikshitha.admin.util.SquareRelativeLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 01-11-2017.
 */

class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private Context context;
    private long schoolId;
    private ArrayList<AlbumImage> selected_items;
    private ArrayList<AlbumImage> items;

    AlbumAdapter(Context context, long schoolId, ArrayList<AlbumImage> items, ArrayList<AlbumImage> selected_items) {
        this.context = context;
        this.schoolId = schoolId;
        this.items = items;
        this.selected_items = selected_items;
    }

    ArrayList<AlbumImage> getDataSet() {
        return items;
    }

    @UiThread
    void setDataSet(ArrayList<AlbumImage> items, ArrayList<AlbumImage> selected_items) {
        this.items = items;
        this.selected_items = selected_items;
        notifyDataSetChanged();
    }

    @UiThread
    void updateDataSet(List<AlbumImage> itms) {
        int pos = items.size();
        this.items.addAll(itms);
        notifyItemRangeInserted(pos, items.size() - 1);
    }

    @UiThread
    void deleteDataSet() {
        for(int i=items.size()-1; i>=0; i--) {
            for(int j = 0; j < selected_items.size(); j++) {
                if(items.get(i).getId() == selected_items.get(j).getId()) {
                    this.selected_items.remove(j);
                    this.items.remove(i);
                    notifyItemRemoved(i);
                    break;
                }
            }
        }

    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new AlbumAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.album_img) ImageView albumImage;
        @BindView(R.id.selected) FrameLayout selected;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final AlbumImage albumImage) {
            setImage(albumImage.getName());
            if (selected_items.contains(albumImage)) {
                selected.setVisibility(View.VISIBLE);
            } else {
                selected.setVisibility(View.GONE);
            }
        }

        private void setImage(String coverPhoto) {
            File dir = new File(Environment.getExternalStorageDirectory().getPath(), "Shikshitha/Admin/" + schoolId);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, coverPhoto);
            if (file.exists()) {
                albumImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } else {
                Picasso.with(context)
                        .load("https://s3.ap-south-1.amazonaws.com/shikshitha-images/" + schoolId + "/" + coverPhoto)
                        .placeholder(R.drawable.placeholder)
                        .into(albumImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable) albumImage.getDrawable()).getBitmap();
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError() {
                                albumImage.setImageResource(R.drawable.placeholder);
                            }
                        });
            }
        }

    }
}

package com.shikshitha.admin.model;

/**
 * Created by Vinay on 02-11-2017.
 */

public class ImageStatus {
    private long id;
    private String name;
    private long albumId;
    private long subAlbumId;
    private boolean sync;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getSubAlbumId() {
        return subAlbumId;
    }

    public void setSubAlbumId(long subAlbumId) {
        this.subAlbumId = subAlbumId;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

}

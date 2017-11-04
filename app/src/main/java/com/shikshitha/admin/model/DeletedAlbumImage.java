package com.shikshitha.admin.model;

public class DeletedAlbumImage {
    private long id;
    private long senderId;
    private long albumId;
    private long albumImageId;
    private String name;
    private long deletedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getAlbumImageId() {
        return albumImageId;
    }

    public void setAlbumImageId(long albumImageId) {
        this.albumImageId = albumImageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(long deletedAt) {
        this.deletedAt = deletedAt;
    }

}

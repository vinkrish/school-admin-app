package com.shikshitha.admin.model;

public class DeletedSubAlbum {
    private long id;
    private long senderId;
    private long albumId;
    private long subAlbumId;
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

    public long getSubAlbumId() {
        return subAlbumId;
    }

    public void setSubAlbumId(long subAlbumId) {
        this.subAlbumId = subAlbumId;
    }

    public long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(long deletedAt) {
        this.deletedAt = deletedAt;
    }

}

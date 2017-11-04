package com.shikshitha.admin.model;

public class DeletedSubAlbumImage {
    private long id;
    private long senderId;
    private long subAlbumId;
    private long subAlbumImageId;
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

    public long getSubAlbumId() {
        return subAlbumId;
    }

    public void setSubAlbumId(long subAlbumId) {
        this.subAlbumId = subAlbumId;
    }

    public long getSubAlbumImageId() {
        return subAlbumImageId;
    }

    public void setSubAlbumImageId(long subAlbumImageId) {
        this.subAlbumImageId = subAlbumImageId;
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

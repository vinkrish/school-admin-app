package com.shikshitha.admin.api;

import com.shikshitha.admin.model.Album;
import com.shikshitha.admin.model.AlbumImage;
import com.shikshitha.admin.model.DeletedAlbum;
import com.shikshitha.admin.model.DeletedAlbumImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Vinay on 29-10-2017.
 */

public interface GalleryApi {

    @POST("album/new")
    Call<Album> saveAlbum(@Body Album album);

    @PUT("album")
    Call<Void> updateAlbum(@Body Album album);

    @GET("album/{albumId}")
    Call<Album> getAlbum(@Path("albumId") long albumId);

    @GET("album/all/{id}/school/{schoolId}")
    Call<List<Album>> getAlbumAboveId(@Path("schoolId") long schoolId,
                                      @Path("id") long id);

    @GET("album/all/school/{schoolId}")
    Call<List<Album>> getAlbums(@Path("schoolId") long schoolId);

    @POST("deletedalbum/new")
    Call<DeletedAlbum> deleteAlbum(@Body DeletedAlbum deletedAlbum);

    @GET("deletedalbum/all/{id}/school/{schoolId}")
    Call<List<DeletedAlbum>> getDeletedAlbumsAboveId(@Path("schoolId") long schoolId,
                                                     @Path("id") long id);

    @GET("deletedalbum/all/school/{schoolId}")
    Call<List<DeletedAlbum>> getDeletedAlbums(@Path("schoolId") long schoolId);

    @POST("ai")
    Call<Void> saveAlbumImages(@Body List<AlbumImage> albumImages);

    @GET("ai/{id}/album/{albumId}")
    Call<ArrayList<AlbumImage>> getAlbumImagesAboveId(@Path("albumId") long albumId,
                                                 @Path("id") long id);

    @GET("ai/album/{albumId}")
    Call<ArrayList<AlbumImage>> getAlbumImages(@Path("albumId") long albumId);

    @POST("deletedai")
    Call<Void> deleteAlbumImages(@Body List<DeletedAlbumImage> deletedAlbumImages);

    @GET("deletedai/{id}/album/{albumId}")
    Call<List<DeletedAlbumImage>> getDeletedAlbumImagesAboveId(@Path("albumId") long albumId,
                                                               @Path("id") long id);

    @GET("deletedai/album/{albumId}")
    Call<List<DeletedAlbumImage>> getDeletedAlbumImages(@Path("albumId") long albumId);

}

package com.xtracker.android.rest;

import com.xtracker.android.objects.Point;
import com.xtracker.android.objects.Track;

import java.sql.Timestamp;
import java.util.List;

import com.xtracker.android.objects.Track;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ApiService {
    @GET("/rest/login")
    public void login(@Query("email") String email, @Query("access_token") String access_token, Callback<Keys> callback);

    @GET("/rest/tracks")
    public void getTracksList(@Query("user_id") long user_id, @Query("hmac") String hmac, Callback<List<Track>> callback);

    @GET("/rest/tracks/{track_id}")
    public void getTrack(@Path("track_id") long track_id, @Query("user_id") long user_id, @Query("hmac") String hmac, Callback<Track> callback);

    @POST("/rest/tracks")
    public void addTrack(@Body Track track, @Header("user_id") long user_id, @Header("hmac") String hmac, Callback<Long> callback);

    @DELETE("/rest/tracks/{track_id}")
    public void deleteTrack(@Path("track_id") long track_id, String access_token);


    static class Keys {
        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public long getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(long publicKey) {
            this.publicKey = publicKey;
        }

        private String privateKey;
        private long publicKey;

    }
}

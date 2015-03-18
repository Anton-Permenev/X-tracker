package com.xtracker.android.rest;

import com.xtracker.android.objects.Point;
import com.xtracker.android.objects.Track;

import java.sql.Timestamp;
import java.util.List;

import com.xtracker.android.objects.Track;

import retrofit.http.DELETE;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface ApiService {
    @GET("rest/login")
    public String login(String email,String token);

    @GET("rest/tracks")
    public List<Track> getTracksList(long user_id, String access_token);

    @GET("rest/tracks/{track_id}")
    public Track getTrack(@Path("track_id") long track_id, String access_token);

    @POST("rest/tracks")
    public void addTrack(List<Point> points, Timestamp time_start, Timestamp time_end, String access_token);

    @DELETE("rest/tracks/{track_id}")
    public void deleteTrack(@Path("track_id") long track_id, String access_token);
}

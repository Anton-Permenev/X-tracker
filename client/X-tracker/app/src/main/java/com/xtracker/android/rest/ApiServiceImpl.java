package com.xtracker.android.rest;

import com.xtracker.android.objects.Point;
import com.xtracker.android.objects.Track;

import java.sql.Timestamp;
import java.util.List;

public class ApiServiceImpl implements ApiService {
    @Override
    public List<Track> getTracksList(long user_id, String access_token) {
        return null;
    }

    @Override
    public void newUser(String email, String access_token) {

    }

    @Override
    public Track getTrack(long track_id, String access_token) {
        // Don't forget about null track_id for new track
        return null;
    }

    @Override
    public void updateTrack(long track_id, String access_token) {

    }

    @Override
    public void addTrack(List<Point> points, Timestamp time_start, Timestamp time_end, String access_token) {

    }

    @Override
    public void editTrack(long track_id, List<Point> points, Timestamp time_start, Timestamp time_end, String access_token) {

    }

    @Override
    public void deleteTrack(long track_id, String access_token) {

    }
}

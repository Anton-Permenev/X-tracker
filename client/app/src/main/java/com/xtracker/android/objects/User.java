package com.xtracker.android.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya on 09.03.2015.
 */
public class User {

    private long userId;
    private String token;
    private String email;
    private List<Track> tracks = new ArrayList<>();

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User user = (User) o;
            return user.getUserId() == this.getUserId();
        } else
            return false;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}


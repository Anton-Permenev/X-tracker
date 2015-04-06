package com.xtracker.android.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya on 09.03.2015.
 */
public class Track implements Serializable{
    private long trackId;

    private Timestamp timeStart;

    private Timestamp timeEnd;
    private List<Jump> jumps = new ArrayList<>();
    private List<Point> points = new ArrayList<>();
    private User user;

    private String title;
    private String description;

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Track) {
            Track track = (Track) o;
            return track.getTrackId() == this.getTrackId();
        } else
            return false;
    }

    public List<Jump> getJumps() {
        return jumps;
    }

    public void setJumps(List<Jump> jumps) {
        this.jumps = jumps;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addPoint(Point point){
        this.points.add(point);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


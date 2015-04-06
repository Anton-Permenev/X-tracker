package com.xtracker.backend.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@XmlRootElement
@Table(name = "tracks", schema = "public", catalog = "xtracker")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private long trackId;
    @Basic
    @Column(name = "time_start")
    private Timestamp timeStart;
    @Basic
    @Column(name = "time_end")
    private Timestamp timeEnd;
    @Basic
    @Column(name="title")
    private String title;
    @Basic
    @Column(name="description")
    private String description;
    @OneToMany(mappedBy = "track")
    private List<Jump> jumps = new ArrayList<>();
    @OneToMany(mappedBy = "track", cascade = CascadeType.PERSIST)
    private List<Point> points = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

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

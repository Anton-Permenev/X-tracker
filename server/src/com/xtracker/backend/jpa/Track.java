package com.xtracker.backend.jpa;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tracks", schema = "public", catalog = "xtracker")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id", nullable = false, insertable = true, updatable = true)
    private long trackId;
    @Basic
    @Column(name = "user_id", nullable = true, insertable = true, updatable = true)
    private Long userId;
    @Basic
    @Column(name = "time_start", nullable = true, insertable = true, updatable = true)
    private Timestamp timeStart;
    @Basic
    @Column(name = "time_end", nullable = true, insertable = true, updatable = true)
    private Timestamp timeEnd;
    @OneToMany(mappedBy = "track")
    private List<Jump> jumps;
    @OneToMany(mappedBy = "track"/*, cascade = CascadeType.PERSIST*/)
    private List<Point> points = new ArrayList<>();
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        if (!user.getTracks().contains(this)) {
            user.getTracks().add(this);
        }
    }
}

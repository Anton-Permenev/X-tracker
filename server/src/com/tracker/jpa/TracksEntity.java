package com.tracker.jpa;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "tracks", schema = "xtracker", catalog = "x-tracker")
public class TracksEntity {
    private long trackId;
    private Long userId;
    private Timestamp timeStart;
    private Timestamp timeEnd;
    private Collection<JumpsEntity> jumpsesByTrackId;
    private Collection<PointsEntity> pointsesByTrackId;
    private UsersEntity usersByUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id", nullable = false, insertable = true, updatable = true)
    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    @Basic
    @Column(name = "user_id", nullable = true, insertable = true, updatable = true)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "time_start", nullable = true, insertable = true, updatable = true)
    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    @Basic
    @Column(name = "time_end", nullable = true, insertable = true, updatable = true)
    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TracksEntity that = (TracksEntity) o;

        if (trackId != that.trackId) return false;
        if (timeEnd != null ? !timeEnd.equals(that.timeEnd) : that.timeEnd != null) return false;
        if (timeStart != null ? !timeStart.equals(that.timeStart) : that.timeStart != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (trackId ^ (trackId >>> 32));
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (timeStart != null ? timeStart.hashCode() : 0);
        result = 31 * result + (timeEnd != null ? timeEnd.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "tracksByTrackId")
    public Collection<JumpsEntity> getJumpsesByTrackId() {
        return jumpsesByTrackId;
    }

    public void setJumpsesByTrackId(Collection<JumpsEntity> jumpsesByTrackId) {
        this.jumpsesByTrackId = jumpsesByTrackId;
    }

    @OneToMany(mappedBy = "tracksByTrackId")
    public Collection<PointsEntity> getPointsesByTrackId() {
        return pointsesByTrackId;
    }

    public void setPointsesByTrackId(Collection<PointsEntity> pointsesByTrackId) {
        this.pointsesByTrackId = pointsesByTrackId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    public UsersEntity getUsersByUserId() {
        return usersByUserId;
    }

    public void setUsersByUserId(UsersEntity usersByUserId) {
        this.usersByUserId = usersByUserId;
    }
}

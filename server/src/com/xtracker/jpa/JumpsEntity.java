package com.xtracker.jpa;

import javax.persistence.*;

@Entity
@Table(name = "jumps", schema = "xtracker", catalog = "x-tracker")
public class JumpsEntity {
    private long jumpId;
    private Long trackId;
    private Integer airTime;
    private TracksEntity tracksByTrackId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jump_id", nullable = false, insertable = true, updatable = true)
    public long getJumpId() {
        return jumpId;
    }

    public void setJumpId(long jumpId) {
        this.jumpId = jumpId;
    }

    @Basic
    @Column(name = "track_id", nullable = true, insertable = true, updatable = true)
    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    @Basic
    @Column(name = "air_time", nullable = true, insertable = true, updatable = true)
    public Integer getAirTime() {
        return airTime;
    }

    public void setAirTime(Integer airTime) {
        this.airTime = airTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JumpsEntity that = (JumpsEntity) o;

        if (jumpId != that.jumpId) return false;
        if (airTime != null ? !airTime.equals(that.airTime) : that.airTime != null) return false;
        if (trackId != null ? !trackId.equals(that.trackId) : that.trackId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (jumpId ^ (jumpId >>> 32));
        result = 31 * result + (trackId != null ? trackId.hashCode() : 0);
        result = 31 * result + (airTime != null ? airTime.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "track_id", referencedColumnName = "track_id")
    public TracksEntity getTracksByTrackId() {
        return tracksByTrackId;
    }

    public void setTracksByTrackId(TracksEntity tracksByTrackId) {
        this.tracksByTrackId = tracksByTrackId;
    }
}

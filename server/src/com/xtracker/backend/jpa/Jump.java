package com.xtracker.backend.jpa;

import javax.persistence.*;

@Entity
@Table(name = "jumps", schema = "public", catalog = "xtracker")
public class Jump {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jump_id", nullable = false, insertable = true, updatable = true)
    private long jumpId;
    @Basic
    @Column(name = "track_id", nullable = true, insertable = true, updatable = true)
    private Long trackId;
    @Basic
    @Column(name = "air_time", nullable = true, insertable = true, updatable = true)
    private Integer airTime;
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "track_id", referencedColumnName = "track_id")
    private Track track;

    public long getJumpId() {
        return jumpId;
    }

    public void setJumpId(long jumpId) {
        this.jumpId = jumpId;
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public Integer getAirTime() {
        return airTime;
    }

    public void setAirTime(Integer airTime) {
        this.airTime = airTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Jump) {
            Jump track = (Jump) o;
            return track.getJumpId() == this.getJumpId();
        } else
            return false;
    }


    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}

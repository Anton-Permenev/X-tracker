package com.xtracker.backend.jpa;

import javax.persistence.*;

@Entity
@Table(name = "points", schema = "public", catalog = "xtracker")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id", nullable = false, insertable = true, updatable = true)
    private long pointId;
    @Basic
    @Column(name = "acceleration", nullable = true, insertable = true, updatable = true, precision = 8)
    private Float acceleration;
    @Basic
    @Column(name = "lat", nullable = true, insertable = true, updatable = true, precision = 8)
    private Float lat;
    @Basic
    @Column(name = "lon", nullable = true, insertable = true, updatable = true, precision = 8)
    private Float lon;
    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }
    /*public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }*/

    public Float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Float acceleration) {
        this.acceleration = acceleration;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point track = (Point) o;
            return track.getPointId() == this.getPointId();
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

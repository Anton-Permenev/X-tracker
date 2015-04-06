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
    private double acceleration;
    @Basic
    @Column(name = "lat", nullable = true, insertable = true, updatable = true, precision = 8)
    private double lat;
    @Basic
    @Column(name = "lon", nullable = true, insertable = true, updatable = true, precision = 8)
    private double lon;
    @Basic
    @Column(name = "speed", nullable = true, insertable = true, updatable = true, precision = 8)
    private float speed;
    @Basic
    @Column(name = "height", nullable = true, insertable = true, updatable = true)
    private double height;

    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
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

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }
}

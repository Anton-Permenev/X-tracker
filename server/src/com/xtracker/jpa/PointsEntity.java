package com.xtracker.jpa;

import javax.persistence.*;

@Entity
@Table(name = "points", schema = "xtracker", catalog = "x-tracker")
public class PointsEntity {
    private long pointId;
    private Long trackId;
    private Float acceleration;
    private Float lat;
    private Float lon;
    private TracksEntity tracksByTrackId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id", nullable = false, insertable = true, updatable = true)
    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
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
    @Column(name = "acceleration", nullable = true, insertable = true, updatable = true, precision = 8)
    public Float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Float acceleration) {
        this.acceleration = acceleration;
    }

    @Basic
    @Column(name = "lat", nullable = true, insertable = true, updatable = true, precision = 8)
    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    @Basic
    @Column(name = "lon", nullable = true, insertable = true, updatable = true, precision = 8)
    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointsEntity that = (PointsEntity) o;

        if (pointId != that.pointId) return false;
        if (acceleration != null ? !acceleration.equals(that.acceleration) : that.acceleration != null) return false;
        if (lat != null ? !lat.equals(that.lat) : that.lat != null) return false;
        if (lon != null ? !lon.equals(that.lon) : that.lon != null) return false;
        if (trackId != null ? !trackId.equals(that.trackId) : that.trackId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (pointId ^ (pointId >>> 32));
        result = 31 * result + (trackId != null ? trackId.hashCode() : 0);
        result = 31 * result + (acceleration != null ? acceleration.hashCode() : 0);
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (lon != null ? lon.hashCode() : 0);
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

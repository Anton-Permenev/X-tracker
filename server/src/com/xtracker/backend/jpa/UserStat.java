package com.xtracker.backend.jpa;

import javax.persistence.*;

@Entity
@Table(name = "user_stats", schema = "public", catalog = "xtracker")
public class UserStat {
    @Basic
    @Column(name = "total_distance", nullable = false, insertable = true, updatable = true, precision = 17)
    private double totalDistance;
    @Basic
    @Column(name = "avg_distance", nullable = false, insertable = true, updatable = true, precision = 17)
    private double avgDistance;
    @Basic
    @Column(name = "max_distance", nullable = false, insertable = true, updatable = true, precision = 17)
    private double maxDistance;
    @Basic
    @Column(name = "max_speed", nullable = false, insertable = true, updatable = true, precision = 17)
    private double maxSpeed;
    @Basic
    @Column(name = "avg_speed", nullable = false, insertable = true, updatable = true, precision = 17)
    private double avgSpeed;
    @Basic
    @Column(name = "avg_duration", nullable = false, insertable = true, updatable = true, precision = 17)
    private double avgDuration;
    @Basic
    @Column(name = "max_duration", nullable = false, insertable = true, updatable = true, precision = 17)
    private double maxDuration;
    @Id
    @Column(name = "user_id")
    private long userId;

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getAvgDistance() {
        return avgDistance;
    }

    public void setAvgDistance(double avgDistance) {
        this.avgDistance = avgDistance;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(double avgDuration) {
        this.avgDuration = avgDuration;
    }

    public double getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(double maxDuration) {
        this.maxDuration = maxDuration;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserStat userStat = (UserStat) o;

        if (Double.compare(userStat.totalDistance, totalDistance) != 0) return false;
        if (Double.compare(userStat.avgDistance, avgDistance) != 0) return false;
        if (Double.compare(userStat.maxDistance, maxDistance) != 0) return false;
        if (Double.compare(userStat.maxSpeed, maxSpeed) != 0) return false;
        if (Double.compare(userStat.avgSpeed, avgSpeed) != 0) return false;
        if (userId != userStat.userId) return false;

        return true;
    }

    public void addDistance(double length) {
        totalDistance += length;
    }
}

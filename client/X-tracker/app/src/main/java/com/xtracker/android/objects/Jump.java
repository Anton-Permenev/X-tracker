package com.xtracker.android.objects;

public class Jump {
    private long jumpId;
    private Integer airTime;
    private Track track;

    public long getJumpId() {
        return jumpId;
    }

    public void setJumpId(long jumpId) {
        this.jumpId = jumpId;
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


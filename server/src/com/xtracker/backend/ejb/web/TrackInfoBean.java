package com.xtracker.backend.ejb.web;

import com.google.gson.Gson;
import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.jpa.Point;
import com.xtracker.backend.jpa.Track;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.sql.SQLException;
import java.util.List;


@ManagedBean
@SessionScoped
public class TrackInfoBean {
    private long trackId;
    private Track track;
    private List<Point> pointList;

    @EJB
    private ORMBean ormBean;

    public void setOrmBean(ORMBean ormBean) {
        this.ormBean = ormBean;
    }


    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) throws SQLException {
        this.trackId = trackId;
        track = ormBean.getTrack(trackId);
        pointList = track.getPoints();
        System.out.println("track received:" + trackId);
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public String getPointsAsJson() {
        if (track != null) {
            for (Point point : pointList) {
                point.setTrack(null);
            }
            return new Gson().toJson(pointList);
        } else {
            return null;
        }

    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }
}

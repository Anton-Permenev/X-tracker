package com.xtracker.backend.ejb.web;

import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.jpa.Track;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.sql.SQLException;
import java.util.List;

@ManagedBean
@SessionScoped
public class TrackListBean {

    @EJB
    private ORMBean ormBean;

    public void setOrmBean(ORMBean ormBean) {
        this.ormBean = ormBean;
    }

    private List<Track> trackList;

    private long userId;

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) throws SQLException {
        this.userId = userId;
        trackList=ormBean.getTracks(userId);
    }
}

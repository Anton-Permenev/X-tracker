package com.xtracker.backend.ejb.web;

import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.jpa.Track;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.sql.Timestamp;
import java.util.List;

@ManagedBean
@SessionScoped
public class TrackListBean {

    @ManagedProperty(value = "#{ormBean}")
    private ORMBean ormBean;

    public void setOrmBean(ORMBean ormBean) {
        this.ormBean = ormBean;
    }

    private List<Track> trackList;
    private Track selected;

    private long id;
    private String title;
    private Timestamp timeEnd;
    private Timestamp timeStart;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Track getSelected() {
        return selected;
    }

    public void setSelected(Track selected) {
        this.selected = selected;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }
}

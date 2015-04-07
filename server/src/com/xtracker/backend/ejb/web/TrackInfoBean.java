package com.xtracker.backend.ejb.web;

import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.jpa.Track;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class TrackInfoBean {
    private Track track;


    @ManagedProperty(value="#{ormBean}")
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
}

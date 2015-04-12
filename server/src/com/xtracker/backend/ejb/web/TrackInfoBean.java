package com.xtracker.backend.ejb.web;

import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.jpa.Track;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.sql.SQLException;

@ManagedBean
@SessionScoped
public class TrackInfoBean {
    private long trackId;
    private Track track;

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
        track =ormBean.getTrack(trackId);
    }
}

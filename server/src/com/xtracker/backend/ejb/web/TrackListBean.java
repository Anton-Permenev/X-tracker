package com.xtracker.backend.ejb.web;

import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.jpa.Point;
import com.xtracker.backend.jpa.Track;
import com.xtracker.backend.jpa.User;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name="trackListBean", eager=true)
@SessionScoped
public class TrackListBean {

    @EJB
    private ORMBean ormBean;

    public void setOrmBean(ORMBean ormBean) {
        this.ormBean = ormBean;
    }

    private List<Track> trackList;

    private User user;
    private String email;
    private List<Point> pointList;


    public TrackListBean() {

    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }

    public List<Track> getTrackList() {
        trackList=user.getTracks();
        return trackList;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        user=ormBean.getUser(email);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

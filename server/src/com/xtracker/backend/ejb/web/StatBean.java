package com.xtracker.backend.ejb.web;

import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.jpa.User;
import com.xtracker.backend.jpa.UserStat;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="statBean", eager = true)
@SessionScoped
public class StatBean {
    @EJB
    private ORMBean ormBean;

    private UserStat stat;

    private String email;

    private TrackListBean trackListBean;
    private User user;

    public StatBean() {
    }

    public void loadStats() {
        user = trackListBean.getUser();
        if (user == null) {
            user = ormBean.getUser(email);
        }
        stat = user.getUserStat();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public UserStat getStat() {
        return stat;
    }

    public void setStat(UserStat stat) {
        this.stat = stat;
    }

    public TrackListBean getTrackListBean() {
        return trackListBean;
    }

    public void setTrackListBean(TrackListBean trackListBean) {
        this.trackListBean = trackListBean;
    }
}

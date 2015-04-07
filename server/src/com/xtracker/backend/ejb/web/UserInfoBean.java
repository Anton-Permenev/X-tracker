package com.xtracker.backend.ejb.web;

import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.jpa.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class UserInfoBean {

    @ManagedProperty(value="#{ormBean}")
    private ORMBean ormBean;
    public void setOrmBean(ORMBean ormBean) {
        this.ormBean = ormBean;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

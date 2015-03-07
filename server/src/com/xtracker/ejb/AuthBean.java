package com.xtracker.ejb;


import com.xtracker.backend.jpa.User;

import javax.ejb.Stateful;

@Stateful
public class AuthBean {


    private long id;
    private User user;

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
}

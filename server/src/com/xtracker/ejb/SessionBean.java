package com.xtracker.ejb;


import com.xtracker.jpa.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "ExampleBeanEJB")
public class SessionBean {

    @PersistenceContext
    EntityManager em;

    public SessionBean() {
        User user = new User();
        em.persist(user);
    }
}

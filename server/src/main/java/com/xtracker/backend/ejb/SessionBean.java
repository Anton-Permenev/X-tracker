package com.xtracker.backend.ejb;


import com.xtracker.backend.jpa.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "ExampleBeanEJB")
public class SessionBean {

    @PersistenceContext(unitName = "session")
    EntityManager em;

    public SessionBean() {
        User user = new User();
        //em.persist(user);
    }
}

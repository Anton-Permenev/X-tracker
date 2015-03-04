package com.xtracker.ejb;

import com.xtracker.jpa.UsersEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "ExampleBeanEJB")
public class SessionBean {

    @PersistenceContext
    EntityManager em;

    public SessionBean() {
        UsersEntity usersEntity = new UsersEntity();
        em.persist(usersEntity);
    }
}

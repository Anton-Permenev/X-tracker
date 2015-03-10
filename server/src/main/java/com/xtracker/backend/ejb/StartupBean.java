package com.xtracker.backend.ejb;


import com.xtracker.backend.jpa.Point;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;

@Singleton
@Startup
public class StartupBean {

    @EJB(beanName = "orm")
    ORMBean ormBean;

    @PostConstruct
    public void postConstruct() {

        ArrayList<Point> points = new ArrayList<>();
        points.add( ormBean.makePoint(0.1f, 0.2f, 0.3f));
        points.add( ormBean.makePoint(0.2f, 0.3f, 0.4f));
        points.add( ormBean.makePoint(0.5f, 0.6f, 0.4f));
        ormBean.addTrack(points, null, null);

    }
}

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
        points.add( ormBean.makePoint(0.1, 0.2, 0.3,0.1f));
        points.add( ormBean.makePoint(0.2, 0.3, 0.4,0.1f));
        points.add( ormBean.makePoint(0.5, 0.6, 0.4,0.1f));
        ormBean.addTrack(points, null, null);

    }
}

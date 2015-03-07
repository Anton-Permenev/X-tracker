package com.xtracker.ejb;


import com.xtracker.jpa.Point;

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
        Point point = ormBean.makePoint(0.1f, 0.2f, 0.3f);

        ArrayList<Point> points = new ArrayList<>();
        points.add(point);
        ormBean.addTrack(points, null, null);
    }
}

package com.xtracker.backend.ejb;

import com.xtracker.backend.jpa.Point;
import com.xtracker.backend.jpa.Track;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Stateless(name = "orm")
public class ORMBean {

    @PersistenceContext
    EntityManager em;

    public ORMBean() {

    }

    public void addTrack(List<Point> points, Timestamp timeStart, Timestamp timeEnd) {
        Track track = new Track();

        for (Point point : points) {
            point.setTrack(track);
            track.setPoints(points);
        }

        //saveTrack(track, timeStart, timeEnd);
        //points.forEach(em::persist);

        Track t = new Track();
        em.persist(t);
        em.flush();
        System.out.println(t.getTrackId());
        Point p = new Point();
        //p.setTrack(t);
        //p.setTrackId(25l);
        t.getPoints().add(p);
        p.setTrack(t);
        System.out.println(p.getTrackId());
        em.persist(p);

        //t.setPoints(new ArrayList<>());
        em.merge(p);
       // em.merge(t);
        em.flush();


    }

    public void editTrack(long trackId, Timestamp timeStart, Timestamp timeEnd) throws SQLException {
        saveTrack(getTrack(trackId), timeStart, timeEnd);
    }

    public void deleteTrack(long trackId) throws SQLException {
        em.remove(getTrack(trackId));
    }

    private Track getTrack(long trackId) throws SQLException {
        Track track = em.find(Track.class, trackId);
        if (track == null)
            throw new SQLException("A track with given id is not presented in the database.");
        return track;
    }

    private void saveTrack(Track track, Timestamp timeStart, Timestamp timeEnd){
        track.setTimeStart(timeStart);
        track.setTimeEnd(timeEnd);
        em.persist(track);

    }

    public Point makePoint(float acceleration, float lat, float lon) {
        Point point = new Point();
        point.setAcceleration(acceleration);
        point.setLat(lat);
        point.setLon(lon);
        return point;
    }



}

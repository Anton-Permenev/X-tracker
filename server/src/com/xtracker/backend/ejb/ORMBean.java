package com.xtracker.backend.ejb;

import com.xtracker.backend.jpa.Point;
import com.xtracker.backend.jpa.Track;
import com.xtracker.backend.jpa.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Stateless(name = "orm")
public class ORMBean {

    @PersistenceContext()
    EntityManager em;

   // @EJB
    //AuthBean authBean;


    public ORMBean() {

    }

    /**
     * Creates and persists Track entity and its Points.
     * @param points a list containing points associated with this track
     * @see #makePoint
     */
    public void addTrack(List<Point> points, Timestamp timeStart, Timestamp timeEnd) {
        Track track = new Track();

        for (Point point : points) {
            point.setTrack(track);
            track.getPoints().add(point);
        }
        saveTrack(track, timeStart, timeEnd);
        detectJumps(track);
    }

    public void editTrack(long trackId, Timestamp timeStart, Timestamp timeEnd) throws SQLException {
        saveTrack(getTrack(trackId), timeStart, timeEnd);
    }

    public void deleteTrack(long trackId) throws SQLException {
        em.remove(getTrack(trackId));
    }

    public Track getTrack(long trackId) throws SQLException {
        Track track = em.find(Track.class, trackId);
        if (track == null)
            throw new SQLException("A track with given id is not presented in the database.");
        return track;
    }

    public List<Track> getTracks(long userId) throws SQLException {
        return getUser(userId).getTracks();
    }


    /**
     * Creates Point entity.
     * @return entity object
     */
    public Point makePoint(double acceleration, double lat, double lon, float speed) {
        Point point = new Point();
        point.setAcceleration(acceleration);
        point.setLat(lat);
        point.setLon(lon);
        point.setSpeed(speed);
        return point;
    }

    public User registerUser(String email, String token) {
        User user = new User();
        user.setEmail(email);
        user.setToken(token);
        em.persist(user);
        return user;
    }

    private void saveTrack(Track track, Timestamp timeStart, Timestamp timeEnd){
        track.setTimeStart(timeStart);
        track.setTimeEnd(timeEnd);
        //track.setUser(authBean.getUser());
        em.persist(track);
    }

    private User getUser(long id) throws SQLException {
        User user = em.find(User.class, id);
        if (user == null)
            throw new SQLException("A user with given id does not exist.");
        return user;

    }

    private void detectJumps(Track track) {
    }


}

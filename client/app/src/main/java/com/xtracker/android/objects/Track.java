package com.xtracker.android.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.xtracker.android.caching.DaoSession;
import com.xtracker.android.caching.JumpDao;
import com.xtracker.android.caching.PointDao;
import com.xtracker.android.caching.TrackDao;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TRACK.
 */
public class Track implements Serializable{

    private Long trackId;
    private String timeStart;
    private String timeEnd;
    private String title;
    private String description;
    private Long userId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TrackDao myDao;

    private List<Point> points = new ArrayList<>();
    private List<Jump> jumps = new ArrayList<>();

    public Track() {
    }

    public Track(Long trackId) {
        this.trackId = trackId;
    }

    public Track(Long trackId, String timeStart, String timeEnd, String title, String description, Long userId) {
        this.trackId = trackId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTrackDao() : null;
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Point> getPoints() {
        if (points == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PointDao targetDao = daoSession.getPointDao();
            List<Point> pointsNew = targetDao._queryTrack_Points(trackId);
            synchronized (this) {
                if(points == null) {
                    points = pointsNew;
                }
            }
        }
        return points;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetPoints() {
        points = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Jump> getJumps() {
        if (jumps == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            JumpDao targetDao = daoSession.getJumpDao();
            List<Jump> jumpsNew = targetDao._queryTrack_Jumps(trackId);
            synchronized (this) {
                if(jumps == null) {
                    jumps = jumpsNew;
                }
            }
        }
        return jumps;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetJumps() {
        jumps = null;
    }

    /** Convenient call for {@link de.greenrobot.dao.AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link de.greenrobot.dao.AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link de.greenrobot.dao.AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    public void addPoint(Point point) {
        points.add(point);
    }
}

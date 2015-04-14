package com.xtracker.android.caching;

import android.content.Context;

import com.xtracker.android.objects.Point;
import com.xtracker.android.objects.Track;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class CacheManager {
    private static CacheManager INSTANCE;
    private DaoMaster.DevOpenHelper db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TrackDao trackDao;
    private PointDao pointDao;
    private boolean isInitialized = false;

    public void initialize(Context context) {
        if (!isInitialized) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
            db = new DaoMaster.DevOpenHelper(context, "tracksCache", null);
            daoMaster = new DaoMaster(db.getWritableDatabase());
            daoSession = daoMaster.newSession();
            trackDao = daoSession.getTrackDao();
            pointDao = daoSession.getPointDao();
            isInitialized = true;
        }
    }

    public void saveTracksList(List<Track> tracks) {
        trackDao.deleteAll();
        trackDao.insertInTx(tracks);
    }

    public void saveTrack(Track track) {
        trackDao.insertOrReplace(track);
        List<Point> points = track.getPoints();
        for (Point point : points) {
            point.setTrackId(track.getTrackId());
            daoSession.insertOrReplace(point);
        }
    }

    public List<Track> getTracksList() {
        return trackDao.queryBuilder().list();
    }

    public static CacheManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CacheManager();
        return INSTANCE;
    }

    public Track getTrack(long trackId) {
        return trackDao.load(trackId);
    }
}

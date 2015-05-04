package com.xtracker.android.objects;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.xtracker.android.callbacks.OnTracking;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Ilya on 04.05.2015.
 */

public class LocationClient implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private LocationManager mLocationManager;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private String mLastUpdateTime;


    /**
     * FOR SAVING STATE BOOLEAN KEYS*
     */

    private boolean mRequestingLocationUpdates;
    private boolean tracking = false;
    private boolean paused = false;
    private Track currentTrack;
    public static final String REQUESTING_LOCATION_UPDATES_KEY = "RLUK";
    public static final String LOCATION_KEY = "LK";
    public static final String TRACKING_KEY = "TK";
    public static final String PAUSED_KEY = "PK";
    public static final String CURRENT_TRACK_KEY = "CTK";

    /****/


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public LocationClient() {
        createLocationRequest();
    }

    public LocationClient(Context context) {
        this.mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        createLocationRequest();
        buildGoogleApiClient(context);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        if (location != null) {
//            mCurrentLocation = location;
//            Point point = new Point();
//            point.setLat(mCurrentLocation.getLatitude());
//            point.setLon(mCurrentLocation.getLongitude());
//            point.setSpeed(mCurrentLocation.getSpeed());
//            point.setHeight(mCurrentLocation.getAltitude());
//            System.out.println("********************************POINT ADDED****************************** " + mCurrentLocation.hasAltitude());
//            currentTrack.addPoint(point);
//            System.out.println(String.valueOf(mCurrentLocation.getLatitude()) + " | " + String.valueOf(mCurrentLocation.getLongitude()));
//        }
        if (location != null) {
            if (isBetterLocation(location, mCurrentLocation)) {
                mCurrentLocation = location;
                System.out.println("Location changed");
                Point point = new Point();
                point.setLat(mCurrentLocation.getLatitude());
                point.setLon(mCurrentLocation.getLongitude());
                point.setSpeed(mCurrentLocation.getSpeed());
                point.setHeight(mCurrentLocation.getAltitude());
                System.out.println("********************************POINT ADDED****************************** " + mCurrentLocation.hasAltitude());
                currentTrack.addPoint(point);
                System.out.println(String.valueOf(mCurrentLocation.getLatitude()) + " | " + String.valueOf(mCurrentLocation.getLongitude()));
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            } else {
                System.out.println("bad");}
        }

    }

    private static final int HALF_MINUTES = 1;


    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > HALF_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -HALF_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 50;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    public void saveState(Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        outState.putParcelable(LOCATION_KEY, mCurrentLocation);
        outState.putBoolean(TRACKING_KEY, tracking);
        outState.putBoolean(PAUSED_KEY, paused);
        outState.putSerializable(CURRENT_TRACK_KEY, currentTrack);
    }

    public void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and
            // make sure that the Start Updates and Stop Updates buttons are
            // correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }

            // Update the value of mCurrentLocation from the Bundle and update the
            // UI to show the correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that
                // mCurrentLocationis not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
            paused = savedInstanceState.getBoolean(PAUSED_KEY);
            tracking = savedInstanceState.getBoolean(TRACKING_KEY);
            currentTrack = (Track) savedInstanceState.getSerializable(CURRENT_TRACK_KEY);
        }
    }

    public void startStopTracking() {
        if (!tracking) {
            currentTrack = new Track();
            mRequestingLocationUpdates = true;
            startLocationUpdates();
            if (!(mGoogleApiClient.isConnected())) {
                mGoogleApiClient.connect();
            }
        } else {
            mRequestingLocationUpdates = false;
            stopLocationUpdates();
        }
        tracking = !tracking;
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean pauseResumeTracking() {
        if (tracking) {
            if (paused) {
                mRequestingLocationUpdates = true;
                startLocationUpdates();
            } else {
                mRequestingLocationUpdates = false;
                stopLocationUpdates();
            }
            paused = !paused;
        }
        return paused;
    }

    public void destroy() {
        mGoogleApiClient.disconnect();
    }
}

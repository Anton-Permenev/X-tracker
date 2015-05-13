package com.xtracker.android.objects;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.xtracker.android.callbacks.OnTracking;


/**
 * Created by Ilya on 04.05.2015.
 */

public class LocationHelper extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = LocationHelper.class.getSimpleName();

    /**
     * For location
     */

    private final LocationManager mLocationManager;
    private Location mCurrentLocation = null;
    private LocationListener _listener;
    private TextView checkView;

    /****/


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;
    private OnTracking onTracking;


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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /****/

    protected synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            if (tracking && !paused)
                startLocationUpdates();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public LocationHelper(Context context, TextView textView1) {

        checkView = textView1;
        mLocationRequest = new LocationRequest().setInterval(0).setFastestInterval(0);

        this.mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        _listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("Loc is changed");

                if (location != null) {
            if (isBetterLocation(location, mCurrentLocation)) {
                    checkView.setText("+ " + String.valueOf(location.getLatitude()));
                    mCurrentLocation = location;
                    Point point = new Point();
                    point.setLat(mCurrentLocation.getLatitude());
                    point.setLon(mCurrentLocation.getLongitude());
                    point.setSpeed(mCurrentLocation.getSpeed());
                    point.setHeight(mCurrentLocation.getAltitude());
                    System.out.println("********************************POINT ADDED****************************** " + mCurrentLocation.hasAltitude());
                    currentTrack.addPoint(point);
                    System.out.println(String.valueOf(mCurrentLocation.getLatitude()) + " | " + String.valueOf(mCurrentLocation.getLongitude()) + " | " + String.valueOf(mCurrentLocation.getAltitude()));
            }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                System.out.println("Status is changed");
            }

            @Override
            public void onProviderEnabled(String provider) {
                System.out.println("GPS enabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                System.out.println("GPS disabled");
            }
        };
        buildGoogleApiClient(context);

        mGoogleApiClient.connect();
    }

    private void stopLocationUpdates() {
        mLocationManager.removeUpdates(_listener);
    }

    private void startLocationUpdates() {
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, _listener);
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
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation != null) {
                startLocationUpdates();
                if (!(mGoogleApiClient.isConnected())) {
                    mGoogleApiClient.connect();
                }
            } else {
                if (!(mGoogleApiClient.isConnected())) {
                    mGoogleApiClient.connect();
                }
            }
        } else {
            mRequestingLocationUpdates = false;
            stopLocationUpdates();
            for (long i = 0; i < currentTrack.getPoints().size(); i++) {
                currentTrack.getPoints().get((int)i).setOrdinal(i);
            }
            onTracking.trackPrepared(currentTrack);
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

    public void setTrackCallback(OnTracking onTracking) {
        this.onTracking = onTracking;
    }

    public void destroy() {
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Changed");
        checkView.setText("Get it!");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        if (tracking && !paused)
            startLocationUpdates();
    }
}

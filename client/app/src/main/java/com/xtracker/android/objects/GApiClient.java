package com.xtracker.android.objects;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.xtracker.android.R;
import com.xtracker.android.fragments.HomeFragment;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ilya on 15.03.2015.
 */
public class GApiClient implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private boolean mRequestingLocationUpdates;
    private boolean tracking  = false;
    private ApiService apiService = RestClient.getInstance().getApiService();
    private boolean paused = false;

    public static final String REQUESTING_LOCATION_UPDATES_KEY = "RLUK";
    public static final String LOCATION_KEY = "LK";
    public static final String TRACKING_KEY = "TK";
    public static final String PAUSED_KEY = "PK";
    private static final String CURRENT_TRACK_KEY = "CTK";


    public Location getmCurrentLocation() {
        return mCurrentLocation;
    }

    private Location mCurrentLocation;
    String TOKEN_STRING;
    private Track currentTrack;
    private View rootView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TextView textView1;

    public GApiClient(Activity context, TextView textView1) {
        this.textView1 = textView1;
        //Create an instance of the Google Play services API client
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        //Create a LocationRequest
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (tracking && !paused)
            startLocationUpdates();

        this.mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (this.mCurrentLocation != null) {
//                track.add(mCurrentLocation);
        }
        //this.currentTrack = restService.getTrack(Long.valueOf(null), TOKEN_STRING);
        //!!!! For testing

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (isBetterLocation(location, mCurrentLocation)) {
                mCurrentLocation = location;
                Point point = new Point();
                point.setLat(mCurrentLocation.getLatitude());
                point.setLon(mCurrentLocation.getLongitude());
                point.setSpeed(mCurrentLocation.getSpeed());
                currentTrack.addPoint(point);
                System.out.println(String.valueOf(mCurrentLocation.getLatitude()) + " | " + String.valueOf(mCurrentLocation.getLongitude()));
                if (textView1 != null)
                    textView1.setText(String.valueOf(mCurrentLocation.getLatitude()) + " | " + String.valueOf(mCurrentLocation.getLongitude()));
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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

    private static final int HALF_MINUTES = 1000 * 30;


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
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

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

            apiService.addTrack(currentTrack, new Callback<Long>() {

                @Override
                public void success(Long trackId, Response response) {
                    System.out.println("Success");
                }

                @Override
                public void failure(RetrofitError error) {
                    System.out.println(currentTrack.getPoints().toString());
                }
            });
        }
        tracking = !tracking;
    }

    public void pauseResumeTracking() {
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
    }

    public void destroy() {
        mGoogleApiClient.disconnect();
    }
}

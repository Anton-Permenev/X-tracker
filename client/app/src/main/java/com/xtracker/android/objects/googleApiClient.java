package com.xtracker.android.objects;

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
import com.xtracker.android.fragments.ScreenOne;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.RestClient;

/**
 * Created by Ilya on 15.03.2015.
 */
public class googleApiClient implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String REQUESTING_LOCATION_UPDATES_KEY = "RLUK";
    private static final String LOCATION_KEY = "LK";
    private static final String BTN_PRESSED_KEY = "BPK";
    private final ApiService restService;
    private boolean mRequestingLocationUpdates;
    private Integer btnPressed = 0;
    private Location mCurrentLocation;
    String TOKEN_STRING;
    private Track currentTrack;
    private View rootView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TextView textView1;

    public googleApiClient(ScreenOne fragment, View rootView) {

        this.rootView = rootView;
        this.currentTrack = new Track(Long.valueOf(1));

        //Establish restService
        restService = new RestClient().getApiService();

        //Create an instance of the Google Play services API client
        mGoogleApiClient = new GoogleApiClient.Builder(fragment.getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
        mRequestingLocationUpdates = false;

        this.textView1 = (TextView) rootView.findViewById(R.id.textView1);

        //Create a LocationRequest
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.startCaptureBtn) {
            btnPressed = (btnPressed + 1) % 2;
            if (btnPressed == 1) {
                mRequestingLocationUpdates = true;
                startLocationUpdates();
                v.setActivated(true);
                if (!(mGoogleApiClient.isConnected())) {
                    mGoogleApiClient.connect();
                }
            } else {
                mRequestingLocationUpdates = false;
                stopLocationUpdates();
                v.setActivated(false);

            }

//            When we will send DATA to server, setFasterInterval should be slower!!!

        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (btnPressed > 0) {
            this.mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (this.mCurrentLocation != null) {
//                track.add(mCurrentLocation);
            }
            //this.currentTrack = restService.getTrack(Long.valueOf(null), TOKEN_STRING);
            //!!!! For testing
        }
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
                point.setTrack(currentTrack);
                currentTrack.addPoint(point);
                //restService.updateTrack(currentTrack.getTrackId(), TOKEN_STRING);
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

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putInt(BTN_PRESSED_KEY, btnPressed);
//        super.onSaveInstanceState(savedInstanceState);
    }

    public void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and
            // make sure that the Start Updates and Stop Updates buttons are
            // correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                updateButtons(savedInstanceState);
            }

            // Update the value of mCurrentLocation from the Bundle and update the
            // UI to show the correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that
                // mCurrentLocationis not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
        }
    }

    private void updateButtons(Bundle savedInstanceState) {
        btnPressed = savedInstanceState.getInt(BTN_PRESSED_KEY);
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

}

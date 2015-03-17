package com.xtracker.android.objects;

import android.app.Activity;
import android.app.Fragment;
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
import com.xtracker.android.rest.ApiServiceImpl;

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
    private boolean mRequestingLocationUpdates;
    private Integer btnPressed = 0;
    private Location mCurrentLocation;
    String TOKEN_STRING;
    ApiServiceImpl restService;
    private Track currentTrack;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public googleApiClient(ScreenOne fragment) {
        //Establish restService
        restService = new ApiServiceImpl();

        //Create an instance of the Google Play services API client
        mGoogleApiClient = new GoogleApiClient.Builder(fragment.getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Create a LocationRequest
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void onClick(View v, View rootView) {
        if (v.getId() == R.id.startCaptureBtn) {
            btnPressed = (btnPressed + 1) % 2;
            mRequestingLocationUpdates = true;
            mGoogleApiClient.connect();
            TextView textView1 = (TextView) rootView.findViewById(R.id.textView1);

//            When we will send DATA to server, setFasterInterval should be slower!!!


        }

//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (btnPressed > 0) {
            this.mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (this.mCurrentLocation != null) {
//                track.add(mCurrentLocation);
            }
            this.currentTrack = restService.getTrack(Long.valueOf(null), TOKEN_STRING);
            if (mRequestingLocationUpdates) {
                startLocationUpdates();
            } else {
                stopLocationUpdates();
            }
        } else {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        Point point = new Point();
        point.setLat(mCurrentLocation.getLatitude());
        point.setLon(mCurrentLocation.getLongitude());
        point.setSpeed(mCurrentLocation.getSpeed());
        point.setTrack(currentTrack);
        currentTrack.addPoint(point);
        restService.updateTrack(currentTrack.getTrackId(), TOKEN_STRING);
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

    public void onSaveInstanceState(Bundle savedInstanceState){
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

}

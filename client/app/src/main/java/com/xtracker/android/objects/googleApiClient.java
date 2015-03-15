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
public class googleApiClient
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private boolean mRequestingLocationUpdates;
    private boolean btnPressed = false;
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

    public void basics() {

    }

    public void onClick(View v,View rootView) {
        if (v.getId() == R.id.startCaptureBtn) {
            btnPressed = true;
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
        if (btnPressed) {
            this.mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (this.mCurrentLocation != null) {
//                track.add(mCurrentLocation);
            }
            this.currentTrack = restService.getTrack(Long.valueOf(null), TOKEN_STRING);
            if (mRequestingLocationUpdates) {
                startLocationUpdates();
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
}

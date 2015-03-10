package com.xtracker.android.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.xtracker.android.R;
import com.xtracker.android.objects.Point;
import com.xtracker.android.objects.Track;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.ApiServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


public class ScreenOne extends Fragment implements
        View.OnClickListener,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    String TOKEN_STRING;
    ApiServiceImpl restService;
    private Track currentTrack;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private View rootView = null;
    ImageButton button = null;
    private boolean mRequestingLocationUpdates;
    private boolean btnPressed = false;

    public ScreenOne() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.screen_one, container,
                false);


        //Establish restService
        restService = new ApiServiceImpl();

        //Set ImageButton clickable
        button = (ImageButton) rootView.findViewById(R.id.startCaptureBtn);
        button.setOnClickListener(this);

        //Create an instance of the Google Play services API client
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Create a LocationRequest
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startCaptureBtn) {
            btnPressed = true;
            mRequestingLocationUpdates = true;
            mGoogleApiClient.connect();
            TextView textView1 = (TextView) rootView.findViewById(R.id.textView1);

//            When we will send DATA to server, setFasterInterval should be slower!!!


        }
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


        String mLatitude = null, mLongitude = null;
//        currentTrack = ;
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void onSavedInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        Point point = new Point();
        point.setLat(mCurrentLocation.getLatitude());
        point.setLon(mCurrentLocation.getLongitude());
        point.setSpeed(mCurrentLocation.getSpeed());
        point.setTrack(currentTrack);
        restService.updateTrack(currentTrack.getTrackId(), TOKEN_STRING);
    }

    private void updateUI() {

    }

}
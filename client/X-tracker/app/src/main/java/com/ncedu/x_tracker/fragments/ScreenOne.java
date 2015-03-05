package com.ncedu.x_tracker.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ncedu.x_tracker.MainActivity;
import com.ncedu.x_tracker.R;


public class ScreenOne extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient mGoogleApiClient;

    public ScreenOne() {
    }

    View rootView = null;
    ImageButton button = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.screen_one, container,
                false);

        button = (ImageButton) rootView.findViewById(R.id.startCaptureBtn);
        button.setOnClickListener(this);

        //Create an instance of the Google Play services API client
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startCaptureBtn) {
            mGoogleApiClient.connect();
            TextView textView1 = (TextView) rootView.findViewById(R.id.textView1);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        String mLatitude = null, mLongitude = null;
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            mLatitude = String.valueOf(mLastLocation.getLatitude());
            mLongitude = String.valueOf(mLastLocation.getLongitude());
            TextView textView1 = (TextView) rootView.findViewById(R.id.textView1);
            textView1.setText(mLatitude + " | " + mLongitude );
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
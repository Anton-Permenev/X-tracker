package com.xtracker.android.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.xtracker.android.R;
import com.xtracker.android.objects.googleApiClient;

public class ScreenOne extends Fragment implements
        View.OnClickListener {


    private View rootView = null;
    ImageButton button = null;
    googleApiClient mGoogleClient;

    public ScreenOne() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.screen_one, container,
                false);

        //Establish googleApiClient
        mGoogleClient = new googleApiClient(this);
        mGoogleClient.updateValuesFromBundle(savedInstanceState);

        //Set ImageButton clickable
        button = (ImageButton) rootView.findViewById(R.id.startCaptureBtn);
        button.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        mGoogleClient.onClick(v,rootView);
    }



}
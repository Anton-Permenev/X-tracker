package com.xtracker.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.xtracker.android.R;
import com.xtracker.android.objects.GApiClient;

public class ScreenOne extends Fragment implements
        View.OnClickListener {


    private View rootView = null;
    ImageButton button = null;
    GApiClient mGoogleClient;

    public ScreenOne() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.screen_one, container,
                false);

        //Establish googleApiClient
        mGoogleClient = new GApiClient(this, rootView);
        mGoogleClient.updateValuesFromBundle(savedInstanceState);

        //Set ImageButton clickable
        button = (ImageButton) rootView.findViewById(R.id.startCaptureBtn);
        button.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        mGoogleClient.onClick(v);
    }



}
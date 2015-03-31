package com.xtracker.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xtracker.android.R;
import com.xtracker.android.objects.GApiClient;

public class HomeFragment extends Fragment implements
        View.OnClickListener {

    private View rootView = null;

    GApiClient mGoogleClient;
    private ImageButton pauseButton;
    private ImageButton trackButton;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_fragment, container,
                false);
        TextView textView1 = (TextView) rootView.findViewById(R.id.textView1);
        //Establish googleApiClient
        mGoogleClient = new GApiClient(this.getActivity(), textView1);
        mGoogleClient.updateValuesFromBundle(savedInstanceState);

        pauseButton = (ImageButton) rootView.findViewById(R.id.pauseButton);
        pauseButton.setVisibility(View.GONE);

        trackButton = (ImageButton) rootView.findViewById(R.id.startCaptureBtn);

        trackButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);

        if (savedInstanceState != null)
            updateButtonsState(savedInstanceState);

        return rootView;
    }

    private void updateButtonsState(Bundle savedInstanceState) {
        boolean tracking = savedInstanceState.getBoolean(GApiClient.TRACKING_KEY);
        boolean paused = savedInstanceState.getBoolean(GApiClient.PAUSED_KEY);
        pauseButton.setVisibility(tracking ? View.VISIBLE : View.GONE);
        pauseButton.setActivated(paused);
        trackButton.setActivated(tracking);
    }

    @Override
    public void onDestroy() {
        mGoogleClient.destroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mGoogleClient.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startCaptureBtn:
                mGoogleClient.startStopTracking();
                v.setActivated(!v.isActivated());
                pauseButton.setVisibility(v.isActivated() ? View.VISIBLE : View.GONE);
                break;
            case R.id.pauseButton:
                mGoogleClient.pauseResumeTracking();
                v.setActivated(!v.isActivated());
                break;
        }
    }
}
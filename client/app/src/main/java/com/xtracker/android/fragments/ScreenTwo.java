package com.xtracker.android.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xtracker.android.R;
import com.xtracker.android.activities.TrackActivity;
import com.xtracker.android.objects.Point;
import com.xtracker.android.objects.Track;
import com.xtracker.android.objects.googleApiClient;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.RestClient;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ScreenTwo extends Fragment implements View.OnClickListener {

    private View rootView;
    private Button button;
    private TextView helloOutput;
    private RestClient restClient;
    private ApiService apiService;


    public ScreenTwo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.screen_two, container,
                false);
        button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(this);

        helloOutput = (TextView) rootView.findViewById(R.id.textView);

        restClient = RestClient.getInstance();
        apiService = restClient.getApiService();

        return rootView;
    }

    public void OnClick(View v) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                //startTrackActivity();
                getHello();
                //restRequestExample();
                break;
        }
    }

    private void startTrackActivity() {
        Intent intent = new Intent(this.getActivity() , TrackActivity.class);
        startActivity(intent);
    }

    private void restRequestExample() {
        //REST request example
        Track track = new Track();
        Point point = new Point();
        point.setSpeed(0.3f);
        ArrayList<Point> points = new ArrayList<>();
        points.add(point);
        track.setPoints(points);

        ApiService apiService = RestClient.getInstance().getApiService();
        apiService.addTrack(track, new Callback<Long>() {

            @Override
            public void success(Long trackId, Response response) {
                if (trackId != null)
                    helloOutput.setText(trackId.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                helloOutput.setText(error.toString()
                );
            }
        });
    }

    private void getHello() {

        helloOutput.setText("vse ok");
        apiService.hello(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                helloOutput.setText(s);
            }

            @Override
            public void failure(RetrofitError error) {
                helloOutput.setText(error.getMessage());
            }
        });
    }
}
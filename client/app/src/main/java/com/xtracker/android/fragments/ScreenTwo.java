package com.xtracker.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xtracker.android.R;
import com.xtracker.android.activities.TrackActivity;
import com.xtracker.android.objects.GApiClient;
import com.xtracker.android.objects.Point;
import com.xtracker.android.objects.Track;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ScreenTwo extends Fragment implements View.OnClickListener {

    private View rootView;
    private Button button;
    private TextView helloOutput;
    private ApiService apiService;
    private List<Track> mTracks;

    public void setTracks(List<Track> tracks) {
        this.mTracks = tracks;
    }

    private ArrayList<Long> tracksArray;


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

        apiService = RestClient.getInstance().getApiService();

        //Initialize tracksArray
        tracksArray = new ArrayList<Long>();

        getTracks();

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        //Create an adapter for ListView

        ArrayAdapter tracksAdapter = new ArrayAdapter<Long>(this.getActivity(),
                android.R.layout.simple_list_item_1, tracksArray);
        //bing adapter to ListView
        listView.setAdapter(tracksAdapter);
        listView.setOnItemClickListener(mMessageClickedHandler);

        return rootView;
    }

    private void getTracks() {
        apiService.getTracksList(new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                setTracks(tracks);
                if (tracks != null) {
                    helloOutput.setText(String.valueOf(tracks.size()));
                } else {
                    helloOutput.setText("Hehe");
                }
                for (Track t : tracks) {
                    updateTracksArray(t.getTrackId());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println(error.toString());
                updateTracksArray(3);
            }
        });
    }

    private void updateTracksArray(long trackId) {
        this.tracksArray.add(trackId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startTrackActivity(0);
//                getHello();
                //restRequestExample();
                break;
        }
    }

    private void startTrackActivity(long trackId) {
        Intent intent = new Intent(this.getActivity(), TrackActivity.class);
        intent.putExtra("TRACK_ID", trackId);
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

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.listView) {
                TextView idText = (TextView) view;
                startTrackActivity(Long.valueOf(String.valueOf(idText.getText())));
            }
        }
    };

}
package com.xtracker.android.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.xtracker.android.R;
import com.xtracker.android.objects.GoogleMapsManager;
import com.xtracker.android.objects.Track;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TrackActivity extends ActionBarActivity {

    private long trackId;
    private ApiService apiService;
    GoogleMapsManager mapsManager;
    Track track;


    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        Intent intent = this.getIntent();
        trackId = intent.getLongExtra("TRACK_ID", 1);
        apiService = RestClient.getInstance().getApiService();
        mapsManager = new GoogleMapsManager(getFragmentManager(), track);

        final TextView editText = (TextView) findViewById(R.id.textView2);
        editText.setText(String.valueOf(trackId));

        apiService.getTrack(trackId, new Callback<Track>() {
            @Override
            public void success(Track track, Response response) {
                setTrack(track);
            }

            @Override
            public void failure(RetrofitError error) {
                editText.setText(error.getMessage());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

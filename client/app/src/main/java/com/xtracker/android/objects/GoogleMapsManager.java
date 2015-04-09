package com.xtracker.android.objects;

import android.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xtracker.android.R;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.RestClient;

public class GoogleMapsManager implements OnMapReadyCallback {

    private final FragmentManager fragmentManager;
    private MapFragment mMap;
    private GApiClient mApiClient;
    private LatLngBounds AREA;
    private ApiService apiService = RestClient.getInstance().getApiService();
    private Track track;

    public GoogleMapsManager(FragmentManager fragmentManager, Track track) {
        this.fragmentManager = fragmentManager;
        this.track = track;

        MapFragment fragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        mMap = fragment;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (track != null) {
            Point point = track.getPoints().get(0);
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(point.getLat(), point.getLon()))
                    .title("Hello world"));
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            LatLngBounds myBounds = new LatLngBounds(new LatLng(point.getLat() - 100, point.getLon() - 100),
                    new LatLng(point.getLat() + 100, point.getLon() + 100));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(myBounds, 0));

            googleMap.setMyLocationEnabled(true);
        }
    }

    public void workMethod() {


    }
}

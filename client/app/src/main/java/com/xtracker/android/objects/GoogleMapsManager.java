package com.xtracker.android.objects;

import android.app.FragmentManager;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.xtracker.android.R;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

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
            drawTrack(track, googleMap);
        }
    }

    private void drawTrack(Track track, GoogleMap googleMap) {
        Point point = track.getPoints().get(0);
        Polyline polylineFast = googleMap.addPolyline(new PolylineOptions()
                .color(Color.RED)
                .geodesic(true));
        Polyline polylineSlow = googleMap.addPolyline(new PolylineOptions()
                .color(Color.GREEN)
                .geodesic(true));
        Polyline polylineMiddle = googleMap.addPolyline(new PolylineOptions()
                .color(Color.MAGENTA)
                .geodesic(true));

        //Fill the polyline list
        List<LatLng> listOfPoints = new ArrayList<>();
        Float minSpeed = track.getPoints().get(0).getSpeed();
        Float maxSpeed = Float.valueOf(0);
        for (Point p : track.getPoints()) {
            if (p.getSpeed() > maxSpeed){
                maxSpeed = p.getSpeed();
            }
            if (p.getSpeed() < minSpeed){
                minSpeed = p.getSpeed();
            }
            listOfPoints.add(new LatLng(p.getLat(), p.getLon()));
        }


        //Fill the polyline
        polyline.setPoints(listOfPoints);
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(point.getLat(), point.getLon()))
//                .title("Hello world"));


        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //Set camera on our point
        LatLngBounds myBounds = new LatLngBounds(new LatLng(point.getLat() - 3, point.getLon() - 3),
                new LatLng(point.getLat() + 3, point.getLon() + 3));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(myBounds, 0));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myBounds.getCenter(), 16));

        googleMap.setMyLocationEnabled(true);
    }

    public void workMethod() {


    }
}

package com.xtracker.android.fragments;

import android.content.Context;
import android.content.DialogInterface;
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
import com.xtracker.android.objects.googleApiClient;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.RestClient;

import java.net.SocketTimeoutException;

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

        restClient = new RestClient();
        apiService = restClient.getApiService();

        return rootView;
    }

    public void OnClick(View v) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                getHello();
                break;
        }
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
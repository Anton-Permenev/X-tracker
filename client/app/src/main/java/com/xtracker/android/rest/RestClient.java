package com.xtracker.android.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {

    public static final String BASE_URL = "http://188.226.194.202:8080/X-trackerWeb/api";
    private static RestClient INSTANCE;
    private Filter client;
    private ApiService apiService;

    public RestClient() {
        Gson gson = new GsonBuilder().create();

        client = new Filter();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(client)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();


        apiService = restAdapter.create(ApiService.class);
    }

    public static RestClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RestClient();
        }
        return INSTANCE;
    }

    public void setKeys(long userId, String privateKey) {
        client.setKeys(userId, privateKey);
    }

    public ApiService getApiService() {
        return apiService;
    }
}
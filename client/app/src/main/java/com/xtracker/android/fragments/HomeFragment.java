package com.xtracker.android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xtracker.android.R;
import com.xtracker.android.Utils;
import com.xtracker.android.callbacks.OnTracking;
import com.xtracker.android.objects.GApiClient;
import com.xtracker.android.objects.LocationClient;
import com.xtracker.android.objects.Track;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment implements
        View.OnClickListener, OnTracking {

    private View rootView = null;
    private ImageButton pauseButton;
    private ImageButton trackButton;
    private ProgressBar progressBar;
    private EditText trackTitle;
    private EditText trackDescription;
    private ActionBar toolbar;
    private ImageView trackImage;
    private View trackingLayout;
    private View addTrackLayout;
    private View idleLayout;
    private View buttons;
    private TextView timeText;

    private ApiService apiService = RestClient.getInstance().getApiService();
    //GApiClient mGoogleClient;
    LocationClient mGoogleClient;

    private Track preparedTrack;
    private State currentState = State.IDLE;

    private long millisPassed = 0;

    private static final String CURRENT_STATE_KEY = "CSK";
    private static final String TRACK_TITLE_KEY = "TTK";
    private static final String TRACK_DESCRIPTION_KEY = "TDK";
    private static final String PREPARED_TRACK_KEY = "PTK";
    private static final String TRACK_IMAGE_KEY = "TIK";
    private static final String MILLIS_PASSED_KEY = "MPK";
    private static final int CHOOSE_PICTURE = 0;

    private final int TIMER_INTERVAL = 100;

    private CountDownTimer countDownTimer = new CountDownTimer(30000, TIMER_INTERVAL) {

        @Override
        public void onTick(long millisUntilFinished) {
            timeText.setText(Utils.getTimeTextFromMillis(millisPassed));
            millisPassed += TIMER_INTERVAL;
        }

        @Override
        public void onFinish() {
            start();
        }
    };

    private enum State {
        IDLE, TRACKING, LOADING, ADD
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_fragment, container,
                false);
        TextView textView1 = (TextView) rootView.findViewById(R.id.textView1);
        //Establish googleApiClient
        //mGoogleClient = new GApiClient(this.getActivity(), textView1);
        mGoogleClient = new LocationClient(this.getActivity());
        mGoogleClient.updateValuesFromBundle(savedInstanceState);
        //mGoogleClient.setTrackCallback(this);

        pauseButton = (ImageButton) rootView.findViewById(R.id.pauseButton);
        pauseButton.setVisibility(View.GONE);
        trackButton = (ImageButton) rootView.findViewById(R.id.startCaptureBtn);
        timeText = (TextView) rootView.findViewById(R.id.currentTime);

        trackingLayout = rootView.findViewById(R.id.trackingLayout);
        addTrackLayout = rootView.findViewById(R.id.addTrackLayout);
        idleLayout = rootView.findViewById(R.id.idleLayout);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        buttons = rootView.findViewById(R.id.buttons);

        trackTitle = (EditText) rootView.findViewById(R.id.trackTitle);
        trackDescription = (EditText) rootView.findViewById(R.id.trackDescription);

        trackButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);

        toolbar = ((ActionBarActivity)getActivity()).getSupportActionBar();

        trackImage = (ImageView) rootView.findViewById(R.id.trackImage);
        trackImage.setOnClickListener(this);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            updateButtonsState(savedInstanceState);
            currentState = (State) savedInstanceState.getSerializable(CURRENT_STATE_KEY);
            preparedTrack = (Track) savedInstanceState.getSerializable(PREPARED_TRACK_KEY);
            if (currentState == State.ADD) {
                trackTitle.setText(savedInstanceState.getString(TRACK_TITLE_KEY));
                trackDescription.setText(savedInstanceState.getString(TRACK_DESCRIPTION_KEY));
                trackImage.setImageBitmap((android.graphics.Bitmap) savedInstanceState.getParcelable(TRACK_IMAGE_KEY));
            } else if (currentState == State.TRACKING) {
                millisPassed = savedInstanceState.getLong(MILLIS_PASSED_KEY);
                timeText.setText(Utils.getTimeTextFromMillis(millisPassed));
                if (!mGoogleClient.isPaused())
                    startTimer();
            }
        }

        setState(currentState);

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
        outState.putSerializable(CURRENT_STATE_KEY, currentState);
        outState.putSerializable(PREPARED_TRACK_KEY, preparedTrack);
        outState.putString(TRACK_TITLE_KEY, trackTitle.getText().toString());
        outState.putString(TRACK_DESCRIPTION_KEY, trackDescription.getText().toString());
        outState.putParcelable(TRACK_IMAGE_KEY, ((BitmapDrawable) trackImage.getDrawable()).getBitmap());
        outState.putLong(MILLIS_PASSED_KEY, millisPassed);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startCaptureBtn:
                if (currentState == State.IDLE) {
                    startTimer();
                    setState(State.TRACKING);
                } else {
                    countDownTimer.cancel();
                }
                mGoogleClient.startStopTracking();
                v.setActivated(!v.isActivated());
                pauseButton.setVisibility(v.isActivated() ? View.VISIBLE : View.GONE);
                pauseButton.setActivated(false);
                break;
            case R.id.pauseButton:
                mGoogleClient.pauseResumeTracking();
                    if (mGoogleClient.isPaused()) {
                        countDownTimer.cancel();
                    } else {
                        startTimer();
                    }

                v.setActivated(!v.isActivated());
                break;
            case R.id.trackImage:
                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent chooserIntent = Intent.createChooser(pickIntent, getResources().getString(R.string.pick_image));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {takePictureIntent});
                startActivityForResult(chooserIntent, CHOOSE_PICTURE);
                break;
        }
    }

    private void startTimer() {
        countDownTimer.cancel();
        countDownTimer.start();
    }

    @Override
    public void trackPrepared(Track track) {
        if (track.getPoints().size() == 0) {
            setState(State.IDLE);
        } else {
            preparedTrack = track;
            preparedTrack.setDuration(millisPassed);
            setState(State.ADD);
        }
        millisPassed = 0;
    }

    @Override
    public void pointAdded() {

    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.add_track, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.addTrack:
                    String title = trackTitle.getText().toString();

                    String description = trackDescription.getText().toString();
                    if (title.trim().length() == 0) {
                        trackTitle.setError(getResources().getString(R.string.setTitleError));
                        return true;
                    }

                    setState(State.LOADING);

                    preparedTrack.setTitle(title);
                    preparedTrack.setDescription(description);
                    apiService.addTrack(preparedTrack, new Callback<Long>() {

                        @Override
                        public void success(Long trackId, Response response) {
                            mode.finish();
                            resetTracking();
                            Toast.makeText(getActivity(), R.string.track_added, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            mode.finish();
                            resetTracking();
                            Toast.makeText(getActivity(), R.string.internal_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Utils.hideKeyboard(getActivity(), trackTitle, trackDescription);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            resetTracking();
        }
    };

    private void resetTracking() {
        preparedTrack = null;
        setState(State.IDLE);
    }

    private void setState(State state) {
        currentState = state;
        addTrackLayout.setVisibility(View.GONE);
        trackingLayout.setVisibility(View.GONE);
        idleLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        buttons.setVisibility(View.GONE);

        View view = null;
        switch (state) {
            case IDLE:
                view = idleLayout;
                trackTitle.setText("");
                trackDescription.setText("");
                trackImage.setImageResource(R.drawable.ic_camera);
                buttons.setVisibility(View.VISIBLE);
                Utils.hideKeyboard(getActivity(), trackTitle, trackDescription);
                break;
            case TRACKING:
                view = trackingLayout;
                buttons.setVisibility(View.VISIBLE);
                break;
            case LOADING:
                view = progressBar;
                break;
            case ADD:
                view = addTrackLayout;
                countDownTimer.cancel();
                toolbar.startActionMode(mActionModeCallback);
                trackTitle.requestFocus();
                Utils.showKeyboard(this.getActivity(), trackTitle);
                break;
        }
        view.setVisibility(View.VISIBLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                trackImage.setImageURI(data.getData());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
package com.xtracker.android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.xtracker.android.callbacks.OnTrackCreated;
import com.xtracker.android.objects.GApiClient;
import com.xtracker.android.objects.Track;
import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.RestClient;

import java.io.File;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment implements
        View.OnClickListener, OnTrackCreated {

    private View rootView = null;

    private ApiService apiService = RestClient.getInstance().getApiService();

    GApiClient mGoogleClient;
    private ImageButton pauseButton;
    private ImageButton trackButton;
    private Track preparedTrack;
    private EditText trackTitle;
    private EditText trackDescription;
    private ActionBar toolbar;
    private View trackingLayout;
    private View addTrackLayout;
    private View idleLayout;

    private State currentState = State.IDLE;
    private ProgressBar progressBar;
    private View buttons;

    private static final String CURRENT_STATE_KEY = "CSK";
    private static final String TRACK_TITLE_KEY = "TTK";
    private static final String TRACK_DESCRIPTION_KEY = "TDK";
    private static final String PREPARED_TRACK_KEY = "PTK";
    private static final String TRACK_IMAGE_KEY = "TIK";
    private static final String PICTURE_FILE_KEY = "PFK";

    private ImageView trackImage;
    private static final int SELECT_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private File mCurrentPhotoFile;

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
        mGoogleClient = new GApiClient(this.getActivity(), textView1);
        mGoogleClient.updateValuesFromBundle(savedInstanceState);
        mGoogleClient.setTrackCallback(this);

        pauseButton = (ImageButton) rootView.findViewById(R.id.pauseButton);
        pauseButton.setVisibility(View.GONE);
        trackButton = (ImageButton) rootView.findViewById(R.id.startCaptureBtn);

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
            mCurrentPhotoFile = (File) savedInstanceState.getSerializable(PICTURE_FILE_KEY);
            if (currentState == State.ADD) {
                trackTitle.setText(savedInstanceState.getString(TRACK_TITLE_KEY));
                trackDescription.setText(savedInstanceState.getString(TRACK_DESCRIPTION_KEY));
                trackImage.setImageBitmap((android.graphics.Bitmap) savedInstanceState.getParcelable(TRACK_IMAGE_KEY));
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
        outState.putSerializable(PICTURE_FILE_KEY, mCurrentPhotoFile);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startCaptureBtn:
                setState(State.TRACKING);
                mGoogleClient.startStopTracking();
                v.setActivated(!v.isActivated());
                pauseButton.setVisibility(v.isActivated() ? View.VISIBLE : View.GONE);
                pauseButton.setActivated(false);
                break;
            case R.id.pauseButton:
                mGoogleClient.pauseResumeTracking();
                v.setActivated(!v.isActivated());
                break;
            case R.id.trackImage:
                chooseImage();
                break;
        }
    }

    private void chooseImage() {

        final AlertDialog.Builder imageDialog = new AlertDialog.Builder(this.getActivity());
        imageDialog.setTitle(R.string.chooseImage);
        final CharSequence[] itemsTitles = {getResources().getString(R.string.takePicture), getResources().getString(R.string.chooseFromGallery)};

        imageDialog.setItems(itemsTitles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                if (which == 1) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.chooseFromGallery)), SELECT_PICTURE);
                } else if (which == 0) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        mCurrentPhotoFile = null;
                        try {
                            mCurrentPhotoFile = Utils.createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            ex.printStackTrace();
                            Toast.makeText(getActivity(), R.string.internal_error, Toast.LENGTH_SHORT).show();
                        }
                        // Continue only if the File was successfully created
                        if (mCurrentPhotoFile != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(mCurrentPhotoFile));
                            startActivityForResult(intent, TAKE_PICTURE);
                            System.out.println(mCurrentPhotoFile.getAbsolutePath());
                        }
                    }
                }
                dialog.dismiss();
            }
        });
        imageDialog.show();
    }

    @Override
    public void trackPrepared(Track track) {
        if (track.getPoints().size() == 0)
            return;

        preparedTrack = track;
        setState(State.ADD);
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
                    if (mCurrentPhotoFile != null)
                        System.out.println("image to pass " + mCurrentPhotoFile.getAbsolutePath());
                    apiService.addTrack(preparedTrack, new Callback<Long>() {

                        @Override
                        public void success(Long trackId, Response response) {
                            System.out.println("Success");
                            mode.finish();
                            resetTracking();
                            Toast.makeText(getActivity(), R.string.track_added, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = Utils.fetchImage(mCurrentPhotoFile);
                if (bitmap != null)
                    trackImage.setImageBitmap(bitmap);
            }
        } else if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                trackImage.setImageURI(data.getData());
                mCurrentPhotoFile = new File(data.getData().getPath());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                toolbar.startActionMode(mActionModeCallback);
                trackTitle.requestFocus();
                Utils.showKeyboard(this.getActivity(), trackTitle);
                break;
        }
        view.setVisibility(View.VISIBLE);
    }


}
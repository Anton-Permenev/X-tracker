package com.xtracker.android.activities;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.xtracker.android.rest.ApiService;
import com.xtracker.android.rest.ApiService.Keys;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.xtracker.android.R;
import com.xtracker.android.rest.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends ActionBarActivity {

    private static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    private static final int REQUEST_CODE_RECOVERABLE_ERROR = 1001;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.email" ;
    private String email;
    private SharedPreferences sharedPrefs;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiService = RestClient.getInstance().getApiService();

        if (isSignedIn())
            finishLogin();
        else
            openAccountPicker();
    }

    private void finishLogin() {
        RestClient.getInstance().setKeys(
                sharedPrefs.getLong("userId", 0),
                sharedPrefs.getString("privateKey", ""));
        Log.d("dbg", "private key: " + sharedPrefs.getString("privateKey", ""));

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean isSignedIn() {
        sharedPrefs = getSharedPreferences("credentials", MODE_PRIVATE);
        return !sharedPrefs.getString("email", "").equals("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void openAccountPicker() {
        String[] accountTypes = new String[] {"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                getToken();
            } else {
                Toast.makeText(this, "pick an account!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_RECOVERABLE_ERROR && resultCode == RESULT_OK) {
            getToken();
        }
    }

    private void getToken() {
        //if (isDeviceOnline)
        (new GetTokenTask(this)).execute(email);
    }

    private void handleException(final Exception e) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            LoginActivity.this,
                            REQUEST_CODE_RECOVERABLE_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVERABLE_ERROR);
                } else {
                    Toast.makeText(getApplicationContext(), "A fatal error has occured.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private class GetTokenTask extends AsyncTask<String, Void, String> {


        private LoginActivity context;

        private GetTokenTask(LoginActivity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return GoogleAuthUtil.getToken(context, params[0], SCOPE);
            } catch (Exception e) {
                context.handleException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                apiService.login(email, s, new Callback<Keys>() {
                    @Override
                    public void success(Keys keys, Response response) {
                        saveCredentials(keys);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void saveCredentials(Keys keys) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("email", email);
        editor.putLong("userId", keys.getPublicKey());
        editor.putString("privateKey", keys.getPrivateKey());

        if (editor.commit()) {
            finishLogin();
        }
    }


}
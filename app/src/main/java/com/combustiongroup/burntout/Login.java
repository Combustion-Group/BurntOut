package com.combustiongroup.burntout;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.combustiongroup.burntout.network.STApi;
import com.combustiongroup.burntout.network.STResponse;
import com.combustiongroup.burntout.network.dto.request.LoginRequest;
import com.combustiongroup.burntout.network.dto.response.UserResponse;
import com.combustiongroup.burntout.util.StringUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Intent main;

    LocationManager lm;
    LocationListener ll_gps, ll_net;
    Location userLocation = null;

    GoogleApiClient gclient;

    final int REQUEST_CHECK_SETTINGS = 0;
    final int REQUEST_ACCESS_LOCATION = 1;

    CallbackManager callbackManager;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.loginbutton)
    TextView mLoginButton;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        final ToggleButton toggleLanguage = (ToggleButton) findViewById(R.id.toggleLanguages);

        assert toggleLanguage != null;
        toggleLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Locale current = getResources().getConfiguration().locale;

                if (current.getLanguage().equals("en")) {
                    String lang = "es";
                    Locale myLocale = new Locale(lang);
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                if (current.getLanguage().equals("es")) {
                    String lang = "en";
                    Locale myLocale = new Locale(lang);
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }

            }
        });

        LoginManager.getInstance().logOut();

        View forgot = findViewById(R.id.forgotpass);
        assert forgot != null;
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login.this, ForgotPassword.class);
                startActivity(i);
            }
        });

        View signup = findViewById(R.id.signup);
        assert signup != null;
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login.this, Signup.class);
                startActivity(i);
            }
        });


        callbackManager = CallbackManager.Factory.create();
        final LoginButton fbLogin = (LoginButton) findViewById(R.id.facebook_login);
        assert fbLogin != null;
        fbLogin.setReadPermissions("email,public_profile");
        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.w("#app", "FB success");

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.w("#app", response.getRawResponse());
                                String email = "error";
                                String id = "";
                                String fname = "";
                                String lname = "";
                                try {
                                    email = object.getString("email");
                                    id = object.getString("id");
                                    fname = object.getString("first_name");
                                    lname = object.getString("last_name");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    Log.w("#app", "user email: " + email);
                                    if (!email.equals("error")) {
                                        fbLogin(email, id, fname, lname);
                                    }
                                }
                            }
                        }
                );
                Bundle params = new Bundle();
                params.putString("fields", "email,first_name,last_name");
                request.setParameters(params);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

                Log.w("#app", "FB error");
                Toast.makeText(Login.this, getString(R.string.error_login_facebook), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                } else {
                }
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }//on create

    // In this function we validating the login fields: Email and Password.
    public void loginClicked(View view) {
        // get values
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        //keyboard slightly obscures toast, gray on gray
        Functions.closeSoftKeyboard(Login.this);

        // determine validity
        boolean emailValid = StringUtils.isValidEmailAddress(email);
        boolean passwordValid = password.length() >= 6;

        if (!emailValid) {
            Toast.makeText(Login.this, getString(R.string.error_empty_email), Toast.LENGTH_LONG).show();
        } else if (!passwordValid) {
            Toast.makeText(Login.this, getString(R.string.error_password_invalid), Toast.LENGTH_LONG).show();
        }

        if (emailValid && passwordValid) {
            login(email, password);
        }

    }//loginClicked

    private void login(String email, String password) {

        STApi.service.login(new LoginRequest(email, password)).enqueue(new Callback<STResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<STResponse<UserResponse>> call, retrofit2.Response<STResponse<UserResponse>> response) {
//                STApi.userResponse = response.body().getData();
//                STApi.status = STApi.userResponse.getStatus();

//                if (STApi.status.equals("one")) {
//                    main = new Intent(Login.this, Main.class);
//                    main.putExtra("fname", STApi.userResponse.getFname());
//                    main.putExtra("lname", STApi.userResponse.getLname());
//                    main.putExtra("picture", STApi.userResponse.getPicture());
//                    main.putExtra("email", STApi.userResponse.getEmail());
//                    startActivity(main);
//
//                } else if (STApi.status.equals("two")) {
//                    Toast.makeText(Login.this, getString(R.string.error_email_password_invalid), Toast.LENGTH_LONG).show();
//                }

                Log.e("LoginActivity", "Logged in!");
                Log.e("LoginActivity", response.body().getData().toString());
            }

            @Override
            public void onFailure(Call<STResponse<UserResponse>> call, Throwable t) {
                Log.e("LoginActivity", "Failed to login.");
                t.printStackTrace();
            }
        });


    }//login

    void fbLogin(final String email, final String id, final String fname, final String lname) {
        //call to loginFB.php
        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.FBLogin.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.w("#app", response);
//                        if (r.status.equals("one")) {
//                            main = new Intent(Login.this, Main.class);
//                            main.putExtra("fname", r.fname);
//                            main.putExtra("lname", r.lname);
//                            main.putExtra("picture", r.picture);
//                            main.putExtra("email", r.email);
//
//                            Net.singleton.startHeavyTask(Login.this);
//                            doSettings();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("fbid", id);

                return params;
            }
        };

        Net.singleton.addRequest(Login.this, req);
    }//fbLogin

    //user authenticated, geolocation given or denied
    void enterApp() {
        Net.singleton.finishHeavyTask();
        Log.w("#app", "still live requests: " + Net.singleton.liveRequests);
        startActivity(main);
        //can't back out to this anyway
        finish();
    }//enter app

    //GEOLOCATION STUFF

    Location getLastKnownLocation() throws SecurityException {
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    void doSettings() {
        gclient = new GoogleApiClient.Builder(Login.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        gclient.connect();
    }//do settings

    void doLocationSettings() {
        LocationRequest accurate = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(300);

        LocationSettingsRequest.Builder b = new LocationSettingsRequest.Builder()
                .addLocationRequest(accurate);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(gclient, b.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.w("#app", "location services enabled");
                        getGeoLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.w("#app", "request location service");
                            Net.singleton.finishHeavyTask();
                            status.startResolutionForResult(Login.this, REQUEST_CHECK_SETTINGS);
                        } catch (Exception e) {
                            Log.w("#app", "error making request");
                            stopGeoListeners();
                        }
                        break;
                    default:
                        Log.w("#app", "request for location service denied");
                        stopGeoListeners();
                        break;
                }
            }
        });
    }//do location settings

    void getGeoLocation() {
        lm = (LocationManager) Login.this.getSystemService(Context.LOCATION_SERVICE);
        ll_gps = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.w("#app", "location given by gps");
                userLocation = location;
                stopGeoListeners();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        ll_net = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.w("#app", "location given by network");
                userLocation = location;
                stopGeoListeners();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (Net.singleton.liveRequests == 0) {
                Net.singleton.startHeavyTask(Login.this);
            }

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll_gps);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll_net);

            new CountDownTimer(10000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    if (userLocation == null) {
                        userLocation = getLastKnownLocation();
                        stopGeoListeners();
                    }
                }
            }.start();
        } else {
            ActivityCompat.requestPermissions(
                    Login.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_LOCATION
            );
        }
    }//get geo location

    void stopGeoListeners() throws SecurityException {
        try {
            lm.removeUpdates(ll_gps);
            lm.removeUpdates(ll_net);
        } catch (Exception e) {

        } finally {
            if (userLocation == null) {
                userLocation = new Location("");
                userLocation.setLatitude(0.0d);
                userLocation.setLongitude(0.0d);
            }

            //some result has been given for location, user previously authenticated
            main.putExtra("latitude", String.valueOf(userLocation.getLatitude()));
            main.putExtra("longitude", String.valueOf(userLocation.getLongitude()));

            Log.w("#app", "using location " + String.valueOf(userLocation));
            enterApp();
        }
    }//stop geo listeners

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.w("#app", "connected to play services");
        doLocationSettings();
    }//on connected

    @Override
    public void onConnectionSuspended(int i) {

    }//on connection suspended

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.w("#app", "unable to connect to google services");
        stopGeoListeners();
    }//on connection failed

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                getGeoLocation();
            } else {
                stopGeoListeners();
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }//on activity result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_ACCESS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGeoLocation();
            } else {
                stopGeoListeners();
            }
        }
    }//on request permissions result

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("app", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver();

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

}//Login

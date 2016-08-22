package com.combustiongroup.burntout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.combustiongroup.burntout.GCM.RegistrationIntentService;
import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.response.LoginResponse;
import com.combustiongroup.burntout.util.Functions;
import com.combustiongroup.burntout.util.SpinnerAlert;
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

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import static com.combustiongroup.burntout.network.BOAPI.gLoginResponse;


public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static Intent main;

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

    private SharedPreferences sp;

    private static final String TAG = "LoginActivity";
    public static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;

        sp = PreferenceManager.getDefaultSharedPreferences(this);

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
                i.putExtra("email", mEmail.getText().toString());
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

                Log.w(TAG, "FB success");

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.w(TAG, response.getRawResponse());
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
                                    Log.w(TAG, "user email: " + email);
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

                Log.e(TAG, "FB error");
                Toast.makeText(Login.this, getString(R.string.error_login_facebook), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });


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

    public static void login(final String email, final String password) {
        SpinnerAlert.show(mContext);
        BOAPI.service.login(email, password, "Android", RegistrationIntentService.token).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                // This is the LoginResponse object that we get from the API
                gLoginResponse = response.body();

                // Check if login was successful, if it was then we get the value "one" on the status field
                if (gLoginResponse.getStatus().equals("one")) {
                    main = new Intent(mContext, Main.class);
                    main.putExtra("fname", gLoginResponse.getFname());
                    main.putExtra("lname", gLoginResponse.getLname());
                    main.putExtra("picture", gLoginResponse.getPicture());
                    main.putExtra("email", gLoginResponse.getEmail());
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                    sp.edit().putString("email",email).putString("password", password).apply();
                    SpinnerAlert.dismiss(mContext);
                    mContext.startActivity(main);

                    // Check if login was unsuccessful, if it was then we get the value "two" on the status field
                } else if (gLoginResponse.getStatus().equals("two")) {
                    // Show a Toast -Alert- that the email and password combination doesn't exits on the API
                    Toast.makeText(mContext, mContext.getString(R.string.error_email_password_invalid), Toast.LENGTH_LONG).show();
                    SpinnerAlert.dismiss(mContext);

                }
                Log.i(TAG, "Logged in!");
                Log.i(TAG, response.body().toString());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Failed to login.");
                Toast.makeText(mContext, mContext.getString(R.string.error_request), Toast.LENGTH_LONG).show();
                SpinnerAlert.dismiss(mContext);

                t.printStackTrace();
            }
        });


    }//login

    void fbLogin(final String email, final String id, final String fname, final String lname) {

        SpinnerAlert.show(Login.this);

        BOAPI.service.facebookLogin(email, fname, lname, id, RegistrationIntentService.token, "Android").enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                // This is the LoginResponse object that we get from the API
                gLoginResponse = response.body();
                gLoginResponse.setPicture("https://graph.facebook.com/" + id + "/picture");
                Log.w(TAG, response.body().toString());
                if (response.body().getStatus().equals("one")) {
                    main = new Intent(Login.this, Main.class);
                    main.putExtra("fname", gLoginResponse.getFname());
                    main.putExtra("lname", gLoginResponse.getLname());
                    main.putExtra("picture", gLoginResponse.getPicture());
                    main.putExtra("email", gLoginResponse.getEmail());

                    doSettings();
                }else{
                    SpinnerAlert.dismiss(Login.this);
                    Toast.makeText(Login.this, getString(R.string.error_request), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                SpinnerAlert.dismiss(Login.this);
                t.printStackTrace();
//                  TODO add couldn't log in toast
            }
        });

    }//fbLogin

    //user authenticated, geolocation given or denied
    void enterApp() {

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
                        Log.w(TAG, "location services enabled");
                        getGeoLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.w(TAG, "request location service");
                            status.startResolutionForResult(Login.this, REQUEST_CHECK_SETTINGS);
                        } catch (Exception e) {
                            Log.w(TAG, "error making request");
                            stopGeoListeners();
                        }
                        break;
                    default:
                        Log.w(TAG, "request for location service denied");
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

                Log.w(TAG, "location given by gps");
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

                Log.w(TAG, "location given by network");
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

            Log.w(TAG, "using location " + String.valueOf(userLocation));
            enterApp();
        }
    }//stop geo listeners

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.w(TAG, "connected to play services");
        doLocationSettings();
    }//on connected

    @Override
    public void onConnectionSuspended(int i) {

    }//on connection suspended

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.w(TAG, "unable to connect to google services");
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

    protected void onResume() {

        super.onResume();
        fillEditTexts();

    }

    private void fillEditTexts() {

        String savedEmail = sp.getString("email", "");
        String savedPassword = sp.getString("password", "");
        mEmail.setText(savedEmail);
        mPassword.setText(savedPassword);

        String incomingEmail = getIntent().getStringExtra("email");
        if (incomingEmail != null) {
            mEmail.setText(incomingEmail);
        }

    }


}//Login

package com.combustiongroup.burntout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Intent main;

    EditText email;
    EditText pass;

    LocationManager lm;
    LocationListener ll_gps, ll_net;
    Location userLocation = null;

    GoogleApiClient gclient;

    final int REQUEST_CHECK_SETTINGS = 0;
    final int REQUEST_ACCESS_LOCATION = 1;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ToggleButton toggleLanguage = (ToggleButton) findViewById(R.id.toggleLanguages);

        assert toggleLanguage != null;
        toggleLanguage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Locale current = getResources().getConfiguration().locale;

                if(current.getLanguage().equals("en")){
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
                if(current.getLanguage().equals("es")){
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

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);

        assert email != null;
        assert pass != null;

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

        final View login = findViewById(R.id.loginbutton);
        assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(preLogin())
                {
                    login();
                }
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
                                try
                                {
                                    email = object.getString("email");
                                    id = object.getString("id");
                                    fname = object.getString("first_name");
                                    lname = object.getString("last_name");
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                                finally
                                {
                                    Log.w("#app", "user email: " + email);
                                    if(!email.equals("error"))
                                    {
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
    }//on create

    boolean preLogin()
    {
        //keyboard slightly obscures toast, gray on gray
        Functions.closeSoftKeyboard(Login.this);

        if(!(email.getText().length() > 0))
        {
            Toast.makeText(Login.this, getString(R.string.error_empty_email), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!(pass.getText().length() > 5))
        {
            Toast.makeText(Login.this, getString(R.string.error_password_invalid), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }//pre login

    void login()
    {

//        Intent load = new Intent(Login.this, Spinner.class);
//        startActivity(load);

        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.Login.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.w("#app", response);
                        LoginResponse r = new LoginResponse(response);
                        if(r.status.equals("one"))
                        {
                            //Spinner.refAct.finish();
                            main = new Intent(Login.this, Main.class);
                            main.putExtra("fname", r.fname);
                            main.putExtra("lname", r.lname);
                            main.putExtra("picture", r.picture);
                            main.putExtra("email", r.email);

                            Net.singleton.startHeavyTask(Login.this);
                            doSettings();
                        }
                        else if(r.status.equals("two"))
                        {
                            //Spinner.refAct.finish();
                            Toast.makeText(Login.this, getString(R.string.error_email_password_invalid), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        //Spinner.refAct.finish();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("email", email.getText().toString());
                params.put("password", pass.getText().toString());

                return params;
            }
        };
        //Net.singleton.requestQueue.add(req);
        Net.singleton.addRequest(Login.this, req);
    }//login

    void fbLogin(final String email, final String id, final String fname, final String lname)
    {
        //call to loginFB.php
        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.FBLogin.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.w("#app", response);
                        LoginResponse r = new LoginResponse(response);
                        if(r.status.equals("one"))
                        {
                            main = new Intent(Login.this, Main.class);
                            main.putExtra("fname", r.fname);
                            main.putExtra("lname", r.lname);
                            main.putExtra("picture", r.picture);
                            main.putExtra("email", r.email);

                            Net.singleton.startHeavyTask(Login.this);
                            doSettings();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                    }
                })
        {

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
    void enterApp()
    {
        Net.singleton.finishHeavyTask();
        Log.w("#app", "still live requests: "+Net.singleton.liveRequests);
        startActivity(main);
        //can't back out to this anyway
        finish();
    }//enter app

    class LoginResponse
    {
        public String status;
        public String fname;
        public String lname;
        public String picture;
        public String email;

        public LoginResponse(String raw)
        {
            try
            {
                JSONObject root = new JSONObject(raw);
                status = root.getString("status");
                fname = root.getString("fname");
                lname = root.getString("lname");
                picture = root.getString("picture");
                email = root.getString("email");
            }
            catch(Exception e)
            {
                status = "parse error";
            }
        }//Constructor
    }//LoginResponse

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

    void doSettings()
    {
        gclient = new GoogleApiClient.Builder(Login.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        gclient.connect();
    }//do settings

    void doLocationSettings()
    {
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
                switch(status.getStatusCode())
                {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.w("#app", "location services enabled");
                        getGeoLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try
                        {
                            Log.w("#app", "request location service");
                            Net.singleton.finishHeavyTask();
                            status.startResolutionForResult(Login.this, REQUEST_CHECK_SETTINGS);
                        }
                        catch(Exception e)
                        {
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

    void getGeoLocation()
    {
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

        if(ContextCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {

            if(Net.singleton.liveRequests == 0)
            {
                Net.singleton.startHeavyTask(Login.this);
            }

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll_gps);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll_net);

            new CountDownTimer(10000, 1000)
            {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    if(userLocation == null)
                    {
                        userLocation = getLastKnownLocation();
                        stopGeoListeners();
                    }
                }
            }.start();
        }
        else
        {
            ActivityCompat.requestPermissions(
                    Login.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_LOCATION
            );
        }
    }//get geo location

    void stopGeoListeners() throws SecurityException
    {
        try
        {
            lm.removeUpdates(ll_gps);
            lm.removeUpdates(ll_net);
        }
        catch(Exception e)
        {

        }
        finally
        {
            if(userLocation == null)
            {
                userLocation = new Location("");
                userLocation.setLatitude(0.0d);
                userLocation.setLongitude(0.0d);
            }

            //some result has been given for location, user previously authenticated
            main.putExtra("latitude", String.valueOf(userLocation.getLatitude()));
            main.putExtra("longitude", String.valueOf(userLocation.getLongitude()));

            Log.w("#app", "using location "+String.valueOf(userLocation));
            enterApp();
        }
    }//stop geo listeners

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        Log.w("#app", "connected to play services");
        doLocationSettings();
    }//on connected

    @Override
    public void onConnectionSuspended(int i)
    {

    }//on connection suspended

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.w("#app", "unable to connect to google services");
        stopGeoListeners();
    }//on connection failed

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CHECK_SETTINGS)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                getGeoLocation();
            }
            else
            {
                stopGeoListeners();
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }//on activity result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_ACCESS_LOCATION)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getGeoLocation();
            }
            else
            {
                stopGeoListeners();
            }
        }
    }//on request permissions result
}//Login

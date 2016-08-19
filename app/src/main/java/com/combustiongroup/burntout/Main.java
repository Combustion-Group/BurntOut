package com.combustiongroup.burntout;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.Vehicle;
import com.combustiongroup.burntout.network.dto.response.UserProfileResponse;
import com.combustiongroup.burntout.ui.VehicleAdapter;
import com.combustiongroup.burntout.util.SpinnerAlert;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import static com.combustiongroup.burntout.network.BOAPI.loginResponse;
import static com.combustiongroup.burntout.network.BOAPI.userInfo;
import static com.combustiongroup.burntout.network.BOAPI.userNotifications;
import static com.combustiongroup.burntout.network.BOAPI.userPreferences;
import static com.combustiongroup.burntout.network.BOAPI.userStats;
import static com.combustiongroup.burntout.network.BOAPI.userVehicles;

public class Main extends AppCompatActivity {

    public static final int IntentNone = -1;
    public static final int IntentMedia = 0;
    public static final int IntentAdd = 1;
    public static final int IntentEdit = 2;
    public static final int IntentEditVehicle = 3;

    private static final String TAG = "MainActivity";
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.alerts)
    ImageView alerts;
    @BindView(R.id.settings)
    ImageView settings;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.rank_title)
    TextView rankTitle;
    @BindView(R.id.reported_value)
    TextView reportedValue;
    @BindView(R.id.ranking_value)
    TextView rankingValue;
    @BindView(R.id.received_value)
    TextView receivedValue;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.vehicle_pager)
    ViewPager vehiclePager;
    @BindView(R.id.report)
    Button report;

    static boolean dataSetModified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getUserInfo(loginResponse.getEmail());

        View settings = findViewById(R.id.settings);
        assert settings != null;
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Main.this, Settings.class);
                startActivity(i);
            }
        });

        photo = (ImageView) findViewById(R.id.photo);
        assert photo != null;
        Glide
                .with(Main.this)
                .load(getIntent().getStringExtra("picture"))
                .asBitmap()
                .placeholder(R.drawable.image_icon_avatar)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new BitmapImageViewTarget(photo) {

                    @Override
                    protected void setResource(Bitmap resource) {

                        RoundedBitmapDrawable c = RoundedBitmapDrawableFactory.create(getResources(), resource);
                        c.setCircular(true);
                        photo.setImageDrawable(c);
                    }
                });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPhoto();
                photo.setMinimumHeight(100);
                photo.setMinimumWidth(100);
            }
        });
        photo.setMinimumHeight(100);
        photo.setMinimumWidth(100);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Main.this, AddVehicle.class);
                startActivityForResult(i, IntentAdd);
            }
        });


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Main.this, Report.class);
                startActivity(i);
            }
        });


        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Main.this, NotificationsDialog.class);
                i.putExtra("email", userInfo.getEmail());
                startActivity(i);
            }
        });


    }//on create

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.w("#on ActivityResult ", " We here");
        if (requestCode == IntentMedia && resultCode == Activity.RESULT_OK) {

            Uri filepath = data.getData();
//            Log.w("#app", filepath.toString());

            Glide
                    .with(Main.this)
                    .load(filepath)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_icon_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(photo) {

                        @Override
                        protected void setResource(Bitmap resource) {

                            RoundedBitmapDrawable c = RoundedBitmapDrawableFactory.create(getResources(), resource);
                            c.setCircular(true);
                            updateUserImage(getBytesForBitmap(resource));
                            photo.setImageDrawable(c);

                        }
                    });
        }//media intent
        else if (requestCode == IntentAdd && resultCode == Activity.RESULT_OK) {
            userVehicles.add(new Vehicle(
                    data.getStringExtra("plate"),
                    data.getStringExtra("model"),
                    Vehicle.getResourceForVehicleType(data.getStringExtra("type")),
                    data.getStringExtra("id"),
                    data.getStringExtra("state")
            ));
            vehiclePager.getAdapter().notifyDataSetChanged();

            Toast.makeText(Main.this, getString(R.string.vehicle_added), Toast.LENGTH_LONG).show();
        }//add vehicle intent
        else if (requestCode == IntentEdit && resultCode == Activity.RESULT_OK) {
//            Log.w("#app", "action: "+data.getStringExtra("action"));

            String a = data.getStringExtra("action");
            Intent action;
            int req = IntentNone;

//            Log.w("#app", "action for item: "+data.getIntExtra("forItem", -1));
            switch (a) {
                case "edit":
                    action = new Intent(Main.this, AddVehicle.class);
                    action.putExtra("editMode", true);
                    action.putExtra("vehicle", userVehicles.get(data.getIntExtra("forItem", 0)));
                    action.putExtra("email", userInfo.getEmail());
                    action.putExtra("forItem", data.getIntExtra("forItem", 0));
                    req = IntentEditVehicle;
                    break;

                case "delete":
//                    Log.w("#app", "delete item " + data.getIntExtra("forItem", -1));
                    deleteVehicle(data.getIntExtra("forItem", -1));
                    action = new Intent();
                    break;

                default:
                    action = new Intent();
                    break;
            }

            if (req != IntentNone) {
                startActivityForResult(action, req);
            }
        }//edit menu intent
        else if (requestCode == IntentEditVehicle && resultCode == Activity.RESULT_OK) {

            Vehicle edited = (Vehicle) data.getSerializableExtra("vehicle");
            userVehicles.set(data.getIntExtra("forItem", 0), edited);
            vehiclePager.getAdapter().notifyDataSetChanged();

//            Log.w("#app", "edit item: " + data.getIntExtra("forItem", -1));

            Toast.makeText(Main.this, getResources().getString(R.string.vehicle_edited), Toast.LENGTH_LONG).show();
        }//edit vehicle intent
    }//on activity result

    @Override
    protected void onResume() {
        super.onResume();

        if (dataSetModified) {
            dataSetModified = false;
            vehiclePager.getAdapter().notifyDataSetChanged();
//            setUpUserProfile();
        }
        if (userNotifications.isEmpty()) {
            alerts.setVisibility(View.INVISIBLE);
        }
    }//on resume

    //thank you, stack overflow
    private void addPhoto() {
        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.MEDIA_IGNORE_FILENAME, ".nomedia");

            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select an image");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, IntentMedia);
    }//add photo

    void getUserInfo(final String email) {
        Log.w(TAG, "Getting user's profile information...");
        SpinnerAlert.show(Main.this);
        BOAPI.service.getUserProfile(email).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, retrofit2.Response<UserProfileResponse> response) {
                Log.w(TAG, response.body().getResultUserProfiles().get(0).getUserinfo().get(0).toString());

                // All these variables are imported statically from BOAPI.java
                userInfo = response.body().getResultUserProfiles().get(0).getUserinfo().get(0);
                userPreferences = response.body().getResultUserProfiles().get(0).getPreferences();
                userStats = response.body().getResultUserProfiles().get(0).getStats();
                userVehicles = response.body().getResultUserProfiles().get(0).getVehicles();
                userNotifications = response.body().getResultUserProfiles().get(0).getNotifications();

                // Set up basic information on the view
                setUpUserProfile();
                //set up Vehicles
                vehiclePager.setAdapter(new VehicleAdapter(Main.this));

                //close spinner
                SpinnerAlert.dismiss(Main.this);

            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e(TAG, "Failed to login.");
                Toast.makeText(Main.this, getString(R.string.error_request), Toast.LENGTH_LONG).show();

                t.printStackTrace();
            }//onFailure
        });

    }//get user info

    public void setUpUserProfile() {
        name.setText(Html.fromHtml("<b>" + userInfo.getUserFname() + "</b> " + userInfo.getUserLname()));
        reportedValue.setText(userStats.getReported());
        receivedValue.setText(userStats.getReportee());
        rankTitle.setText(userStats.getMyRank());
        rankingValue.setText(userStats.getRanking());
    }//set user info

    public void updateUserImage(final byte[] imageData) {


        VolleyMultipartRequest req = new VolleyMultipartRequest(Request.Method.POST, Net.Urls.SetProfileImage.value,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        Log.w("#app update image", new String(response.data));
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

                params.put("email", getIntent().getStringExtra("email"));

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {

                Map<String, DataPart> data = new HashMap<>();

                data.put("filename", new DataPart(
                        "picture_for_user_" + userInfo.getUserId() + ".jpg",
                        imageData,
                        "image/jpeg"
                ));

                return data;
            }
        };
//        Net.singleton.requestQueue.add(req);
        Net.singleton.addRequest(Main.this, req);
    }//update user image

    void deleteVehicle(final int position) {
        //vehicle plate
        //user email

        if (position == -1) {
            return;
        }

        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.DeleteVehicle.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (getStatusFromSimple(response).equals("one")) {
                            Toast.makeText(Main.this, getResources().getString(R.string.vehicle_deleted), Toast.LENGTH_LONG).show();

                            userVehicles.remove(position);
                            vehiclePager.getAdapter().notifyDataSetChanged();
                        } else {
                            Toast.makeText(Main.this, getResources().getString(R.string.vehicle_not_deleted), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText(Main.this, getResources().getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("email", userInfo.getEmail());
                params.put("plate_number", userVehicles.get(position).getPlateNumber());

                return params;
            }
        };
        Net.singleton.addRequest(Main.this, req);
    }//delete vehicle

    @Override
    public void onBackPressed() {

        logout();
    }//on back pressed

    void logout() {
        Intent i = new Intent(Main.this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }//logout

    public byte[] getBytesForBitmap(Bitmap b) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        return baos.toByteArray();
    }//get bytes for bitmap


    public static String getStatusFromSimple(String raw) {
        try {
            JSONObject r = new JSONObject(raw);
            return r.getString("status");
        } catch (Exception e) {
            return "error";
        }
    }//get status from simple


}//Main

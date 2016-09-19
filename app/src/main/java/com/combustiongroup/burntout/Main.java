package com.combustiongroup.burntout;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.Net;
import com.combustiongroup.burntout.network.dto.Vehicle;
import com.combustiongroup.burntout.network.dto.response.StatusResponse;
import com.combustiongroup.burntout.network.dto.response.UserProfileResponse;
import com.combustiongroup.burntout.ui.VehicleAdapter;
import com.combustiongroup.burntout.util.PermissionUtils;
import com.combustiongroup.burntout.util.SpinnerAlert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import static com.combustiongroup.burntout.util.PermissionUtils.PermissionsRequestCode;

public class Main extends AppCompatActivity {

    public static final int IntentNone = -1;
    public static final int IntentMedia = 4;
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
    public static boolean showAlert = false;

    private Uri outputFileUri;
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserInfo(BOAPI.gLoginResponse.getEmail());

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        View settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Main.this, Settings.class);
                startActivity(i);
            }
        });

        photo = (ImageView) findViewById(R.id.photo);

        Glide.with(Main.this)
                .load(BOAPI.gLoginResponse.getPicture())
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
                PermissionUtils.requestAppPermissions(Main.this);

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
                i.putExtra("email",  BOAPI.gUserInfo.getEmail());
                startActivity(i);
            }
        });


        PermissionUtils.requestAppPermissions(this);



    }//on create

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionsRequestCode: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Main.this, "You need to have the require permissions to access this app", Toast.LENGTH_LONG).show();

                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentMedia && resultCode == Activity.RESULT_OK) {
            if (currentapiVersion < Build.VERSION_CODES.LOLLIPOP) {

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
            }else {
            final boolean isCamera;
            if (data == null) {
                isCamera = true;
                Log.e("onActivityResult","camera was use");

            } else {
                final String action = data.getAction();
                if (action == null) {
                    Log.e("onActivityResult","camera was not use");

                    isCamera = false;
                } else {
                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }

            Uri lImageFile;
            if (isCamera) {
                lImageFile = outputFileUri;
                Log.e("onActivityResult path",outputFileUri.getPath());


            } else {
                lImageFile = data == null ? null : data.getData();
            }

            Log.e("onActivityResult","back from camera");

            Glide.with(Main.this)
                    .load(lImageFile)
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
                            Log.e("onActivityResult","Before Execute");

                            updateUserImage(getBytesForBitmap(resource));
                            photo.setImageDrawable(c);

                        }
                    });

            Log.e("onActivityResult","After everything");
            }
        }//media intent
        else if (requestCode == IntentAdd && resultCode == Activity.RESULT_OK) {
            BOAPI.gUserVehicles.add(new Vehicle(
                    data.getStringExtra("plate_number"),
                    data.getStringExtra("model"),
                    data.getStringExtra("type_id"),
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
                    action.putExtra("vehicle",  BOAPI.gUserVehicles.get(data.getIntExtra("forItem", 0)));
                    action.putExtra("email",  BOAPI.gUserInfo.getEmail());
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
            BOAPI.gUserVehicles.set(data.getIntExtra("forItem", 0), edited);
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
            setUpUserProfile();
        }
        if (showAlert) {
            alerts.setVisibility(View.VISIBLE);
        }else{
            alerts.setVisibility(View.INVISIBLE);

        }

    }//on resume

    private void addPhoto() {
        PermissionUtils.requestAppPermissions(Main.this);
        if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP) {

            // Determine Uri of camera image to save.
            final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
            root.mkdirs();
            final String fname = "img_" + System.currentTimeMillis() + ".jpg";
            final File sdImageMainDirectory = new File(root, fname);
            outputFileUri = Uri.fromFile(sdImageMainDirectory);

            // Camera.
            List<Intent> cameraIntents = new ArrayList<Intent>();
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
            for (ResolveInfo res : listCam) {
                String packageName = res.activityInfo.packageName;
                Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(packageName);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                cameraIntents.add(intent);
            }

            // Filesystem.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/jpeg");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

            // Chooser of filesystem options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select an image");

            // Add the camera options.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
            if (captureIntent.resolveActivity(getPackageManager()) != null) {

                startActivityForResult(chooserIntent, IntentMedia);
            }
        }else {
            // Camera.
            final List<Intent> cameraIntents = new ArrayList<Intent>();
            final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
        }
    }//add photo

    void getUserInfo(final String email) {
        Log.w(TAG, "Getting user's profile information...");
        SpinnerAlert.show(Main.this);

        BOAPI.service.getUserProfile(email).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, retrofit2.Response<UserProfileResponse> response) {

                // All these variables are imported statically from BOAPI.java
                BOAPI.gUserInfo = response.body().getResults().get(0).getUserinfo().get(0);
                BOAPI.gUserPreferences = response.body().getResults().get(0).getPreferences();
                BOAPI.gUserStats = response.body().getResults().get(0).getStats();
                BOAPI.gUserVehicles = response.body().getResults().get(0).getVehicles();
                BOAPI.gUserNotifications = response.body().getResults().get(0).getNotifications();

                // Set up basic information on the view
                setUpUserProfile();
                //set up Vehicles
                vehiclePager.setAdapter(new VehicleAdapter(Main.this));
                //Show alerts icon if we have notifiactions
                showAlert = !BOAPI.gUserNotifications.isEmpty();
                //close spinner
                SpinnerAlert.dismiss(Main.this);

            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e(TAG, "Failed to get user profile.");
                Toast.makeText(Main.this, getString(R.string.error_request), Toast.LENGTH_LONG).show();
                SpinnerAlert.dismiss(Main.this);
                t.printStackTrace();
            }//onFailure
        });

    }//get user info

    public void setUpUserProfile() {
        name.setText(Html.fromHtml("<b>" +  BOAPI.gUserInfo.getUser_fname() + "</b> " +  BOAPI.gUserInfo.getUser_lname()));
        reportedValue.setText( BOAPI.gUserStats.getReported());
        receivedValue.setText( BOAPI.gUserStats.getReportee());
        rankTitle.setText( BOAPI.gUserStats.getRanking());
        rankingValue.setText( BOAPI.gUserStats.getMy_rank());
    }//set user info

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void updateUserImage(Uri aImageFile) {
//        File file = FileUtils.getFile(this, aImageFile);
//
//        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("filename", "picture_for_user_" +  BOAPI.gUserInfo.getUser_id() + ".jpg", reqFile);
//        SpinnerAlert.show(Main.this);
//        Log.e(TAG, file.getName());
//        Log.e(TAG, file.getPath());
//
//        BOAPI.service.uploadProfilePicure(BOAPI.gUserInfo.getEmail(), body).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//
//                Log.e(TAG, "Picture has been uploaded");
//
//                SpinnerAlert.dismiss(Main.this);
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace();
//                SpinnerAlert.dismiss(Main.this);
//            }
//        });
//    }//update user image

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
                        "picture_for_user_" + BOAPI.gUserInfo.getUser_id() + ".jpg",
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
        //vehicle plate_number
        //user email

        if (position == -1) {
            return;
        }
        //show spinner until the request gets a response
        SpinnerAlert.show(Main.this);

        BOAPI.service.deleteVehicle( BOAPI.gUserInfo.getEmail(),  BOAPI.gUserVehicles.get(position).getPlate_number()).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, retrofit2.Response<StatusResponse> response) {
                Log.i(TAG, "Deleting Car...");
                Log.i(TAG, response.body().getStatus());
                if (response.body().getStatus().equals("one")) {
                    Toast.makeText(Main.this, getResources().getString(R.string.vehicle_deleted), Toast.LENGTH_LONG).show();

                    BOAPI.gUserVehicles.remove(position);
                    vehiclePager.getAdapter().notifyDataSetChanged();
                } else {
                    Toast.makeText(Main.this, getResources().getString(R.string.vehicle_not_deleted), Toast.LENGTH_LONG).show();
                }

                // close spinner after request is done
                SpinnerAlert.dismiss(Main.this);

            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Main.this, getResources().getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();
                SpinnerAlert.dismiss(Main.this);


            }
        });

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

}//Main

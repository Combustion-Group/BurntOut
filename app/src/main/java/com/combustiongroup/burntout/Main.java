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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends AppCompatActivity {

    public static ProfileResponseParser userInfo;
    ViewPager vehiclePager;

    final int IntentNone = -1;
    final int IntentMedia = 0;
    final int IntentAdd = 1;
    final int IntentEdit = 2;
    final int IntentEditVehicle = 3;

    ImageView photo;
    View alerts;

    private static final String TAG = "SplashActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userInfo = new ProfileResponseParser();
        getUserInfo(getIntent().getStringExtra("email"));

        //set basic info from login results
        TextView name = (TextView) findViewById(R.id.name);
        assert name != null;
        name.setText(getIntent().getStringExtra("fname") + " " + getIntent().getStringExtra("lname"));

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
        View add = findViewById(R.id.add);
        assert add != null;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Main.this, AddVehicle.class);
                startActivityForResult(i, IntentAdd);
            }
        });


        vehiclePager = (ViewPager) findViewById(R.id.vehicle_pager);
        assert vehiclePager != null;
        vehiclePager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return userInfo.vehicles.size();
            }//get count

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == ((View) object);
            }//is view from object

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                final int n = position;

                View rootView = getLayoutInflater().inflate(R.layout.pager_vehicle, container, false);

                View edit = (View) rootView.findViewById(R.id.edit);
                assert edit != null;
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*userInfo.vehicles.remove(n);
                        vehiclePager.getAdapter().notifyDataSetChanged(); */
                        Intent menu = new Intent(Main.this, ProfileVehicleEditPrompt.class);
                        menu.putExtra("forItem", n);
                        startActivityForResult(menu, IntentEdit);
                    }
                });

                TextView plate = (TextView) rootView.findViewById(R.id.plate);
                TextView model = (TextView) rootView.findViewById(R.id.model);
                ImageView image = (ImageView) rootView.findViewById(R.id.vehicle);

                assert plate != null && model != null && image != null;

                plate.setText(userInfo.vehicles.get(position).plate);
                model.setText(userInfo.vehicles.get(position).model);
                image.setImageResource(userInfo.vehicles.get(position).resource);

                ((ViewPager) container).addView(rootView);
                return rootView;
            }//instantiate item

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

                ((ViewPager) container).removeView((View) object);
            }//destroy item

            @Override
            public int getItemPosition(Object object) {

                return POSITION_NONE;
            }//get item position
        });

        View report = findViewById(R.id.report);
        if (report != null) {
            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(Main.this, Report.class);
                    startActivity(i);
                }
            });
        }

        alerts = findViewById(R.id.alerts);
        assert alerts != null;
        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Main.this, NotificationsDialog.class);
                i.putExtra("email", userInfo.email);
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
            userInfo.vehicles.add(new Vehicle(
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
                    action.putExtra("vehicle", userInfo.vehicles.get(data.getIntExtra("forItem", 0)));
                    action.putExtra("email", userInfo.email);
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
            userInfo.vehicles.set(data.getIntExtra("forItem", 0), edited);
            vehiclePager.getAdapter().notifyDataSetChanged();

//            Log.w("#app", "edit item: " + data.getIntExtra("forItem", -1));

            Toast.makeText(Main.this, getResources().getString(R.string.vehicle_edited), Toast.LENGTH_LONG).show();
        }//edit vehicle intent
    }//on activity result

    @Override
    protected void onResume() {
        super.onResume();

        if (userInfo.dataSetModified) {
            userInfo.dataSetModified = false;
            vehiclePager.getAdapter().notifyDataSetChanged();
            setUserInfo();
        }
        if (userInfo.check_alerts) {
            alerts.setVisibility(View.INVISIBLE);
        }
    }//on resume

    //thank you, stack overflow
    private void addPhoto() {
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
    }//add photo

    void getUserInfo(final String email) {

        //launch request for user info for given email
        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.ProfileInformation.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.w("#app", response);
                        userInfo.parse(response);
                        userInfo.email = getIntent().getStringExtra("email");
                        setUserInfo();
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

                return params;
            }
        };
        Net.singleton.addRequest(Main.this, req);
    }//get user info

    public void setUserInfo() {

        TextView name = (TextView) findViewById(R.id.name);
        TextView reported = (TextView) findViewById(R.id.reported_value);
        TextView received = (TextView) findViewById(R.id.received_value);
        TextView rankTitle = (TextView) findViewById(R.id.rank_title);
        TextView rankValue = (TextView) findViewById(R.id.ranking_value);

        assert name != null &&
                reported != null &&
                received != null &&
                rankTitle != null &&
                rankValue != null;

        name.setText(Html.fromHtml("<b>" + userInfo.fname + "</b> " + userInfo.lname));
        reported.setText(userInfo.reported);
        received.setText(userInfo.reportee);
        rankTitle.setText(userInfo.rankTitle);
        rankValue.setText(userInfo.ranking);
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
                        "picture_for_user_" + userInfo.id + ".jpg",
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

                            userInfo.vehicles.remove(position);
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

                params.put("email", userInfo.email);
                params.put("plate_number", userInfo.vehicles.get(position).plate);

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

    public class ProfileResponseParser implements Serializable {
        public boolean pushEnabled, isFB;
        public String fname, lname, reported, reportee, ranking, rankTitle, id, email;
        public ArrayList<Vehicle> vehicles = new ArrayList<>();
        //if the vehicle list is modified from another activity
        public boolean dataSetModified = false, check_alerts;

        public void parse(String raw) {

            try {
                JSONObject root = new JSONObject(raw);
                JSONArray results = root.getJSONArray("results");
                JSONObject userInfoContainer = results.getJSONObject(0);
                //user info
                JSONArray users = userInfoContainer.getJSONArray("userinfo");
                JSONObject user = users.getJSONObject(0);
                fname = user.getString("user_fname");
                lname = user.getString("user_lname");
                id = user.getString("user_id");
                isFB = user.getString("user_isFB").equals("1");

                //user preferences
                JSONObject prefs = userInfoContainer.getJSONObject("preferences");
                pushEnabled = prefs.getString("push_notifications").equals("1");

                //user notificaitons - Check if there are any notifications
                JSONArray notifications = userInfoContainer.getJSONArray("notifications");
                if (notifications.length() < 1) {
                    check_alerts = true;
                } else {
                    check_alerts = false;
                }

                //user vehicles
                JSONArray vehicles = userInfoContainer.getJSONArray("vehicles");
                for (int i = 0; i < vehicles.length(); i++) {
                    JSONObject vehicle = vehicles.getJSONObject(i);
                    this.vehicles.add(new Vehicle(
                            vehicle.getString("plate_number"),
                            vehicle.getString("car_model"),
                            Vehicle.getResourceForVehicleType(vehicle.getString("vehicle_type_id")),
                            vehicle.getString("vehicle_id"),
                            vehicle.getString("plate_state")
                    ));
                }

                vehiclePager.getAdapter().notifyDataSetChanged();

                //stats
                JSONObject stats = userInfoContainer.getJSONObject("stats");
                reported = stats.getString("reported");
                reportee = stats.getString("reportee");
                ranking = stats.getString("my_rank");
                rankTitle = stats.getString("ranking");
            } catch (Exception e) {
                pushEnabled = false;
                e.printStackTrace();
            }
        }//parse
    }//ProfileResponseParser

    public static String getStatusFromSimple(String raw) {
        try {
            JSONObject r = new JSONObject(raw);
            return r.getString("status");
        } catch (Exception e) {
            return "error";
        }
    }//get status from simple


}//Main

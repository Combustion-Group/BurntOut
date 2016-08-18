package com.combustiongroup.burntout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsDialog extends AppCompatActivity {

    private static final String TAG = "";
    ViewPager pager;
    public static NotificationsResponseParser notifications;
    ImageView photo;
    View alerts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_dialog);

        alerts = findViewById(R.id.alert_icon);
        assert alerts != null;
        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notifications = new NotificationsResponseParser();

        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.ProfileInformation.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            notifications.parse(response);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        };

        Net.singleton.addRequest(NotificationsDialog.this, req);

        pager = (ViewPager) findViewById(R.id.view_pager);
        assert pager != null;

        pager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {

                return notifications.notificationsList.size();
            }//get count

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }//is view from object

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {

                View rootView = getLayoutInflater().inflate(R.layout.el_notification, container, false);
                rootView.setTag("n" + position);

                final View yes = rootView.findViewById(R.id.yes),
                        no = rootView.findViewById(R.id.no),
                        line = rootView.findViewById(R.id.line),
                        dismiss = rootView.findViewById(R.id.dismiss),
                        question = rootView.findViewById(R.id.question);


                assert yes != null && no != null && line != null && dismiss != null && question != null;


                final int n = position;

                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        yes.setVisibility(View.VISIBLE);
                        no.setVisibility(View.VISIBLE);
                        line.setVisibility(View.VISIBLE);
                        question.setVisibility(View.VISIBLE);
                        dismiss.setVisibility(View.INVISIBLE);

                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifications.deleteNotification(n);
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        notifications.deleteNotification(n);
                    }
                });

                //                Get all textviews
                TextView timestamp = (TextView) rootView.findViewById(R.id.timestamp);
                TextView message = (TextView) rootView.findViewById(R.id.message);
                TextView plate = (TextView) rootView.findViewById(R.id.plate);
                TextView name = (TextView) rootView.findViewById(R.id.name);
                TextView rank_title = (TextView) rootView.findViewById(R.id.rank_title);
                TextView reported = (TextView) rootView.findViewById(R.id.reported_value);
                TextView received = (TextView) rootView.findViewById(R.id.received_value);
                TextView ranking = (TextView) rootView.findViewById(R.id.ranking_value);
                TextView report = (TextView) rootView.findViewById(R.id.report);
                photo = (ImageView) rootView.findViewById(R.id.photo);


                assert timestamp != null && message != null && plate != null &&
                        name != null && reported != null && received != null
                        && report != null && photo != null;

                //                populate all text views
                timestamp.setText(notifications.notificationsList.get(n).created);
                Glide
                        .with(NotificationsDialog.this)
                        .load(notifications.notificationsList.get(n).profile_picture)
                        .asBitmap()
                        .override(80, 80)
                        .placeholder(R.drawable.image_icon_avatar)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(photo) {

                            @Override
                            protected void setResource(Bitmap resource) {

                                RoundedBitmapDrawable c = RoundedBitmapDrawableFactory.create(
                                        getResources(), resource);
                                c.setCircular(true);
                                photo.setImageDrawable(c);
                            }
                        });
                message.setText(notifications.notificationsList.get(n).message);
                plate.setText(notifications.notificationsList.get(n).plate);
                rank_title.setText(notifications.notificationsList.get(n).ranking_title);
                name.setText(notifications.notificationsList.get(n).firstname + " " +
                        notifications.notificationsList.get(n).lastname);
                reported.setText(notifications.notificationsList.get(n).reported);
                received.setText(notifications.notificationsList.get(n).received);
                ranking.setText(notifications.notificationsList.get(n).ranking);
                report.setText(notifications.notificationsList.get(n).firstname + " " +
                        notifications.notificationsList.get(n).lastname + " has reported that your " +
                        notifications.notificationsList.get(n).lights + " are not working properly.");
                RelativeLayout vehicleView = (RelativeLayout) rootView.findViewById(R.id.vehicle);

                int vehicleType = Integer.parseInt(notifications.notificationsList.get(n).vehicle_type);
                switch (vehicleType) {
                    case 0:
                        View car = getLayoutInflater().inflate(R.layout.el_car, null);
                        new VehicleLights(
                                R.layout.el_car,
                                new int[]{
                                        R.id.front_0,
                                        R.id.front_1,
                                        R.id.front_2,
                                        R.id.front_3,
                                        R.id.back_0,
                                        R.id.back_1,
                                        R.id.back_2,
                                        R.id.back_3,
                                        R.id.back_4,
                                        R.id.back_5
                                },
                                new int[]{
                                        R.color.yellow,
                                        R.color.yellow,
                                        R.color.yellow,
                                        R.color.yellow,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.yellow,
                                        R.color.button_red,
                                        R.color.button_red
                                },
                                new String[]{
                                        "Front Right Headlight",
                                        "Front Right Fog Light",
                                        "Front Left Headlight",
                                        "Front Left Fog Light",
                                        "Back Left Tail Light",
                                        "Back Left Brake Light",
                                        "Back Center Brake Light",
                                        "Back License Plate Light",
                                        "Back Right Brake Light",
                                        "Back Right Tail Light"
                                }).showBurntouts(car, notifications.notificationsList.get(n).lights);
                        vehicleView.addView(car);
                        break;
                    case 1:
                        View bike = getLayoutInflater().inflate(R.layout.el_bike, null);

                        new VehicleLights(
                                R.layout.el_bike,
                                new int[]{
                                        R.id.front_0,
                                        R.id.back_0,
                                        R.id.back_1,
                                        R.id.back_2
                                },
                                new int[]{
                                        R.color.yellow,
                                        android.R.color.holo_orange_light,
                                        R.color.button_red,
                                        android.R.color.holo_orange_light
                                },
                                new String[]{
                                        "Front Headlight",
                                        "Back Left Turn Signal",
                                        "Back Brake Light",
                                        "Back Right Turn Signal"
                                }).showBurntouts(bike, notifications.notificationsList.get(n).lights);
                        vehicleView.addView(bike);
                        break;
                    case 2:
                        View bus = getLayoutInflater().inflate(R.layout.el_bus, null);
                        new VehicleLights(
                                R.layout.el_bus,
                                new int[]{
                                        R.id.front_0,
                                        R.id.front_1,
                                        R.id.front_2,
                                        R.id.front_3,
                                        R.id.back_0,
                                        R.id.back_1,
                                        R.id.back_2,
                                        R.id.back_3,
                                        R.id.back_4,
                                        R.id.back_5,
                                        R.id.back_6,
                                        R.id.back_7
                                },
                                new int[]{
                                        R.color.yellow,
                                        R.color.yellow,
                                        R.color.yellow,
                                        R.color.yellow,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red
                                },
                                new String[]{
                                        "Front Right Headlight",
                                        "Front Right Marker Light",
                                        "Front Left Marker Light",
                                        "Front Left Headlight",
                                        "Back Left Brake Light",
                                        "Back Left Marker Light",
                                        "Back Left Tail Light",
                                        "Back Center Marker Light",
                                        "Back Center Brake Light",
                                        "Back Right Tail Light",
                                        "Back Right Marker Light",
                                        "Back Right Brake Light"
                                }).showBurntouts(bus, notifications.notificationsList.get(n).lights);

                        vehicleView.addView(bus);
                        break;
                    case 3:
                        View truck = getLayoutInflater().inflate(R.layout.el_truck, null);


                        new VehicleLights(
                                R.layout.el_truck,
                                new int[]{
                                        R.id.front_0,
                                        R.id.front_1,
                                        R.id.back_0,
                                        R.id.back_1,
                                        R.id.back_2,
                                        R.id.back_3,
                                        R.id.back_4,
                                        R.id.back_5,
                                        R.id.back_6
                                },
                                new int[]{
                                        R.color.yellow,
                                        R.color.yellow,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red,
                                        R.color.button_red
                                },
                                new String[]{
                                        "Front Right Headlight",
                                        "Front Left Headlight",
                                        "Back Left Tail Light",
                                        "Back Left Brake Light",
                                        "Back Left Marker Light",
                                        "Back Center Marker Light",
                                        "Back Right Tail Light",
                                        "Back Right Brake Light",
                                        "Back Right Marker Light"
                                }).showBurntouts(truck, notifications.notificationsList.get(n).lights);
                        vehicleView.addView(truck);
                        break;
                }

                container.addView(rootView);
                return rootView;
            }//instantiate

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

                container.removeView(((View) object));
            }//destroy item

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }//get item position
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                View rootView = pager.findViewWithTag("n" + position);
//
//                PercentRelativeLayout container_v = (PercentRelativeLayout) rootView.findViewById(R.id.container_vehicle);
//                container_v.setBackgroundResource(R.drawable.style_circle_blue);
//
//                PercentRelativeLayout front_view = (PercentRelativeLayout) rootView.findViewById(R.id.front);
//                PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) front_view.getLayoutParams();
//                PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
//                info.heightPercent = 0.90f;
//                info.widthPercent = 0.90f;
//                front_view.requestLayout();
//
//                PercentRelativeLayout back_view = (PercentRelativeLayout) rootView.findViewById(R.id.back);
//                PercentRelativeLayout.LayoutParams params2 = (PercentRelativeLayout.LayoutParams) back_view.getLayoutParams();
//                PercentLayoutHelper.PercentLayoutInfo info2 = params2.getPercentLayoutInfo();
//                info2.heightPercent = 0.90f;
//                info2.widthPercent = 0.90f;
//                back_view.requestLayout();

            }

            @Override
            public void onPageSelected(int position) {

                View rootView = pager.findViewWithTag("n" + position);

                View yes = rootView.findViewById(R.id.yes),
                        no = rootView.findViewById(R.id.no),
                        line = rootView.findViewById(R.id.line),
                        dismiss = rootView.findViewById(R.id.dismiss),
                        question = rootView.findViewById(R.id.question);
                assert yes != null && no != null && line != null && dismiss != null && question != null;
                yes.setVisibility(View.INVISIBLE);
                no.setVisibility(View.INVISIBLE);
                line.setVisibility(View.INVISIBLE);
                question.setVisibility(View.INVISIBLE);
                dismiss.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }//on create

    public class NotificationsResponseParser implements Serializable {
        public ArrayList<Notifications> notificationsList = new ArrayList<>();

        public void parse(String raw) {

            try {
                Log.w("#app", raw);
                JSONObject root = new JSONObject(raw);
                JSONArray results = root.getJSONArray("results");
                JSONObject notificationsContainer = results.getJSONObject(0);
                JSONArray notifications = notificationsContainer.getJSONArray("notifications");

                for (int i = 0; i < notifications.length(); i++) {

                    JSONObject notification = notifications.getJSONObject(i);

                    notificationsList.add(new Notifications(
                            notification.getString("user_fname"),
                            notification.getString("user_lname"),
                            notification.getString("picture"),
                            notification.getString("message"),
                            notification.getString("notifier_ranking"),
                            notification.getString("lights_out"),
                            notification.getString("created"),
                            notification.getString("plate_number"),
                            notification.getString("notifier_reported_count"),
                            notification.getString("notifier_reporter_count"),
                            notification.getString("theranking"),
                            notification.getString("notification_id"),
                            notification.getString("vehicle_type")


                    ));

                }
                pager.getAdapter().notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//parse

        public void deleteNotification(final int positions) {


            if (positions == -1) {
                return;
            }

            StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.DeleteNotification.value,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(NotificationsDialog.this, getString(R.string.notifiation_deleted), Toast.LENGTH_LONG).show();
                            notifications.notificationsList.remove(positions);
                            pager.getAdapter().notifyDataSetChanged();
                            if (notifications.notificationsList.size() == 0) {
                                finish();
                                Main.userInfo.check_alerts = true;

                                Main.userInfo.dataSetModified = true;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(NotificationsDialog.this, getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();


                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();

                    params.put("notification_id", notificationsList.get(positions).notification_id);
                    return params;
                }
            };
            Net.singleton.addRequest(NotificationsDialog.this, req);
        }//delete Notification


    }//ProfileResponseParser

    class VehicleLights {
        int layout;
        public int[] ids;
        public int[] colors;
        public boolean[] active;
        public String[] issues;

        public VehicleLights(int layout, int[] ids, int[] colors, String[] issues) {
            this.layout = layout;
            this.ids = ids;
            this.colors = colors;
            this.issues = issues;
            this.active = new boolean[ids.length];
            for (int i = 0; i < ids.length; i++) {
                active[i] = true;
            }
        }//Constructor

        public void showBurntouts(final View view, String burntouts) {
            String[] result = burntouts.split(", ");

            TextView lblFront = (TextView) view.findViewById(R.id.lbl_front);
            lblFront.setVisibility(View.GONE);
            TextView lblBack = (TextView) view.findViewById(R.id.lbl_back);
            lblBack.setVisibility(View.GONE);

            for (int i = 0; i < active.length; i++) {
                final int n = i;

                final View l = view.findViewById(this.ids[i]);
                l.setBackgroundResource(android.R.color.transparent);

                if (l != null) {
                    Log.w("This is one:", l.toString());

                    if (active[n]) {
                        for (int s = 0; s < result.length; s++) {
                            Log.w("These are the lights: ", result[s]);
                            Log.w("These are the issues: ", issues[i]);

                            if (result[s].equals(issues[i])) {
                                l.setBackgroundResource(colors[i]);
                            }
                        }

                    } else {
                        l.setBackgroundResource(android.R.color.transparent);

                    }
                    PercentRelativeLayout container_v = (PercentRelativeLayout) view.findViewById(R.id.container_vehicle);
                    container_v.setBackgroundResource(R.drawable.style_circle_blue);
//                    new CountDownTimer(3000000, 2000) {
//                        boolean showFrontBack = true;
//
//                        public void onTick(long millisUntilFinished) {
//                            PercentRelativeLayout front = (PercentRelativeLayout) view.findViewById(R.id.front);
//                            PercentRelativeLayout back = (PercentRelativeLayout) view.findViewById(R.id.back);

//
//
//                            if (showFrontBack) {
//                                front.setVisibility(View.INVISIBLE);
//                                back.setVisibility(View.VISIBLE);
//
//                                showFrontBack = false;
//                            } else {
//                                back.setVisibility(View.INVISIBLE);
//                                front.setVisibility(View.VISIBLE);
//
//                                showFrontBack = true;
//
//
//                            }
//                        }

//                        public void onFinish() {
//                        }
//                    }.start();

                }
            }


        }


    }//VehicleLights


}//NotificationsDialog

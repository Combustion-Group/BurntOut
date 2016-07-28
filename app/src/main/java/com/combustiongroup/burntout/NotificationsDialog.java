package com.combustiongroup.burntout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
                reported.setText(Integer.toString(notifications.notificationsList.get(n).reported));
                received.setText(Integer.toString(notifications.notificationsList.get(n).received));
                ranking.setText(Integer.toString(notifications.notificationsList.get(n).ranking));
                report.setText(notifications.notificationsList.get(n).firstname + " " +
                        notifications.notificationsList.get(n).lastname + " has reported that your " +
                        notifications.notificationsList.get(n).lights + " are not working properly.");

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
                            notification.getInt("notifier_reported_count"),
                            notification.getInt("notifier_reporter_count"),
                            notification.getInt("theranking"),
                            notification.getInt("notification_id")

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
                                Main.userInfo.check_alerts= true;

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

                    params.put("notification_id", Integer.toString(notificationsList.get(positions).notification_id));
                    return params;
                }
            };
            Net.singleton.addRequest(NotificationsDialog.this, req);
        }//delete Notification


    }//ProfileResponseParser




}//NotificationsDialog

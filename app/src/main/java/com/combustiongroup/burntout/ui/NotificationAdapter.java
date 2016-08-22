package com.combustiongroup.burntout.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.combustiongroup.burntout.NotificationsDialog;
import com.combustiongroup.burntout.R;
import com.combustiongroup.burntout.network.BOAPI;

/**
 * Created by qualixium-imac on 8/20/16.
 */

public class NotificationAdapter extends PagerAdapter {
    private static final String TAG = "NotificationAdapter";
    public static Activity activity;

//    @BindView(R.id.report)
//    TextView report;
//    @BindView(R.id.timestamp)
//    TextView timestamp;
//    @BindView(R.id.message)
//    TextView message;
//    @BindView(R.id.plate_number)
//    TextView plate_number;
//    @BindView(R.id.vehicle)
//    RelativeLayout vehicle;
//    @BindView(R.id.photo)
//    ImageView photo;
//    @BindView(R.id.name)
//    TextView name;
//    @BindView(R.id.rank_title)
//    TextView rankTitle;
//    @BindView(R.id.reported_value)
//    TextView reportedValue;
//    @BindView(R.id.ranking_value)
//    TextView rankingValue;
//    @BindView(R.id.received_value)
//    TextView receivedValue;
//    @BindView(R.id.dismiss)
//    Button dismiss;
//    @BindView(R.id.no)
//    Button no;
//    @BindView(R.id.yes)
//    Button yes;

    public NotificationAdapter(Activity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {

        return BOAPI.gUserNotifications.size();
    }//get count

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }//is view from object

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());

        View rootView = inflater.inflate(R.layout.el_notification, container, false);
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
                NotificationsDialog.deleteNotification(n);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NotificationsDialog.deleteNotification(n);
            }
        });

        //                Get all textviews
        TextView timestamp = (TextView) rootView.findViewById(R.id.timestamp);
        TextView message = (TextView) rootView.findViewById(R.id.message);
        TextView plate = (TextView) rootView.findViewById(R.id.plate_number);
        TextView name = (TextView) rootView.findViewById(R.id.name);
        TextView rank_title = (TextView) rootView.findViewById(R.id.rank_title);
        TextView reported = (TextView) rootView.findViewById(R.id.reported_value);
        TextView received = (TextView) rootView.findViewById(R.id.received_value);
        TextView ranking = (TextView) rootView.findViewById(R.id.ranking_value);
        TextView report = (TextView) rootView.findViewById(R.id.report);
        final ImageView photo = (ImageView) rootView.findViewById(R.id.photo);


        assert timestamp != null && message != null && plate != null &&
                name != null && reported != null && received != null
                && report != null && photo != null;

        //                populate all text views
        timestamp.setText(BOAPI.gUserNotifications.get(n).getCreated());
        Log.e(TAG, BOAPI.gUserNotifications.get(n).getPicture());
        Glide
                .with(activity.getApplicationContext())
                .load(BOAPI.gUserNotifications.get(n).getPicture())
                .asBitmap()
                .override(80, 80)
                .placeholder(R.drawable.image_icon_avatar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new BitmapImageViewTarget(photo) {

                    @Override
                    protected void setResource(Bitmap resource) {

                        RoundedBitmapDrawable c = RoundedBitmapDrawableFactory.create(
                                activity.getResources(), resource);
                        c.setCircular(true);
                       photo.setImageDrawable(c);
                    }
                });
        message.setText(BOAPI.gUserNotifications.get(n).getMessage());
        plate.setText(BOAPI.gUserNotifications.get(n).getPlate_number());
        rank_title.setText(BOAPI.gUserNotifications.get(n).getNotifier_ranking());
        name.setText(BOAPI.gUserNotifications.get(n).getUser_fname() + " " +
                BOAPI.gUserNotifications.get(n).getUser_lname());
        reported.setText(BOAPI.gUserNotifications.get(n).getNotifier_reported_count());
        received.setText(BOAPI.gUserNotifications.get(n).getNotifier_reporter_count());
        ranking.setText(BOAPI.gUserNotifications.get(n).getTheranking());
        report.setText(BOAPI.gUserNotifications.get(n).getUser_fname() + " " +
                BOAPI.gUserNotifications.get(n).getUser_lname() + " has notifier_reported_count that your " +
                BOAPI.gUserNotifications.get(n).getLights_out() + " are not working properly.");
        RelativeLayout vehicleView = (RelativeLayout) rootView.findViewById(R.id.vehicle);

        int vehicleType = Integer.parseInt(BOAPI.gUserNotifications.get(n).getVehicle_type());
        switch (vehicleType) {
            case 0:
                View car = inflater.inflate(R.layout.el_car, null);
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
                        }).showBurntouts(car, BOAPI.gUserNotifications.get(n).getLights_out());
                vehicleView.addView(car);
                break;
            case 1:
                View bike = inflater.inflate(R.layout.el_bike, null);

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
                        }).showBurntouts(bike, BOAPI.gUserNotifications.get(n).getLights_out());
                vehicleView.addView(bike);
                break;
            case 2:

            View truck = inflater.inflate(R.layout.el_truck, null);

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
                    }).showBurntouts(truck, BOAPI.gUserNotifications.get(n).getLights_out());
            vehicleView.addView(truck);
            break;
            case 3:
                View bus = inflater.inflate(R.layout.el_bus, null);
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
                        }).showBurntouts(bus, BOAPI.gUserNotifications.get(n).getLights_out());

                vehicleView.addView(bus);
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
                            Log.w("These are the lights ", result[s]);
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
                }
            }
        }
    }//VehicleLights
}

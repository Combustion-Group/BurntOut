package com.combustiongroup.burntout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Report extends AppCompatActivity {

    final int IntentState = 0;

    TextView state;
    EditText plate;
    EditText message;
    TextView details;
    ViewPager vehiclePager;
    int selected;
    VehicleLights[] lights = new VehicleLights[]{
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
                    }),
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
                    }),
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
                    }),
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
                            R.color.yellow  ,
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
                    })
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        details = (TextView) findViewById(R.id.issues);
        plate = (EditText) findViewById(R.id.plate_number);
        message = (EditText) findViewById(R.id.message);

        vehiclePager = (ViewPager) findViewById(R.id.vehicle_pager);
        assert vehiclePager != null;

        vehiclePager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return lights.length;
            }//get count

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == ((View) object);
            }//is view from object

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                View rootView = getLayoutInflater().inflate(lights[position].layout, container, false);
                //using tags because getting children by index (for reset) occasionally gave the wrong view
                rootView.setTag("n"+position);

                lights[position].bind(rootView);

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
        });//pager adapter

        vehiclePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                selected = position;
                Log.w("#app", "scrolled to page "+position);
                lights[position].reset( vehiclePager.findViewWithTag("n"+position) );
                lights[position].buildReport();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });//page change listener

        state = (TextView) findViewById(R.id.state);
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Report.this, ValuePickerList.class);
                startActivityForResult(i, IntentState);
            }
        });

        View submit = findViewById(R.id.submit);
        assert submit != null;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(preSubmit())
                {
                    reportBurnout();
                }
            }
        });
    }//on create

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == IntentState && resultCode == RESULT_OK)
        {
            state.setText(data.getStringExtra("choice"));
        }
    }//on activity result

    public boolean preSubmit()
    {

        if(plate.getText().length() < 1)
        {
            Toast.makeText(Report.this, getString(R.string.license_empty), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(details.length() < 1)
        {
            Toast.makeText(Report.this, getString(R.string.complete_step2), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }//pre submit

    void reportBurnout()
    {
        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.ReportBurnout.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.w("Report response", response.toString());

//                        String status = Main.getStatusFromSimple(response);
                        ReportResponse res = new ReportResponse(response);
                        if(res.status.equals("one"))
                        {
                            Log.w("#app status: ", res.status);

                            if(res.key)
                            {
                                Main.userInfo.reported = String.valueOf(Integer.parseInt(Main.userInfo.reported) + 1);
                                Main.userInfo.dataSetModified = true;
                                finish();
                                Toast.makeText(Report.this, getString(R.string.report_submitted), Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(Report.this, getString(R.string.error_submitting_report), Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(Report.this, getString(R.string.error_reporting_yourself), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(Report.this, getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();

                params.put("email", Main.userInfo.email);
                params.put("vehicle_type", String.valueOf(selected));
                params.put("plate_state", state.getText().toString());
                params.put("license_plate", plate.getText().toString());
                params.put("lights_out", details.getText().toString());
                params.put("special_message", message.getText().toString());

                Log.w("Report sent", params.toString());

                return params;
            }
        };
        Net.singleton.addRequest(Report.this, req);
    }//report burnout

    class VehicleLights
    {
        int layout;
        public int[] ids;
        public int[] colors;
        public boolean[] active;
        public String[] issues;

        public VehicleLights(int layout, int[] ids, int[] colors, String[] issues)
        {
            this.layout = layout;
            this.ids = ids;
            this.colors = colors;
            this.issues = issues;
            this.active = new boolean[ids.length];
            for(int i = 0;i < ids.length;i++)
            {
                active[i] = true;
            }
        }//Constructor

        public void bind(View view)
        {

            for(int i = 0;i < active.length;i++)
            {
                final int n = i;

                final View l = view.findViewById(this.ids[i]);
                if(l != null)
                {

                    if(active[n])
                    {
                        l.setBackgroundResource(colors[i]);
                    }
                    else
                    {
                        l.setBackgroundResource(android.R.color.transparent);
                    }

                    l.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            active[n] = !active[n];
                            if(!active[n])
                            {
                                l.setBackgroundResource(android.R.color.transparent);
                            }
                            else
                            {
                                l.setBackgroundResource(colors[n]);
                            }

                            buildReport();
                        }
                    });
                }
            }
        }//bind

        public void reset(View view)
        {
            for(int i = 0;i < active.length;i++)
            {
                active[i] = true;
                View l = view.findViewById(ids[i]);
                if(l != null)
                {
                    l.setBackgroundResource(colors[i]);
                }
            }
        }//reset

        public void buildReport()
        {
            String report = "";
            boolean first = true;

            for(int i = 0;i < active.length;i++)
            {
                if(!active[i])
                {
                    if(first)
                    {
                        first = false;
                    }
                    else
                    {
                        report += ", ";
                    }
                    report += issues[i];
                }
            }

            details.setText(report);
        }//build report
    }//VehicleLights

    class ReportResponse
    {
        public String status;//one - it might have worked!
        public boolean key;//true - it worked!
        public ReportResponse(String raw)
        {
            try
            {
                JSONObject root = new JSONObject(raw);
                //status one or two or whatever
                status = root.getString("status");
                //key is false or null
                key = !root.isNull("key");
            }
            catch(Exception c)
            {
                status = "parse error";
            }
        }//Constructor
    }//Report Response
}//Report

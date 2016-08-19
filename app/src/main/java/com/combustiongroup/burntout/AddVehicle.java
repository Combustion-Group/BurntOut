package com.combustiongroup.burntout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.Vehicle;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddVehicle extends AppCompatActivity {

    final int IntentState = 0;

    ViewPager vehicles;
    int[] images = new int[]{
            R.drawable.image_car_front,
            R.drawable.image_bike_front,
            R.drawable.image_truck_front,
            R.drawable.image_bus_front};

    String[] names;

    int selected = 0;
    TextView state;
    EditText plate;
    EditText model;

    boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        names = new String[]{ getResources().getString(R.string.car),
                getResources().getString(R.string.bike), getResources().getString(R.string.truck),
                getResources().getString(R.string.bus)};

        editMode = getIntent().getBooleanExtra("editMode", false);

        vehicles = (ViewPager) findViewById(R.id.vehicle_pager);
        state = (TextView) findViewById(R.id.state);
        final View submit = findViewById(R.id.submit);
        plate = (EditText) findViewById(R.id.plate_number);
        model = (EditText) findViewById(R.id.vehicle_make);

        assert vehicles != null && state != null && submit != null;

        vehicles.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return images.length;
            }//get count

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == ((View) object);
            }//is view from object

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                View rootView = getLayoutInflater().inflate(R.layout.pager_image_label_bottom, container, false);

                TextView name = (TextView) rootView.findViewById(R.id.name);
                ImageView image = (ImageView) rootView.findViewById(R.id.image);

                assert name != null && image != null;

                name.setText(names[position]);
                image.setImageResource(images[position]);

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
        vehicles.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                selected = position;
                Log.w("#app", "scrolled to: "+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });//page change listener

        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AddVehicle.this, ValuePickerList.class);
                startActivityForResult(i, IntentState);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(preSubmit())
                {
                    if(editMode)
                    {
                        editVehicle();
                    }
                    else
                    {
                        registerVehicle();
                    }
                }
            }
        });

        if(editMode)
        {
            ((Button) submit).setText(R.string.save_changes);
            Vehicle v = (Vehicle) getIntent().getSerializableExtra("vehicle");
            plate.setText(v.getPlateNumber());
            model.setText(v.getCarModel());
            state.setText(v.getPlateState());
            vehicles.setCurrentItem( Vehicle.getTypeForResource( v.getResource() ) );
        }
    }//on create

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if(requestCode == IntentState && resultCode == Activity.RESULT_OK)
        {
            state.setText(data.getStringExtra("choice"));
        }
    }//on activity result

    boolean preSubmit()
    {

        if(plate.getText().length() < 1)
        {
            Toast.makeText(AddVehicle.this, getResources().getString(R.string.license_empty), Toast.LENGTH_LONG).show();
            return false;
        }
        else if(model.getText().length() < 1)
        {
            Toast.makeText(AddVehicle.this, getResources().getString(R.string.car_model_empty), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }//presubmit

    void registerVehicle()
    {
        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.AddVehicle.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        AddVehicleResponse res = new AddVehicleResponse(response);
                        if(res.status.equals("one"))
                        {
                            Intent rdata = new Intent();
                            rdata.putExtra("plate", plate.getText().toString());
                            rdata.putExtra("model", model.getText().toString());
                            rdata.putExtra("type", String.valueOf(selected));
                            rdata.putExtra("id", res.id);
                            rdata.putExtra("state", state.getText().toString());

                            setResult(Activity.RESULT_OK, rdata);
                            finish();
                        }
                        else if(res.status.equals("two"))
                        {
                            Toast.makeText(AddVehicle.this, getResources().getString(R.string.license_already_register), Toast.LENGTH_LONG).show();
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

                params.put("email", BOAPI.userInfo.getEmail());
                params.put("vehicle_type", ""+selected);
                params.put("car_model", model.getText().toString());
                params.put("plate_state", state.getText().toString());
                params.put("plate_number", plate.getText().toString());

                return params;
            }
        };
        Net.singleton.addRequest(AddVehicle.this, req);
    }//register vehicle

    void editVehicle()
    {
        //construct new vehicle to submit for editing & pass back to update display
        final Vehicle edited = new Vehicle(
                plate.getText().toString(),
                model.getText().toString(),
                Vehicle.getResourceForVehicleType(String.valueOf(selected)),
                ((Vehicle) getIntent().getSerializableExtra("vehicle")).getVehicleId(),
                state.getText().toString()
        );

        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.EditVehicle.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //response is "one" in all cases, so it worked, maybe!
                        Intent rdata = new Intent();
                        rdata.putExtra("vehicle", edited);
                        rdata.putExtra("forItem", getIntent().getIntExtra("forItem", 0));

                        setResult(Activity.RESULT_OK, rdata);
                        finish();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText(AddVehicle.this, getResources().getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("email", BOAPI.userInfo.getEmail());
                params.put("vehicle_id", edited.getVehicleId());
                params.put("vehicle_type_id", String.valueOf(Vehicle.getTypeForResource(edited.getResource())));
                params.put("car_model", edited.getCarModel());
                params.put("state", edited.getPlateState());
                params.put("plate_number", edited.getPlateNumber());

                return params;
            }
        };
        Net.singleton.addRequest(AddVehicle.this, req);
    }//edit vehicle

    class AddVehicleResponse
    {
        String status;
        String id;
        public AddVehicleResponse(String raw)
        {
            try
            {
                JSONObject root = new JSONObject(raw);
                status = root.getString("status");
                id = root.getString("vehicle_id");
            }
            catch(Exception e)
            {
                status = "parse error";
            }
        }//Constructor
    }//AddVehicleResponse
}//AddVehicle

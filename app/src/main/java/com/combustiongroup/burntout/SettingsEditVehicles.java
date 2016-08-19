package com.combustiongroup.burntout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.Vehicle;

import java.util.HashMap;
import java.util.Map;

import static com.combustiongroup.burntout.network.BOAPI.userVehicles;

public class SettingsEditVehicles extends AppCompatActivity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        lv = (ListView) findViewById(R.id.list);

        assert lv != null;

        lv.setAdapter(new ArrayAdapter<Vehicle>(
                SettingsEditVehicles.this,
                R.layout.el_list_item,
                BOAPI.userVehicles
        )
        {

            @Override
            public View getView(int position, View v, ViewGroup parent)
            {
                final int n = position;
                if(v == null)
                {
                    v = getLayoutInflater().inflate(R.layout.el_vehicle_vertical, parent, false);
                }

                ImageView image = (ImageView) v.findViewById(R.id.vehicle);
                if(image != null)
                {
                    image.setImageResource(userVehicles.get(position).getResource());
                }

                ImageView delete = (ImageView) v.findViewById(R.id.edit);
                if(delete != null)
                {
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            deleteVehicle(n);
                        }
                    });
                }

                TextView model = (TextView) v.findViewById(R.id.model);
                if(model != null)
                {
                    model.setText(userVehicles.get(position).getCarModel() );
                }

                TextView plate = (TextView) v.findViewById(R.id.plate);
                if(plate != null)
                {
                    plate.setText(userVehicles.get(position).getPlateNumber() );
                }

                return v;
            }//get view
        });
    }//on create

    void deleteVehicle(final int position)
    {

        StringRequest req = new StringRequest(Request.Method.POST, Net.Urls.DeleteVehicle.value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        userVehicles.remove(position);
                        Main.dataSetModified = true;
                        ((ArrayAdapter<Vehicle>) lv.getAdapter()).notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText(SettingsEditVehicles.this, getString(R.string.error_removing_vehicle), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("email", BOAPI.userInfo.getEmail());
                params.put("plate_number", BOAPI.userVehicles.get(position).getPlateNumber());

                return params;
            }
        };
        Net.singleton.addRequest(SettingsEditVehicles.this, req);
    }//delete vehicle
}//SettingsEditVehicle

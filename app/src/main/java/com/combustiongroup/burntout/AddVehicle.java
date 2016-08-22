package com.combustiongroup.burntout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.combustiongroup.burntout.network.BOAPI;
import com.combustiongroup.burntout.network.dto.Vehicle;
import com.combustiongroup.burntout.network.dto.response.StatusResponse;
import com.combustiongroup.burntout.ui.AddVehicleAdapter;
import com.combustiongroup.burntout.ui.ValuePickerList;
import com.combustiongroup.burntout.util.SpinnerAlert;

import retrofit2.Call;
import retrofit2.Callback;

public class AddVehicle extends AppCompatActivity {

    private static final String TAG = "AddVehicleActivity";
    final int IntentState = 0;

    ViewPager vehicles;

    int selected = 0;
    TextView state;
    EditText plate;
    EditText model;

    boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);


        editMode = getIntent().getBooleanExtra("editMode", false);

        vehicles = (ViewPager) findViewById(R.id.vehicle_pager);
        state = (TextView) findViewById(R.id.state);
        final View submit = findViewById(R.id.submit);
        plate = (EditText) findViewById(R.id.plate_number);
        model = (EditText) findViewById(R.id.vehicle_make);

        assert vehicles != null && state != null && submit != null;

        vehicles.setAdapter(new AddVehicleAdapter(AddVehicle.this));//pager adapter

        vehicles.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                selected = position;
                Log.w("#app", "scrolled to: " + position);
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

                if (preSubmit()) {
                    if (editMode) {
                        editVehicle();
                    } else {
                        registerVehicle();
                    }
                }
            }
        });

        if (editMode) {
            ((Button) submit).setText(R.string.save_changes);
            Vehicle v = (Vehicle) getIntent().getSerializableExtra("vehicle");
            plate.setText(v.getPlate_number());
            model.setText(v.getCar_model());
            state.setText(v.getPlate_state());
            vehicles.setCurrentItem(Integer.parseInt(v.getVehicle_type_id()));
        }
    }//on create

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == IntentState && resultCode == Activity.RESULT_OK) {
            state.setText(data.getStringExtra("choice"));
        }
    }//on activity result

    boolean preSubmit() {

        if (plate.getText().length() < 1) {
            Toast.makeText(AddVehicle.this, getResources().getString(R.string.license_empty), Toast.LENGTH_LONG).show();
            return false;
        } else if (model.getText().length() < 1) {
            Toast.makeText(AddVehicle.this, getResources().getString(R.string.car_model_empty), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }//presubmit

    void registerVehicle() {
        SpinnerAlert.show(AddVehicle.this);

        BOAPI.service.registerVehicle(
                BOAPI.gUserInfo.getEmail(),
                Integer.toString(selected),
                model.getText().toString(),
                state.getText().toString(),
                plate.getText().toString())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, retrofit2.Response<StatusResponse> response) {
                        Log.w(TAG, "Raw Response for Register Vehicle");
                        if (response.body().getStatus().equals("one")) {
                            Intent rdata = new Intent();
                            rdata.putExtra("plate_number", plate.getText().toString());
                            rdata.putExtra("model", model.getText().toString());
                            rdata.putExtra("type_id", Integer.toString(selected));
                            rdata.putExtra("state", state.getText().toString());

                            setResult(Activity.RESULT_OK, rdata);
                            finish();

                        } else if (response.body().getStatus().equals("two")) {
                            Toast.makeText(AddVehicle.this, getResources().getString(R.string.license_already_register), Toast.LENGTH_LONG).show();
                        }
                        SpinnerAlert.dismiss(AddVehicle.this);
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        Toast.makeText(AddVehicle.this, getResources().getString(R.string.error_request), Toast.LENGTH_LONG).show();
                        SpinnerAlert.dismiss(AddVehicle.this);
                    }
                });

    }//register vehicle

    void editVehicle() {
        //construct new vehicle to submit for editing & pass back to update display
        final Vehicle edited = new Vehicle(
                plate.getText().toString(),
                model.getText().toString(),
                Integer.toString(selected),
                state.getText().toString()
        );
        SpinnerAlert.show(AddVehicle.this);

        BOAPI.service.editVehicle(
                BOAPI.gUserInfo.getEmail(),
                edited.getVehicle_id(),
                Integer.toString(selected),
                edited.getCar_model(),
                edited.getPlate_state(),
                edited.getPlate_number()).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, retrofit2.Response<StatusResponse> response) {
                //response is "one" in all cases, so it worked, maybe!
                Intent rdata = new Intent();
                rdata.putExtra("vehicle", edited);
                rdata.putExtra("forItem", getIntent().getIntExtra("forItem", 0));

                setResult(Activity.RESULT_OK, rdata);
                SpinnerAlert.dismiss(AddVehicle.this);

                finish();
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                t.printStackTrace();
                SpinnerAlert.dismiss(AddVehicle.this);
                Toast.makeText(AddVehicle.this, getResources().getString(R.string.error_message_while_saving), Toast.LENGTH_LONG).show();

            }
        });

    }//edit vehicle


}//AddVehicle

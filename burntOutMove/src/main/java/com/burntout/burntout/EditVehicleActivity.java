package com.burntout.burntout;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class EditVehicleActivity extends Activity implements Post.Communicator, VehicleTypeHSV.Communicator {
	
	public ProgressDialog pm;
	public Post editVehicle;
	
	public SharedPreferences sharedPref;
	public Bundle bundle;
	public Vehicle vehicle;
	
	public ArrayList<Vehicle> userVehicles;
	public PageMarkers pageMarkers;
	
	public String vehicleType, makeModel, plateState, plateNumber, vehicleId, newVehicleType, newMakeModel, newPlateState, newPlateNumber, email;
	public int thisVehicleKey;
	
	public EditText makeModelInput, plateNumInput;
	public StatePicker statePicker;
	public VehicleTypeHSV vehicleTypeHSV;
	@SuppressWarnings("rawtypes")
	public ArrayAdapter stateAdapter;
	
	public boolean wasEdited = false;
	public boolean profEdited = false;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_vehicle);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E5791")));
  		bar.setTitle("Burnt Out");
  		bar.setSubtitle("Edit Vehicle");
		bar.setDisplayHomeAsUpEnabled(true);
		
		sharedPref = this.getSharedPreferences(
				  getString(R.string.pref), Context.MODE_PRIVATE);
		bundle = getIntent().getExtras();
		
		vehicle = (Vehicle)getIntent().getSerializableExtra("vehicle");
		thisVehicleKey = getIntent().getIntExtra("position", -1);
		
		int profEdit = getIntent().getIntExtra("profEdited", -1);
		if(profEdit == 1) {
			profEdited = true;
		}
				
		
		email = sharedPref.getString("email", null);
		
		
		
		
		
		makeModelInput = (EditText)findViewById(R.id.editvehicle_makemodel);
		plateNumInput = (EditText)findViewById(R.id.editvehicle_platenumber);
		statePicker = (StatePicker)findViewById(R.id.editvehicle_statePicker);
		vehicleTypeHSV = (VehicleTypeHSV)findViewById(R.id.editVehicleTypeHSV);
		
		pageMarkers = (PageMarkers)findViewById(R.id.pageMarkersEdit);
		pageMarkers.setTotalPages(4);
		pageMarkers.makeView(0);
		
		vehicleTypeHSV.setComm(this);
		vehicleTypeHSV.initItems(this);
        vehicleTypeHSV.setFeatureItems();
        vehicleTypeHSV.addManagers(this);
		
		
		
		vehicleId = vehicle.getVehicleId();
		vehicleType = vehicle.getVehicleTypeId();
		makeModel = vehicle.getCarModel();
		plateState = vehicle.getPlateState();
		plateNumber = vehicle.getPlateNumber();
		
		//make autopicker show vehicle type
		@SuppressWarnings("unused")
		int vType = Integer.parseInt(vehicleType);
		
		
		
		makeModelInput.setText(makeModel);
		plateNumInput.setText(plateNumber);
		statePicker.setPrompt(plateState);
		
		stateAdapter = (ArrayAdapter)statePicker.getAdapter();
		int spinnerPosition = stateAdapter.getPosition(plateState);
		statePicker.setSelection(spinnerPosition);
		
		
		
		
		
		
		
		
	}

	

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
     
            case android.R.id.home:
            leaveme();
            return true;
      
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	public void leaveme() {
		if(!wasEdited) {
			setResult(RESULT_CANCELED);
		}
		finish();
	}
	
	public void submitClicked(View v) {
		
		pm = new ProgressDialog(this, R.style.MyTheme);
		pm.show();
		
		editVehicle = new Post();
		editVehicle.setCommunicator(this);
		
		int vType = vehicleTypeHSV.getActiveFeature();
		
		newVehicleType = Integer.toString(vType);
		newMakeModel = makeModelInput.getText().toString();
		newPlateNumber = plateNumInput.getText().toString();
		newPlateState = statePicker.getSelectedItem().toString();
		
		StringCheck stringCheck = new StringCheck();
		
		
		
		if(checkInputs(newPlateNumber, newMakeModel)) {
		
			if(stringCheck.checkSpecialCharsMakeModel(newMakeModel) && stringCheck.checkSpecialCharsPlateNum(newPlateNumber)) {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
				nameValuePairs.add(new BasicNameValuePair("email", email));
				nameValuePairs.add(new BasicNameValuePair("vehicle_id", vehicleId));
		        nameValuePairs.add(new BasicNameValuePair("vehicle_type_id", newVehicleType));
		        nameValuePairs.add(new BasicNameValuePair("car_model", newMakeModel));
		        nameValuePairs.add(new BasicNameValuePair("state", newPlateState));
		        nameValuePairs.add(new BasicNameValuePair("plate_number", newPlateNumber));
		        
		        
		        editVehicle.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/editVehicle.php");
			}
			else {
				Toast.makeText(this, "Special Characters Not Allowed", Toast.LENGTH_LONG).show();
			}
		}
		
		pm.dismiss();
		pm = null;
		
	}

	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		try {
			if(s.getString("status").equals("one")) {
				
				wasEdited = true;
				vehicle.setVehicleTypeId(newVehicleType);
				vehicle.setCarModel(newMakeModel);
				vehicle.setPlateNumber(newPlateNumber);
				vehicle.setPlateState(newPlateState);
				
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				intent.putExtra("vehicle", vehicle);
				intent.putExtra("position", thisVehicleKey);
				
				Toast.makeText(this, "Vehicle Updated", Toast.LENGTH_LONG).show();
				if(profEdited) {
					intent.putExtra("profEdited", 1);
				}
				else {
					intent.putExtra("profEdited", 0);
				}
				
				finish();
			}
			if(s.getString("status").equals("two")) {
				Toast.makeText(this, "Error Updating Vehicle", Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean checkInputs(String newPlateNumber, String newMakeModel) {
		
		if(newPlateNumber.length() < 1) {
			Toast.makeText(this, "Enter Plate Number", Toast.LENGTH_LONG).show();
			return false;
		}
		if(newPlateNumber.length() > 15) {
			Toast.makeText(this, "Plate Number Too Long", Toast.LENGTH_LONG).show();
			return false;
		}
		if(newMakeModel.length() < 1) {
			Toast.makeText(this, "Enter Make/Model", Toast.LENGTH_LONG).show();
			return false;
		}
		if(newMakeModel.length() > 50) {
			Toast.makeText(this, "Make/Model Too Long", Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
	}



	@Override
	public void setPageMarkers(int currentPage) {
		// TODO Auto-generated method stub
		pageMarkers.makeView(currentPage);
		
	}
}

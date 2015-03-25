package com.burntout.burntout;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.burntout.burntout.StatePicker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;



public class AddCarActivity extends Activity implements Post.Communicator, VehicleTypeHSV.Communicator {
	
	
	public ArrayList<EditText> textInputs;
	public ProgressDialog pm;
	public Post addVehicle;
	
	public StatePicker statePicker;
	public VehicleTypeHSV vehicleTypeHSV;
	
	public VehicleViewer vehicleAdapter;
	public PageMarkers pageMarkers;
		
	
	public Button submitBtn;
	public EditText makeModelEntry, plateNumberEntry;
	
	public SharedPreferences sharedPref;
	public String email, vType, makeModel, vState, vPlateNumber;
	
	
	@SuppressWarnings("unused")
	private String userID = "2";
	public int currentVehicle;
		
	public LayoutInflater inflater;
	public RelativeLayout autopickerContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_car_activity);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E5791")));
  		bar.setTitle("Burnt Out");
  		bar.setSubtitle("Add Vehicle");
		bar.setDisplayHomeAsUpEnabled(true);
		
		//adapter = new AutopickerArrayAdapter(this, R.layout.add_car_activity, autopickerValues);
		
		makeModelEntry = (EditText)findViewById(R.id.make_model);
		plateNumberEntry = (EditText)findViewById(R.id.plate_number);
		pageMarkers = (PageMarkers)findViewById(R.id.pageMarkersAdd);
		pageMarkers.setTotalPages(4);
		pageMarkers.makeView(0);
		
		Context context = this;
		
		SharedPreferences sharedPref = context.getSharedPreferences(
				  getString(R.string.pref), Context.MODE_PRIVATE);

		email = sharedPref.getString("email", null);
		
		
		vehicleTypeHSV = (VehicleTypeHSV)findViewById(R.id.addVehicleTypeHSV);
		vehicleTypeHSV.setComm(this);
		vehicleTypeHSV.initItems(this);
        vehicleTypeHSV.setFeatureItems();
        vehicleTypeHSV.addManagers(this);
		
		statePicker = (StatePicker)findViewById(R.id.statepicker_view2);
		
		submitBtn = (Button)findViewById(R.id.submit_button_addcar);
		
		
		
		
		addListenerOnSubmit();
		
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
	
	
	public void addListenerOnSubmit() {
		
		
		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				int vTypeInt = vehicleTypeHSV.getActiveFeature();
				vType = Integer.toString(vTypeInt);
				Log.d("vtype", vType);
				makeModel = makeModelEntry.getText().toString().trim();
				vState = statePicker.getSelectedItem().toString();
				vPlateNumber = plateNumberEntry.getText().toString().trim();
				
				if(makeModel.length() < 1)
				{
					Toast.makeText(AddCarActivity.this, "Car Model can't be empty!",Toast.LENGTH_LONG).show();
				}
				else if(vPlateNumber.length() < 1)
				{
					Toast.makeText(AddCarActivity.this, "License Plate can't be empty!",Toast.LENGTH_LONG).show();
				}
				else
				{
					loginBurnt(vType, makeModel, vState, vPlateNumber);	
				}
				
					
				
			}
			
		});
	}
	
	
	
	public void loginBurnt(String vType, String makeModel, String vState, String vPlateNumber) {
		
		
	
		
		
		pm = new ProgressDialog(this, R.style.MyTheme);
		pm.show();

		addVehicle = new Post();
		addVehicle.setCommunicator(this);
		
		StringCheck stringCheck = new StringCheck();
		makeModel = stringCheck.cleanseSpecialChars(makeModel);
		vPlateNumber = stringCheck.cleanseSpecialChars(vPlateNumber);
		
		if(checkInputs(makeModel, vPlateNumber)) {
		
			 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			 
			 nameValuePairs.add(new BasicNameValuePair("email", email));
	         nameValuePairs.add(new BasicNameValuePair("vehicle_type", vType));
	         nameValuePairs.add(new BasicNameValuePair("car_model", makeModel));
	         nameValuePairs.add(new BasicNameValuePair("plate_state", vState));
	         nameValuePairs.add(new BasicNameValuePair("plate_number", vPlateNumber));
	         
	         
	         addVehicle.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/addvehicle.php");
		}
		else {
			pm.dismiss();
			pm = null;
		}
		
	}
	
	
	public void leaveme() {
		
		setResult(RESULT_CANCELED);	
		
		finish();
	}


	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		Log.d("json",s.toString());
		String status;
		try {
			status = s.getString("status");
			Log.d("status", status);
			if(status.equals("one"))
			{
				
				
				Toast.makeText(this, "Vehicle Added", Toast.LENGTH_LONG).show();
								
				Intent returnIntent = new Intent();
								
				String vehicleId = s.getString("vehicle_id");
				
				Vehicle vehicle = new Vehicle(vehicleId, vType, makeModel, vPlateNumber, vState);
				
				returnIntent.putExtra("addedVehicle", vehicle);
				
				setResult(RESULT_OK, returnIntent);
				
				finish();
				
				 
				
			}
			else
			{
			Toast.makeText(this, "License Plate Taken" , Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			pm.dismiss();
			pm = null;
		
	}
	
	public void setVType(int vtype) {
		
		currentVehicle = vtype;
	}



	@Override
	public void setPageMarkers(int currentPage) {
		// TODO Auto-generated method stub
		pageMarkers.makeView(currentPage);
		
	}

	public boolean checkInputs(String plateNumber, String makeModel) {
			
			if(plateNumber.length() < 1) {
				Toast.makeText(this, "Enter Plate Number", Toast.LENGTH_LONG).show();
				return false;
			}
			if(plateNumber.length() > 25) {
				Toast.makeText(this, "Make and Model Too Long", Toast.LENGTH_LONG).show();
				return false;
			}
			if(makeModel.length() < 1) {
				Toast.makeText(this, "Enter Make/Model", Toast.LENGTH_LONG).show();
				return false;
			}
			if(makeModel.length() > 50) {
				Toast.makeText(this, "Make/Model Too Long", Toast.LENGTH_LONG).show();
				return false;
			}
			
			return true;
		}
	
	

}

package com.burntout.burntout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class DebugActivity extends Activity implements Post.Communicator {
	
	private ProgressDialog pm;
	@SuppressWarnings("unused")
	private Post login;
	private String email, password;
	public List<Vehicle> vehicleList;
	public Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);
		email = "genprotrans@yahoo.com";
		vehicleList = new ArrayList<Vehicle>();
		
		@SuppressWarnings("unused")
		Bundle bundle = getIntent().getExtras();
		
		
		SharedPreferences sharedPref = ((Activity) this).getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("user_id", "2");
		editor.putString("email", email);
		editor.commit();
		
		Intent i = new Intent(this, DemoActivity.class);
		this.startActivity(i);
		
		
		/*
		pm = new ProgressDialog(this);
		pm.show();

		login = new Post();
		login.setCommunicator(this);
		
		email = "genprotrans@yahoo.com";
		password = "numnuts";
		

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("email", email));
        
        login.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/profile.php");
		*/
		
		
		
		
		
	}

	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		Log.d("json",s.toString());
		String status;
		try {
			status = s.getString("status");
			if(status.equals("one"))
			{
				SharedPreferences sharedPref = ((Activity) this).getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString("fname", s.getString("fname"));
				String em = s.getString("email");
				
				int userIDint = s.getInt("user_id");
				String userID = String.valueOf(userIDint);
				
				editor.putString("email", em);
				
				editor.putString("user_id", userID);
				editor.commit();
				
				
				
				
				 Toast.makeText(this, em, Toast.LENGTH_LONG).show();
				Intent i = new Intent(this,ProfileActivity.class);
				 
				 i.putExtra("fname", s.getString("fname"));
				 i.putExtra("lname", s.getString("lname"));
				 i.putExtra("email", s.getString("email"));
				 i.putExtra("picture", s.getString("picture"));
				
				 i.putExtra("password",password);
				this.startActivity(i);
			}
			else
			{
			Toast.makeText(this, "Username/Password Incorrect" , Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			pm.dismiss();
			pm = null;
	}
	
	public void createVehiclesArray(JSONObject cars) throws JSONException {
		
		//get total # vehicles
		int totalVehicles = cars.length();
		
		//create array to hold vehicle objects
	//	ArrayList<Vehicle> carsArray = new ArrayList<Vehicle>();
		
		//create a vehicle object for each vehicle
		for(int i=0; i<totalVehicles; i++) {
			
			JSONObject thisCar = cars.getJSONObject(Integer.toString(i));
			String vehicle_id = thisCar.getString("vehicle_id");
			String vehicle_type_id = thisCar.getString("vehicle_type_id");
			String car_model = thisCar.getString("car_model");
			String plate_number = thisCar.getString("plate_number");
			String plate_state = thisCar.getString("plate_state");
			String created = thisCar.getString("created");
			
			Vehicle userCar = new Vehicle(vehicle_id, vehicle_type_id, car_model, plate_number, plate_state, created);
			vehicleList.add(userCar);
			
		}
		
		bundle.putSerializable("Vehicles", (Serializable)vehicleList);
		
	}

	
}

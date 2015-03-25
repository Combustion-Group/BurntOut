package com.burntout.burntout;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class VehicleEditor implements Post.Communicator {
	
	Vehicle vehicle;
	ProgressDialog pm;
	Post deleteVehicle;
	Context c;
	Communicator comm;
	String email;
	int pos;
	boolean wasDeleted = false;
	
	public VehicleEditor(Vehicle v, Context context, int position, String email) {
		
		this.vehicle = v;
		this.c = context;
		this.pos = position;
		this.email = email;
	}
	
	
	public void setCommunicator(Communicator c) {
		comm = c;
	}
	
	
	public void editThisVehicle() {
		
	}
	
	public void deleteThisVehicle() {
		
		String plate_number = this.vehicle.getPlateNumber();
		pm = new ProgressDialog(this.c, R.style.MyTheme);
		pm.show();
		deleteVehicle = new Post();
		deleteVehicle.setCommunicator(this);
		
		Log.d("email", email);
		Log.d("plate_number", plate_number);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("plate_number", plate_number));
		
		deleteVehicle.executePosts(nameValuePairs, "http://combustioninnovation.com/production/Goodyear/php/deletecar.php");
		
	}
	
	
	

	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		Log.d("response", s.toString());
		try {
			
			if(s.getString("status").equals("one")) {
				wasDeleted = true;
				pm.dismiss();
				Toast.makeText(this.c, "Vehicle Deleted", Toast.LENGTH_LONG).show();
				comm.vehicleDeleted(pos);
				
			}
			else {
				pm.dismiss();
				Toast.makeText(this.c, "Error Deleting Vehicle", Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean getResult() {
		return wasDeleted;
	}
	
	public interface Communicator {
		
		public void vehicleDeleted(int position);
	}


}

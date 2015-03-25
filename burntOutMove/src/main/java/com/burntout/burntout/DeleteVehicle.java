package com.burntout.burntout;

import java.util.ArrayList;




import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteVehicle extends Activity implements Post.Communicator, DeleteVehiclesArrayAdapter.Communicator {

	public ProgressDialog pm;
	public Post deleteVehicle;
	public Bundle bundle;
	
	public RelativeLayout deleteableItem;
	public ImageView vehiclePic;
	public TextView carInfo;
	public Button deleteBtn;
	
	public LayoutInflater inflater;
	
	public Vehicle vehicle;
	public ArrayList<Vehicle> myVehicles;
	
	public ListView deleteItemContainer;
	public int deletedIndex;
	
	public String email;
	
	public boolean arrayChanged = false;
	
	DeleteVehiclesArrayAdapter arrayAdapter;
	
	
	
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_vehicle);
		
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E5791")));
  		bar.setTitle("Burnt Out");
  		bar.setSubtitle("Delete Vehicle");
		bar.setDisplayHomeAsUpEnabled(true);
		
		@SuppressWarnings("unused")
		Bundle bundle = getIntent().getExtras();
		
		SharedPreferences sharedPref = this.getSharedPreferences(
				  getString(R.string.pref), Context.MODE_PRIVATE);
		email = sharedPref.getString("email", null);
		
		SerializedObject serializedObject = (SerializedObject)getIntent().getSerializableExtra("vehicles");
		myVehicles = serializedObject.getMyVehicles();
		
		
		
		
		
		deleteItemContainer = (ListView)findViewById(R.id.deletevehicles_listview);		
		
		
		arrayAdapter = new DeleteVehiclesArrayAdapter(this, R.layout.delete_vehicle, myVehicles);
		arrayAdapter.setCommunicator(this);
		deleteItemContainer.setAdapter(arrayAdapter);
		
		
		
		//makeEntries();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_vehicle, menu);
		return true;
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
	public void deleteThisVehicle(String plate_number, int pos) {
		
		//after gotResponse(), delete vehicle from array in memory
		@SuppressWarnings("unused")
		Vehicle deletedVehicle = arrayAdapter.getItem(pos);
	//	arrayAdapter.remove(deletedVehicle);
		myVehicles.remove(pos);		
		
		arrayAdapter.notifyDataSetChanged();
		
		deleteFromDb(plate_number, email);
		arrayChanged = true;
	
		
		
		Toast.makeText(this, "Vehicle Deleted", Toast.LENGTH_LONG).show();
		if(!arrayChanged) {
			arrayChanged = true;
		}
		
	}
	
	
	public void deleteFromDb(String plate_number, String email) {
		
		pm = new ProgressDialog(this, R.style.MyTheme);
		pm.show();
		
		deleteVehicle = new Post();
		deleteVehicle.setCommunicator(this);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("plate_number", plate_number));
		
		deleteVehicle.executePosts(nameValuePairs, "http://combustioninnovation.com/production/Goodyear/php/deletecar.php");
		
		
		
	}

	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		pm.dismiss();
		pm = null;
		
		
	}
	
	public void leaveme() {
		
		Intent returnIntent = new Intent();
		if(arrayChanged) {
			setResult(RESULT_OK, returnIntent);
			
			SerializedObject serializedObject = new SerializedObject(myVehicles);
			returnIntent.putExtra("vehicles", serializedObject);
			
			
		}
		else {
			setResult(RESULT_CANCELED, returnIntent);
		}
		
		
		finish();
	}
	
	
	@Override
	public void onBackPressed() {
		leaveme();
	}
	
	
	/*


	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		try {
			if(s.getString("status").equals("one")) {
				
				myVehicles.remove(deletedIndex);
				
				
				Toast.makeText(this, "Vehicle Deleted", Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	*/
}

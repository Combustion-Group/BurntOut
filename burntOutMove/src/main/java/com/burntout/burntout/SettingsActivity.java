package com.burntout.burntout;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity implements Post.Communicator {
	Post login;
	String email;
	ArrayList<Vehicle> myVehicles;
	UserData userData;
	Bundle bundle;
	Switch pns;
	int pn;
	boolean pnsIsChecked;
	boolean vehiclesEdited = false;
	boolean profileEdited = false;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	int logintype;
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_view);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E5791")));
  		bar.setTitle("Burnt Out");
  		bar.setSubtitle("Settings");
		bar.setDisplayHomeAsUpEnabled(true);
		
		Bundle bundle = getIntent().getExtras();
		SerializedObject serializedObject = (SerializedObject)getIntent().getSerializableExtra("vehicles");
		myVehicles = serializedObject.getMyVehicles();

		fixLayout();
		
		
		Context context =this; 
		SharedPreferences sharedPref = context.getSharedPreferences(
		  getString(R.string.pref), Context.MODE_PRIVATE);
		
		editor = sharedPref.edit();
		
		email = sharedPref.getString("email", null);
   
		pn = sharedPref.getInt("push_notifications", 0);
		int fn = sharedPref.getInt("facebook_notifications", 0);

		
		 //Switch fns = (Switch)findViewById(R.id.facebooknotificationsswitch);
		 boolean b = (fn != 0);
	     //fns.setChecked(b);
	    	
	    	pns = (Switch)findViewById(R.id.pushnotificationsswitch);
			boolean pnb = (pn != 0);
		    pns.setChecked(pnb);
		    pns.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					login = new Post();
					login.setCommunicator(SettingsActivity.this);
					
					
				
					 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					 nameValuePairs.add(new BasicNameValuePair("email", email));
					 if(isChecked) {
						 
						 nameValuePairs.add(new BasicNameValuePair("push_notifications", "1"));
						 editor.putInt("push_notifications", 1);
						 
					 }
					 else {
						 nameValuePairs.add(new BasicNameValuePair("push_notifications", "0"));
						 editor.putInt("push_notifications", 0);
					 }
					 editor.commit();
				     login.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/editpreferences.php");
					
				}
			});
	    	
	    	
	}
	
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
	//makes all the layoutss (buttons and labels have 95 percent width to look good on all screens
	public void fixLayout()
	{
		 int wid =   (int) (this.getResources().getDisplayMetrics().widthPixels * .92);
		int[]	inputs = {R.id.editvehicle,R.id.privacyb,R.id.termsb,R.id.contactb,R.id.logoutb,R.id.editinfobutton};
		int[]	v = {R.id.pnholder};//R.id.fnholder};
		int[]	labels = {R.id.labelone,R.id.labeltwo,R.id.labelthree,R.id.l3};
		for(int i=0;i<inputs.length;i++)
		{
			Button b =  (Button)findViewById(inputs[i]);
			b.getLayoutParams().width = wid;
		}
		for(int i=0;i<v.length;i++)
		{
			RelativeLayout b =  (RelativeLayout)findViewById(v[i]);
			b.getLayoutParams().width = wid;
		}
		
		for(int i=0;i<labels.length;i++)
		{
			TextView b =  (TextView)findViewById(labels[i]);
			b.getLayoutParams().width = wid;
		}
		
		
		
	}	

	
	//x button click
	public void goAway(View v)
	{	
		Intent returnIntent = new Intent();
		if(!vehiclesEdited && !profileEdited) {
			setResult(RESULT_CANCELED, returnIntent);
			finish();
		}
		else {
			setResult(RESULT_OK, returnIntent);
			SerializedObject serializedObject = new SerializedObject(myVehicles);
			returnIntent.putExtra("vehicles", serializedObject);
			finish();
			
		}
		
	}
	
	
	public void leaveme()
	{
		Intent returnIntent = new Intent();
        if(vehiclesEdited || profileEdited) {
        
            setResult(RESULT_OK, returnIntent);
            
            returnIntent.putExtra("logout","no");
            if(vehiclesEdited) {
            	SerializedObject serializedObject = new SerializedObject(myVehicles);
                returnIntent.putExtra("vehiclesEdited", "1");
                returnIntent.putExtra("vehicles", serializedObject);
            }
            else {
            	returnIntent.putExtra("vehiclesEdited", "0");
            }
            if(profileEdited) {
            	returnIntent.putExtra("profileEdited", 1);
            }
            else {
            	returnIntent.putExtra("profileEdited", 0);
            }
            
        }
        else {
            
           
            setResult(RESULT_CANCELED, returnIntent);

            
        }
        finish();
		
	}
	
// logs user out of of app if they accept 
	public void logOut(View v)
	{
		  new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Burnt Out")
	        .setMessage("Are you sure you want to Logout?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	/*
	        	SharedPreferences.Editor editor = sharedPref.edit();
	        	editor.clear();
	        	editor.commit();
	        	*/
	        	Intent returnIntent = new Intent();
				returnIntent.putExtra("logout","yes");
				 if(vehiclesEdited) {
		            	SerializedObject serializedObject = new SerializedObject(myVehicles);
		                returnIntent.putExtra("vehiclesEdited", "1");
		                returnIntent.putExtra("vehicles", serializedObject);
		            }
		            else {
		            	returnIntent.putExtra("vehiclesEdited", "0");
		            }
		            if(profileEdited) {
		            	returnIntent.putExtra("profileEdited", 1);
		            }
		            else {
		            	returnIntent.putExtra("profileEdited", 0);
		            }
				setResult(RESULT_OK,returnIntent);
				finish();
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();
		
		
	}

	
// switch on change function to update the statuses for push notifications and facebook notifications	
	/*
	public OnClickListener changeSwitchStatus()
	{
		login = new Post();
		login.setCommunicator(this);
		Log.d("isChecked", "true");
	
		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		 nameValuePairs.add(new BasicNameValuePair("email", email));
		 if(pns.isChecked()) {
			 
			 nameValuePairs.add(new BasicNameValuePair("push_notifications", "1"));
		 }
		 else {
			 nameValuePairs.add(new BasicNameValuePair("push_notifications", "0"));
		 }
	     login.executePosts(nameValuePairs,"http://www.combustioninnovation.com/production/Goodyear/php/editpreferences.php");
		return null;
		
	}
	*/

	@SuppressWarnings("unused")
	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		
		try {
			if(s.getString("status").equals("one")) {
				Log.d("email", email);
				GcmRegistration gcmRegistration = new GcmRegistration(email, this);
				
					
				Log.d("edited", "true");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	//edit information button click
	public void goToEditProfile(View v)
	{
		
		Intent i = new Intent(this,EditProfileActivity.class);
		if(vehiclesEdited) {
			i.putExtra("vehiclesEdited", 1);
		}
		else {
			i.putExtra("vehiclesEdited", 0);
		}
		
		this.startActivityForResult(i, 2);
	}
	
	public void privacyWasPressed(View v)
    {
        Uri uri = Uri.parse("http://www.burntoutapp.com/privacy");
         Intent intent = new Intent(Intent.ACTION_VIEW, uri);
         startActivity(intent);
         
    }
	
	public void termsWasPressed(View v)
    {
        Uri uri = Uri.parse("http://www.burntoutapp.com/terms");
         Intent intent = new Intent(Intent.ACTION_VIEW, uri);
         startActivity(intent);
         
    }
	
	public void emailCombustion(View v)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","contact@burntoutapp.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Dear BurntOut Team");
                    startActivity(Intent.createChooser(emailIntent, "Email BurntOut"));
    }
	
	public void gotoDelete(View v) {
		
		Intent intent = new Intent(this, DeleteVehicle.class);

		SerializedObject serializedObject = new SerializedObject(myVehicles);

		intent.putExtra("vehicles", serializedObject);
		
		if(profileEdited) {
			intent.putExtra("profEdited", 1);
		}
		else {
			intent.putExtra("profEdited", 0);
		}
 		
		startActivityForResult(intent, 1);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == 1) {
			if(resultCode == RESULT_OK) {
				
				SerializedObject serializedObject = (SerializedObject) data.getSerializableExtra("vehicles");
			//	myVehicles = new ArrayList<Vehicle>();
				myVehicles = serializedObject.getMyVehicles();
				vehiclesEdited = true;
				int profEdit = data.getIntExtra("profEdited", -1);
				if(profEdit == 1) {
					profileEdited = true;
				}
				
			}
			
		}
		
		if(requestCode == 2) {
			
			if(resultCode == RESULT_OK) {
				profileEdited = true;
				
				
			}
			int vehicleEdit = data.getIntExtra("vehicleEdit", -1);
			if(vehicleEdit == 1) {
				vehiclesEdited = true;
			}
			else {
				vehiclesEdited = false;
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		leaveme();
	}
	

}

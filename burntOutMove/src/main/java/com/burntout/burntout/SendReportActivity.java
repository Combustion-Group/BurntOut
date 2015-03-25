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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendReportActivity extends Activity implements Post.Communicator, OnItemSelectedListener, TappablesHSV.Communicator {
	
	public TextView platecheck, reportedLightsCheck, frontText, backText;
	public EditText reportPlateNumber, message;
	
	public Button submitBtn;
	public StatePicker statePicker;
	public String plateState;
	public String reportedBurnouts;
	
	
	
	TappablesHSV reportables;
	public PageMarkers pageMarkers;
	
	public ArrayList<RAPItem> rapItems;
	public RAPItem rapItem;
	
	
	
	public SharedPreferences sharedPref;
	
	public ProgressDialog pm;
	public Post reportVehicle;
	
	public OnClickListener listener;
	public Context context;
	
	
	private String email;
	
	public int numReports = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_report);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E5791")));
  		bar.setTitle("Burnt Out");
  		bar.setSubtitle("Report Burnout");
		bar.setDisplayHomeAsUpEnabled(true);
		
		
		context = this;
		SharedPreferences sharedPref = context.getSharedPreferences(
    	        getString(R.string.pref), Context.MODE_PRIVATE);
		
		email = sharedPref.getString("email", null);
		
		Log.d("email", email);
		
		
		frontText = (TextView)findViewById(R.id.frontText);
		backText = (TextView)findViewById(R.id.backText);
		reportedLightsCheck = (TextView)findViewById(R.id.reported_lights_text);
		reportPlateNumber = (EditText)findViewById(R.id.report_plate_number);
		message = (EditText)findViewById(R.id.extra_message);
		submitBtn = (Button)findViewById(R.id.report_button);
		pageMarkers = (PageMarkers)findViewById(R.id.pageMarkersReport);
		pageMarkers.setTotalPages(4);
		pageMarkers.makeView(0);
		
				
		statePicker = (StatePicker)findViewById(R.id.statepicker_view1);
		
		reportables = (TappablesHSV)findViewById(R.id.tappablesHSV1);
		reportables.setCommunicator(this);
		reportables.initItems(this);
        reportables.setFeatureItems();
        reportables.addManagers(this);
        
				
        reportPlateNumber.clearFocus();
        message.clearFocus();
        
        
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
				
				int vType = reportables.getActiveFeature();
				
				
				
				
				String vTypeString = Integer.toString(vType);
				String plateState = statePicker.getSelectedItem().toString();
				String plateNumber = reportPlateNumber.getText().toString();
				String lightsOut = reportables.getReported();
				
				StringCheck stringCheck = new StringCheck();
				
				String extraMsg = message.getText().toString();
				
				extraMsg = extraMsg.replace("\\n", " ");
				
				
				//email = "genprotrans@yahoo.com";
				
				if(plateNumber.length() < 1) {
					Toast.makeText(SendReportActivity.this, "Please Fill in a plate number!",Toast.LENGTH_LONG).show();
				}
				else if(lightsOut.length() < 1)	{
					Toast.makeText(SendReportActivity.this, "Please Complete Step 2!",Toast.LENGTH_LONG).show();
				}
				else {
					if(stringCheck.checkSpecialCharsPlateNum(plateNumber) && stringCheck.checkSpecialCharsMessage(extraMsg)) {
						loginBurnt(email, plateState, plateNumber, lightsOut, extraMsg, vTypeString);		
					}
					else {
						Toast.makeText(context, "Special Characters Not Allowed", Toast.LENGTH_LONG).show();
					}
				}
			}	
			
		});
		
	}
	
public void addReportAutoPicker() {
	
		
		//LinearLayout l = (LinearLayout)findViewById(R.id.reportAutoPicker1);
		//slider = new SlidingView_Cars(this);
  		//l.addView(slider);
				
	}
	

	public void loginBurnt(String email, String plateState, String plateNumber, String lightsOut, String extraMsg, String vTypeString) {
		
		pm = new ProgressDialog(this, R.style.MyTheme);
		pm.show();

		reportVehicle = new Post();
		reportVehicle.setCommunicator(this);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
		 
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("vehicle_type", vTypeString));
		nameValuePairs.add(new BasicNameValuePair("plate_state", plateState));
        nameValuePairs.add(new BasicNameValuePair("license_plate", plateNumber));
        nameValuePairs.add(new BasicNameValuePair("lights_out", lightsOut));
        nameValuePairs.add(new BasicNameValuePair("special_message", extraMsg));
                
        reportVehicle.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/sendmessage.php");
		
	}
	
	

	

	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		Log.d("status", s.toString());
		try {
			if(s.getString("status").equals("one")) {
				pm.dismiss();
				Toast.makeText(this, "Burnout Reported", Toast.LENGTH_LONG).show();
				numReports = 1;
				Intent returnIntent = new Intent();
				returnIntent.putExtra("increment", numReports);
				setResult(RESULT_OK, returnIntent);
				
				finish();
			}
			else if(s.getString("status").equals("two")){
				Toast.makeText(this, "Error Reporting Burnout", Toast.LENGTH_LONG).show();
			   pm.dismiss();
			}
			else {
				Toast.makeText(this, "Error:  Cannot report yourself", Toast.LENGTH_LONG).show();
				pm.dismiss();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unused")
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String plateState = parent.getItemAtPosition(position).toString();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void leaveme() {
		
		finish();
	}
	
	public void setReportsString(String reports) {
		
		reportedLightsCheck.setText(reports);
		
	}

	@Override
	public void setReports(String reported, String viewedReported) {
		// TODO Auto-generated method stub
		reportedLightsCheck.setText(viewedReported);
		reportedBurnouts = reported;
		
	}
	
	public void setPageMarkers(int currentPage) {
		pageMarkers.makeView(currentPage);
	}
	
	@SuppressWarnings("unused")
	public void setTextFields(int width) {
		
		int frameWidth = 0;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		
		int screenMidPt = metrics.widthPixels/2;
		int densityDpi = (int)(metrics.density );
		
		switch(width) {
		case 0:
			frameWidth = 150 * densityDpi;
			break;
		case 1:
			frameWidth = 120 * densityDpi;
			break;
		case 2:
			frameWidth = 110 * densityDpi;
			break;
		case 3:
			frameWidth = 120 * densityDpi;
			break;
		}
		frontText.setWidth(frameWidth);
		backText.setWidth(frameWidth);
	}
}

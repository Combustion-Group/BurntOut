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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends Activity implements Post.Communicator {
	
	public SharedPreferences sharedPref;
	public SharedPreferences.Editor editor;
	public String fname, lname, email, origEmail, fnameEdit, lnameEdit, emailEdit;
	public String isFB;
	public EditText fnameInput, lnameInput, emailInput;
	public Button changePwdBtn;
	
	public ProgressDialog pm;
	public Post editProfile;
	
	public boolean noEmail;
	
	public boolean vehiclesEdited = false;
	
	public int isEdited = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E5791")));
  		bar.setTitle("Burnt Out");
  		bar.setSubtitle("Edit Profile");
		bar.setDisplayHomeAsUpEnabled(true);
		
		fnameInput = (EditText)findViewById(R.id.editprof_fname);
		lnameInput = (EditText)findViewById(R.id.editprof_lname);
		emailInput = (EditText)findViewById(R.id.editprof_email);
		changePwdBtn = (Button)findViewById(R.id.editprof_changepwdbtn);
		
		sharedPref = this.getSharedPreferences(
				  getString(R.string.pref), Context.MODE_PRIVATE);
		
		fname = sharedPref.getString("fname", null);
		lname = sharedPref.getString("lname", null);
		email = sharedPref.getString("email", null);
		origEmail = sharedPref.getString("email", null);
		isFB = sharedPref.getString("isFB", "no");
		
		int vehicleEdit = getIntent().getIntExtra("vehicleEdit", -1);
		if(vehicleEdit == 1) {
			vehiclesEdited = true;
		}
		else {
			vehiclesEdited = false;
		}
		
		Log.d("email", email);
		emailInput.setText(email);
		
		if(isFB.equals("1")) {
			changePwdBtn.setVisibility(View.INVISIBLE);
			emailInput.setVisibility(View.GONE);
			noEmail = true;
			
		}
		else {
			emailInput.setText(email);
			noEmail = false;
		}
		
		fnameInput.setText(fname);
		lnameInput.setText(lname);
		
		
		
		
	}

	

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
     
            case android.R.id.home:
            Intent returnIntent = new Intent();
            if(isEdited == 1) {
            	setResult(RESULT_OK, returnIntent);
            }
            else {
            	setResult(RESULT_CANCELED, returnIntent);
            }
            if(vehiclesEdited) {
            	returnIntent.putExtra("vehicleEdit", 1);
            }
            else {
            	returnIntent.putExtra("vehicleEdit", 0);
            }
            Log.d("leaving", "leaving");
            finish();
            return true;
      
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	public void submitClick(View v) {
		
		pm = new ProgressDialog(this, R.style.MyTheme);
		pm.show();
		
		editProfile = new Post();
		editProfile.setCommunicator(this);
		
		fnameEdit = fnameInput.getText().toString();
		lnameEdit = lnameInput.getText().toString();
		if(noEmail) {
			emailEdit = origEmail;
			
		}
		else {
			emailEdit = emailInput.getText().toString();
			if(emailEdit == origEmail) {
				Toast.makeText(this, "Same Email Address", Toast.LENGTH_LONG).show();
				//return;
			}
		}
		
		Log.d("fname", fnameEdit);
		Log.d("lname", lnameEdit);
		Log.d("email", emailEdit);
		Log.d("origemail", origEmail);
		
		
		StringCheck stringCheck = new StringCheck();
		
		if(checkInputs(fnameEdit, lnameEdit, emailEdit)) {
			
			if(stringCheck.checkSpecialCharsName(fnameEdit) && stringCheck.checkSpecialCharsName(lnameEdit)) {
			
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
				nameValuePairs.add(new BasicNameValuePair("f_name", fnameEdit));
				nameValuePairs.add(new BasicNameValuePair("l_name", lnameEdit));
				nameValuePairs.add(new BasicNameValuePair("newEmail", emailEdit));
				nameValuePairs.add(new BasicNameValuePair("oldEmail", origEmail));
				
				editProfile.executePosts(nameValuePairs, "http://combustioninnovation.com/production/Goodyear/php/editUserInfo.php");
			}
			else {
				Toast.makeText(this, "Special Characters Not Allowed", Toast.LENGTH_LONG).show();
			}
			
		}
		
		pm.dismiss();
		pm = null;
		
		
		
		
	}
	
	public void changePwdClick(View v) {
		
		Intent i = new Intent(this, ChangePasswordActivity.class);
		startActivity(i);
	}
	
	public boolean checkInputs(String fname, String lname, String email) {
		
		if(fname.length() < 1) {
			Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_LONG).show();
			return false;
		}
		if(lname.length() < 1) {
			Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
			return false;
		}
		if(email.length() < 1 && !noEmail) {
			Toast.makeText(this, "Please Enter Valid Email", Toast.LENGTH_LONG).show();
			return false;
		}
		if(fname.length() > 30) {
			Toast.makeText(this, "First Name Too Long", Toast.LENGTH_LONG).show();
			return false;
		}
		if(lname.length() > 30) {
			Toast.makeText(this, "Last Name Too Long", Toast.LENGTH_LONG).show();
			return false;
		}
		
		
		return true;
	}



	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		
		try {
			if(s.getString("status").equals("two")) {
				Toast.makeText(this, "Error: Email Already Registered", Toast.LENGTH_LONG).show();				
			}
			if(s.getString("status").equals("one")) {
				Toast.makeText(this, "Profile Updated", Toast.LENGTH_LONG).show();
				isEdited = 1;
				editor = sharedPref.edit();
				Log.d("fnameEdit", fnameEdit);
				editor.putString("fname", fnameEdit);
				editor.putString("lname", lnameEdit);
				editor.putString("email", emailEdit);
				editor.putString("profEdited", "1");
				editor.commit();
				
			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		
		Intent returnIntent = new Intent();
        if(isEdited == 1) {
        	setResult(RESULT_OK, returnIntent);
        }
        else {
        	setResult(RESULT_CANCELED, returnIntent);
        }
        if(vehiclesEdited) {
        	returnIntent.putExtra("vehicleEdit", 1);
        }
        else {
        	returnIntent.putExtra("vehicleEdit", 0);
        }
        Log.d("leaving", "leaving");
        finish();
	}
}

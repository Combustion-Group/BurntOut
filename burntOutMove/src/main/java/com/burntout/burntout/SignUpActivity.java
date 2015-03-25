package com.burntout.burntout;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity  implements Post.Communicator {
	 Post login;
	 ProgressDialog pm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_screen);
//	/	bye();
		fixLayout();
	}

	public void fixLayout()
	{
		 int wid =   (int) (this.getResources().getDisplayMetrics().widthPixels * .92);
		int[]	inputs = {R.id.signupfname,R.id.signuplname,R.id.signupemail,R.id.signuppassword,R.id.signupcpassword};
		for(int i=0;i<inputs.length;i++)
		{
			
			EditText b =  (EditText)findViewById(inputs[i]);
			b.getLayoutParams().width = wid;
			
			
		}
		

		Button ba = (Button)findViewById(R.id.signupbutton);
		ba.getLayoutParams().width = (int) (wid*.95);
	}	
	
	
	public void goAway(View v)
	{
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}
	
	
	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}
	
	public void tryToLogin(View v)
	{
		String [] alerts = {"First Name ","Last Name","Email","Password","Confirm Password"};
		
		int[]	inputs = {R.id.signupfname,R.id.signuplname,R.id.signupemail,R.id.signuppassword,R.id.signupcpassword};
		for(int i=0;i<inputs.length;i++)
		{
			
			EditText b =  (EditText)findViewById(inputs[i]);
			if(b.getText().length() == 0)
			{
				Toast.makeText(this,alerts[i] + " Cannot Be Empty",Toast.LENGTH_LONG).show();
				break;
			}
			if(i == inputs.length -1)
			{
				EditText pass =  (EditText)findViewById(inputs[3]);
				String password = pass.getText().toString();
				EditText cpass =  (EditText)findViewById(inputs[4]);
				String cpassword = cpass.getText().toString();
				if(pass.length() < 5)
				{
					Toast.makeText(this,"Passwords Must Be 6 Characters",Toast.LENGTH_LONG).show();
				}
				else if(!password.equals(cpassword))
				{
					Toast.makeText(this,"Passwords Do Not Match",Toast.LENGTH_LONG).show();
				}
				else
				{
					
					ArrayList<String>vals  = new ArrayList<String>(inputs.length);
					for(int k=0;k<inputs.length;k++)
					{
						
						EditText o =  (EditText)findViewById(inputs[k]);
						vals.add(o.getText().toString());
					
					}
						String fname = vals.get(0).toString();
						String lname = vals.get(1).toString();
						String em = vals.get(2).toString();
						String pw = vals.get(3).toString();
						
						
					
						pm = new ProgressDialog(this, R.style.MyTheme);
						pm.show();
			
						login = new Post();
						login.setCommunicator(this);
						
						StringCheck stringCheck = new StringCheck();
						
						if(stringCheck.checkSpecialCharsName(fname) && stringCheck.checkSpecialCharsName(lname)) {
					
			
							 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
					         nameValuePairs.add(new BasicNameValuePair("email", em));
					         nameValuePairs.add(new BasicNameValuePair("fname", fname));
					         nameValuePairs.add(new BasicNameValuePair("lname",lname));
					         nameValuePairs.add(new BasicNameValuePair("password",pw));
					         nameValuePairs.add(new BasicNameValuePair("username","##"));
					         login.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/adduser.php");
						}
						else {
							Toast.makeText(this, "Special Characters Not Allowed", Toast.LENGTH_LONG).show();
							pm.dismiss();
							pm = null;
						}
						
					}
					
					
				
				
			
			}
		}
		}
		
		@Override
		public void gotResponse(JSONObject s) {
			// TODO Auto-generated method stub
			
			String status;
			try {
				status = s.getString("loginstatus");
				if(status.equals("one"))
				{
					int[]	inputs = {R.id.signupfname,R.id.signuplname,R.id.signupemail,R.id.signuppassword,R.id.signupcpassword};
					EditText pass =  (EditText)findViewById(inputs[3]);
					String password = pass.getText().toString();
					
					EditText em =  (EditText)findViewById(inputs[2]);
					String email = em.getText().toString();
			
					
					Intent returnIntent = new Intent();
					returnIntent.putExtra("email",email);
					returnIntent.putExtra("password",password);
					setResult(RESULT_OK,returnIntent);
					finish();
		
				}
				else if(status.equals("four"))
				{
					Toast.makeText(this, "Email in use", Toast.LENGTH_LONG).show();
				}
				else
				{
				Toast.makeText(this, status , Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				pm.dismiss();
				pm = null;
		}
		
		
		
		
		
		
		

	}
	


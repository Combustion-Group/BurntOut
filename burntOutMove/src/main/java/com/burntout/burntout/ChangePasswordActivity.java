package com.burntout.burntout;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity implements Post.Communicator{
	Post login;
	ProgressDialog pm;
	String mypass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
//	/	bye();
		fixLayout();
	}
	
	
	public void fixLayout()
	{
		 int wid =   (int) (this.getResources().getDisplayMetrics().widthPixels * .92);
		int[]	inputs = {R.id.cpoldpassword,R.id.cpnewpassword,R.id.cpconfirmpassword};
		for(int i=0;i<inputs.length;i++)
		{
			
			EditText b =  (EditText)findViewById(inputs[i]);
			b.getLayoutParams().width = wid;
			
			
		}
		

		Button ba = (Button)findViewById(R.id.changepassbutton);
		ba.getLayoutParams().width = (int) (wid*.95);
	}	

	
	public void goAway(View v)
	{
		finish();
		
	}
	
	public void changePassword(View v)
	{
		String [] alerts = {"Old Password ","New Password","Confirm Password"};
		
		int[]	inputs = {R.id.cpoldpassword,R.id.cpnewpassword,R.id.cpconfirmpassword};
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
				
				EditText opass =  (EditText)findViewById(inputs[0]);
				String opassword = opass.getText().toString();
				EditText pass =  (EditText)findViewById(inputs[1]);
				String password = pass.getText().toString();
				EditText cpass =  (EditText)findViewById(inputs[2]);
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
					Context context =this; 
					SharedPreferences sharedPref = context.getSharedPreferences(
					getString(R.string.pref), Context.MODE_PRIVATE);
			   
					String email = sharedPref.getString("email", "no");
					@SuppressWarnings("unused")
					String oldpassword = sharedPref.getString("password", "no");
					
						
						
					
							pm = new ProgressDialog(this, R.style.MyTheme);
							pm.show();
				
							login = new Post();
							login.setCommunicator(this);
						
							mypass = password;
				
						 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
				         nameValuePairs.add(new BasicNameValuePair("email", email));
				         nameValuePairs.add(new BasicNameValuePair("newpassword", password));
				         nameValuePairs.add(new BasicNameValuePair("oldpassword", opassword));
				         login.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/changepassword.php");
						}
					
					
				
				
			
			}
		}
		
		
		
	}
	
	
	public boolean validated(){
		
		return true;
	}


	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		String status;
		try {
			status = s.getString("status");
			if(status.equals("one"))
			{
				
				Context context = this;
		    	SharedPreferences sharedPref = context.getSharedPreferences(
		    	        getString(R.string.pref), Context.MODE_PRIVATE);
		    
		    	SharedPreferences.Editor editor = sharedPref.edit();
		  		editor.putString("password", mypass);
		  		editor.commit();
				
				Toast.makeText(this,"Password Changed",Toast.LENGTH_LONG).show();
				finish();
			}
			else
			{
				Toast.makeText(this,"Old Password Incorrect! ",Toast.LENGTH_LONG).show();
			}
		
			} catch (JSONException e) {
		// TODO Auto-generated catch block
					e.printStackTrace();
			}
	
		pm.dismiss();
		pm = null;
		
		
		
	}
	
	
}

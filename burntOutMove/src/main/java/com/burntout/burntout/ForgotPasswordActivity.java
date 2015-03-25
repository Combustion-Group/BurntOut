package com.burntout.burntout;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity implements Post.Communicator {
	ProgressDialog pm;
	Post login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
//	/	bye();
		fixLayout();
	}
	
	
	public void fixLayout()
	{
		 int wid =   (int) (this.getResources().getDisplayMetrics().widthPixels * .92);
		int[]	inputs = {R.id.forgotemail};
		for(int i=0;i<inputs.length;i++)
		{
			
			EditText b =  (EditText)findViewById(inputs[i]);
			b.getLayoutParams().width = wid;
			
			
		}
		

		Button ba = (Button)findViewById(R.id.forgotpassbutton);
		ba.getLayoutParams().width = (int) (wid*.95);
	}	

	
	
	
	public void goAway(View v)
	{
		finish();
		
	}


	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
			try {
				if(s.getString("status").equals("one"))
				{
					Toast.makeText(this, "Your new password has been emailed!", Toast.LENGTH_LONG).show();			
				}
				else
				{
					Toast.makeText(this, "There was an error!", Toast.LENGTH_LONG).show();		
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		pm.dismiss();
		pm = null;
	}
	
	
	
	
	
	public void forgotPass(View v)
	{
	
		EditText fg = (EditText)findViewById(R.id.forgotemail);
		
		String text = fg.getText().toString();
		if(text.length() == 0)
		{
			Toast.makeText(this, "Email cannot be blank!!", Toast.LENGTH_LONG).show();	
		}
		else
		{
			pm = new ProgressDialog(this, R.style.MyTheme);
			pm.show();

			login = new Post();
			login.setCommunicator(this);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email", text));
			login.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/forgotPW.php");
		}
	}
	
}

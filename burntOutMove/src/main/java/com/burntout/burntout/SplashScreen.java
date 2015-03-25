package com.burntout.burntout;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		bye();
	}


	public void bye(){
		
		
		  new Handler().postDelayed(new Runnable() {
			  
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	         //       Intent i = new Intent(SplashScreen.this, LoginActivity.class);
	           //     startActivity(i);
	 
	                // close this activity
	                finish();
	            }
	        }, 3000);
		
	}
	
	
	

}

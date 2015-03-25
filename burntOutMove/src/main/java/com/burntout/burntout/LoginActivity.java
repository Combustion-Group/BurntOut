package com.burntout.burntout;






import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.support.v4.app.FragmentActivity;













import com.facebook.*;
import com.facebook.android.Facebook;
import com.facebook.model.*;
import com.facebook.widget.*;



 
public class LoginActivity extends FragmentActivity implements Post.Communicator, Communicator {
	 RelativeLayout main;
	 ImageView myImage;
	 ProgressDialog pm;
	 Post login;
	 Bundle savedInstanceState;
	
	 ArrayList<EditText>inputs;
	 Facebook facebook;
	 private PendingAction pendingAction = PendingAction.NONE;
	 private GraphUser user;
	 
	 String username;
    String birthday;
    String firstname;
    String lastname;
    String picture;
    String picurl;
    String gender;
    String age;
    String fbid;
    String login_lat;
    String login_lng;
    String device;
    String email;
    String token;
    @SuppressWarnings("unused")
	private LoginButton facebookButton;
    @SuppressWarnings("unused")
	private TextView facebookLoginStatus;
    RelativeLayout mainrelativelayout, fbLoginHolder;
    int isFBLogin;
    boolean isLoggedIn;
    boolean initialLogin = true;
    FBFragment fbFragment;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.login_screen);
			this.savedInstanceState = savedInstanceState;
			isLoggedIn = false;
			
			pm = null;
						
			mainrelativelayout = (RelativeLayout)findViewById(R.id.mainView);
			fbLoginHolder = (RelativeLayout)findViewById(R.id.fb_login_holder);
						
			
			bye();
			int[] ids = {R.id.email, R.id.password};
			inputs = new ArrayList<EditText>(ids.length);
			
			for(int i=0;i<ids.length;i++)
			{
				EditText b =  (EditText)findViewById(ids[i]);
				inputs.add(b);
			}
			
			
			
			fixLayout();
			
			checkIfLoggedIn();
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void bye(){
	
	     main  = (RelativeLayout)findViewById(R.id.mainView);
		 myImage = new ImageView(this);
		 
		 int wid =   (int) (getResources().getDisplayMetrics().widthPixels );
		 int height =   (int) (getResources().getDisplayMetrics().heightPixels);
		 Log.d("width", Integer.toString(wid));
		 Log.d("height", Integer.toString(height));
		 if(height >= 1280) {
			 myImage.setImageResource(R.drawable.splash);
		 }
		 else {
			 myImage.setImageResource(R.drawable.splash2);
		 }
		 
		 myImage.setScaleType(ScaleType.FIT_XY);
		 
		 main.addView(myImage);
		 main.bringChildToFront(myImage);
		 myImage.getLayoutParams().height = height;
		 myImage.getLayoutParams().width = wid;
		 new AlertDialog.Builder(this)
		  	.setTitle("Don't Text While Driving")
		  	.setMessage("Please pull over before using this app")
		  	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new Handler().postDelayed(new Runnable() {
						  
						
			            @Override
			            public void run() {
			    
			            		
							main.removeView(myImage);
							checkIfLoggedIn();
							addFBManager();
			            	
			            }
			        }, 3000);
					
				}
			})
		  	.show();
		  
		 
		 
		
		  
	}
	
	

		
		public void fixLayout()
		{
			 int wid =   (int) (this.getResources().getDisplayMetrics().widthPixels * .92);
			int[]	inputs = {R.id.password,R.id.email};
			for(int i=0;i<inputs.length;i++)
			{
				
				EditText b =  (EditText)findViewById(inputs[i]);
				b.getLayoutParams().width = wid;
				
				
			}
			
			//facebookButton.getLayoutParams().width = wid;
			Button ba = (Button)findViewById(R.id.loginbutton);
			ba.getLayoutParams().width = wid;
			/*
			ArrayList<String> fbPermissions = new ArrayList<String>();
			fbPermissions.add("public_profile");
			fbPermissions.add("email");
			fbPermissions.add("user_photos");
			
			fbFragment = LoginManager.getFBLogin();
			fbFragment.setPermissions(fbPermissions);
			
			fbFragment.setCallback(new Request.GraphUserCallback() {
				
				@Override
				public void onCompleted(GraphUser sentUser, Response response) {
					// TODO Auto-generated method stub
					
					user = sentUser;
					updateUI();
					
					firstname = user.getFirstName();
					lastname = user.getLastName();				
					email = (String) user.asMap().get("email");
					fbid = user.getId();
					picture = "https://graph.facebook.com/" + user.getId() + "/picture";
					isFBLogin = 1;
					
				}
			});
			
			if (savedInstanceState == null) {
				
			    getSupportFragmentManager()
			        .beginTransaction()
			        .add(android.R.id.content, fbFragment)
			        .commit();
			} else {
			    // Or set the fragment from restored state info
			    // this will be custom code
			}
			*/
		}	
		
		public void addFBManager() {
			
			
			ArrayList<String> fbPermissions = new ArrayList<String>();
			fbPermissions.add("public_profile");
			fbPermissions.add("email");
			fbPermissions.add("user_photos");
			
					
			fbFragment = LoginManager.getFBLogin();
			
			fbFragment.setPermissions(fbPermissions);
					
			fbFragment.setCallback(new Request.GraphUserCallback() {
						
				@Override
				public void onCompleted(GraphUser sentUser, Response response) {
					// TODO Auto-generated method stub
					
					user = sentUser;
					updateUI();
					
					firstname = user.getFirstName();
					lastname = user.getLastName();				
					email = (String) user.asMap().get("email");
					fbid = user.getId();
					picture = "https://graph.facebook.com/" + user.getId() + "/picture";
					isFBLogin = 1;
					
				}
			});
			
			
			if (savedInstanceState == null) {
				
			    getSupportFragmentManager()
			        .beginTransaction()
			        .add(R.id.fb_login_holder, fbFragment)
			        .commit();
			} else {
			    // Or set the fragment from restored state info
			    // this will be custom code
				
			}
			
		}
	
		
	
		
		public void forgotPass(View v)
		{
			Intent i = new Intent(this,ForgotPasswordActivity.class);
		
			this.startActivity(i);
			
		}
		
		
		public void tryToLogin(View v)
		{
				String email = inputs.get(0).getText().toString();
				String password = inputs.get(1).getText().toString();
				isFBLogin = 0;
				loginBurnt(email,password);
		}
		
		
		public void signUpScreen(View v)
		{
			
			Intent i = new Intent(this,SignUpActivity.class);
			   this.overridePendingTransition(R.anim.stay,
		               R.anim.slide_up_in);
			this.startActivityForResult(i, 1);
		}
		
		
		public void loginBurnt(String email, String password)
		{
			//if(!isLoggedIn) {
				if(validated())
				{
				
				pm = new ProgressDialog(this, R.style.MyTheme);
				pm.show();
		
				login = new Post();
				login.setCommunicator(this);
				
		
				 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		         nameValuePairs.add(new BasicNameValuePair("email", email));
		         nameValuePairs.add(new BasicNameValuePair("password",password));
		         login.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/login.php");
				}
			//}
			
			
		}
		
		
		
		@Override
		public void gotResponse(JSONObject s) {
			// TODO Auto-generated method stub
			
			String status;
			try {
				status = s.getString("status");
				if(status.equals("one"))
				{
				
				//	Toast.makeText(this, "JESUS",Toast.LENGTH_LONG).show();
					
					SharedPreferences sharedPref = ((Activity) this).getPreferences(Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString("fname", s.getString("fname"));
					String em = inputs.get(0).getText().toString();
					String pass = inputs.get(1).getText().toString();
				
					editor.putString("email", em);
					editor.putString("password", pass);
					
					Log.d("fblogin", Integer.toString(isFBLogin));
					editor.putString("isFB", Integer.toString(isFBLogin));
				
					editor.commit();
					
					//Log.d("isFB", s.getString("logintype"));
			//		 Toast.makeText(this, em, Toast.LENGTH_LONG).show();
					isLoggedIn = true;
					
					
					
					Intent i = new Intent(this,ProfileActivity.class);
					
					 i.putExtra("fname", s.getString("fname"));
					 i.putExtra("lname", s.getString("lname"));
					 i.putExtra("email", s.getString("email"));
					 i.putExtra("picture", s.getString("picture"));
					 i.putExtra("logintype", Integer.toString(isFBLogin));
					 
					 
					 
					 
				//	 Toast.makeText(this, s.getString("picture"),Toast.LENGTH_LONG).show();
					 String password = inputs.get(1).getText().toString();
					 i.putExtra("password",password);
					   this.overridePendingTransition(R.anim.stay,
				               R.anim.slide_up_in);
					this.startActivityForResult(i, 3);
				}
				else {
					if(!initialLogin) {
						Toast.makeText(this, "Incorrect Password" , Toast.LENGTH_LONG).show();
					}
				
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(pm!=null)
			{
				pm.dismiss();
				pm = null;
			}
			initialLogin = false;
		}

		
		
		public boolean validated(){
			
			String email = inputs.get(0).getText().toString();
			String password = inputs.get(1).getText().toString();
			if(email.length() > 0 && password.length() > 0) {
				return true;
			}
			else {
				return false;
			}
		}
		

	
public int checkLoginType()
{
	
	Context context =this; 
	SharedPreferences sharedPref = context.getSharedPreferences(
	  getString(R.string.pref), Context.MODE_PRIVATE);

	String ltype = sharedPref.getString("logintype","0");
	
	return Integer.parseInt(ltype);

}



public void checkIfLoggedIn()
{
	Context context =this; 
	SharedPreferences sharedPref = context.getSharedPreferences(
	  getString(R.string.pref), Context.MODE_PRIVATE);

	String email = sharedPref.getString("email", "no");
	String password = sharedPref.getString("password", "no");
	
	//Toast.makeText(this,email,Toast.LENGTH_LONG).show();
	if(!password.equals("no"))
	{
			//saved credentials
		
		
		loginBurnt(email,password);
	}

} 


  




protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
	
    if (requestCode == 1) {
        if(resultCode == RESULT_OK){
            String email=data.getStringExtra("email");
            String password=data.getStringExtra("password");
            loginBurnt(email,password);
        }
        if (resultCode == RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }
    else if(requestCode == 2)
    {
    	   if(resultCode == RESULT_OK){
               String email=data.getStringExtra("email");
               String password=data.getStringExtra("password");
               inputs.get(1).setText(password);
               inputs.get(0).setText(email);
               loginBurnt(email,password);
           }
           if (resultCode == RESULT_CANCELED) {
               //Write your code if there's no result
           }
    }
    else if(requestCode == 3) {
    	//return from profile
    	if(resultCode == RESULT_OK) {
    		
    		Session.getActiveSession().closeAndClearTokenInformation();
    		Session.setActiveSession(null);
    		isFBLogin =0;
    		
    		
    	}
    	else
    	{
    		isLoggedIn = false;
    		
    		
    	
    	}
    	
    	fbFragment.logout(this);
    	SharedPreferences sharedPref = this.getSharedPreferences(
				  getString(R.string.pref), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		
		editor.clear();
		editor.commit();
		
		for(int i=0;i<inputs.size();i++)
		{
			
			inputs.get(i).setText("");
			inputs.get(i).clearFocus();
		}
	
    }
    else
    {
    	  super.onActivityResult(requestCode, resultCode, data);
  	     Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

    }
}//onActivityResult

@SuppressWarnings("unused")
private Session.StatusCallback callback = new Session.StatusCallback() {
    @Override
    public void call(Session session, SessionState state, Exception exception) {
     onSessionStateChange(session, state, exception);
     
     String token = session.getAccessToken();
     Log.d("mytoken",token);
    }
};
private enum PendingAction {
    NONE,
    POST_PHOTO,
    POST_STATUS_UPDATE
}
@SuppressWarnings("unused")
private UiLifecycleHelper uiHelper;






@SuppressWarnings("unused")
private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
    @Override
    public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
        Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
    }

    @Override
    public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
        Log.d("HelloFacebook", "Success!");
    }
};
private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    if (pendingAction != PendingAction.NONE &&
            (exception instanceof FacebookOperationCanceledException ||
            exception instanceof FacebookAuthorizationException)) {
            new AlertDialog.Builder(LoginActivity.this)
                .setTitle("cancel")
                .setMessage("said no")
                .setPositiveButton("cool", null)
                .show();
   
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {

    }
    
}

private void updateUI() {
    Session session = Session.getActiveSession();
    boolean enableButtons = (session != null && session.isOpened());

    		
    if (enableButtons && user != null) {
    	picurl = "https://graph.facebook.com/";
    	
    //	 facebookLoginStatus.setText("Logout of Facebook");
    	 firstname  = user.getFirstName();
    	 lastname = user.getLastName();
    	 birthday = user.getBirthday();
    	 gender = (String) user.asMap().get("gender");
    	 fbid = user.getId();
    	 username = user.getUsername();
    	 picture = picurl + user.getId() + "/picture?width=400";
    	 device = "Android";
    	 email = (String) user.asMap().get("email");
    //	 Toast.makeText(this, email, Toast.LENGTH_LONG).show();
    	 
    	 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
    	 nameValuePairs.add(new BasicNameValuePair("email", email));
    	 nameValuePairs.add(new BasicNameValuePair("fname", firstname));
    	 nameValuePairs.add(new BasicNameValuePair("lname", lastname));
    	 nameValuePairs.add(new BasicNameValuePair("birthday", birthday));
    	 nameValuePairs.add(new BasicNameValuePair("gender", gender));
    	 nameValuePairs.add(new BasicNameValuePair("fbid", user.getId()));
    	 nameValuePairs.add(new BasicNameValuePair("picture", picture));
    	 
    	 Post fbLogin = new Post();
    	 fbLogin.setCommunicator(this);
    	 
    	 fbLogin.executePosts(nameValuePairs, "http://combustioninnovation.com/production/Goodyear/php/loginFB.php");
    	 
    	 
    	 
    	 
    }
}

@SuppressWarnings("unused")
private void logout(){
    // clear any user information
    isLoggedIn = false;
    
    // find the active session which can only be facebook in my app
    Session session = Session.getActiveSession();
    // run the closeAndClearTokenInformation which does the following
    // DOCS : Closes the local in-memory Session object and clears any persistent 
    // cache related to the Session.
    session.closeAndClearTokenInformation();
    
    session.isClosed();
    // return the user to the login screen
    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    // make sure the user can not access the page after he/she is logged out
    // clear the activity stack
    finish();
}

	public void refreshFB() {
		
		fbFragment = null;
		
		
		
		ArrayList<String> fbPermissions = new ArrayList<String>();
		fbPermissions.add("public_profile");
		fbPermissions.add("email");
		fbPermissions.add("user_photos");
		
				
		fbFragment = LoginManager.getFBLogin();
		
		fbFragment.setPermissions(fbPermissions);
				
		fbFragment.setCallback(new Request.GraphUserCallback() {
					
			@Override
			public void onCompleted(GraphUser sentUser, Response response) {
				// TODO Auto-generated method stub
				
				user = sentUser;
				updateUI();
				
				firstname = user.getFirstName();
				lastname = user.getLastName();				
				email = (String) user.asMap().get("email");
				fbid = user.getId();
				picture = "https://graph.facebook.com/" + user.getId() + "/picture";
				isFBLogin = 1;
				
			}
		});
		
		/*
		getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fb_login_holder, fbFragment);
        .commit();
		*/
		/*
		if (savedInstanceState == null) {
			
		    getSupportFragmentManager()
		        .beginTransaction()
		        .add(R.id.fb_login_holder, fbFragment)
		        .commit();
		} else {
		    // Or set the fragment from restored state info
		    // this will be custom code
			
		}
		*/
	}


}



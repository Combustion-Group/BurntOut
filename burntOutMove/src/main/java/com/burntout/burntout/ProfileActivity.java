package com.burntout.burntout;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;









import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;









import com.pkmmte.circularimageview.CircularImageView;









import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;









import com.squareup.picasso.Picasso;

public class ProfileActivity extends Activity implements Post.Communicator, VehicleViewer.Communicator, VehicleEditor.Communicator, NotificationsAdapter.Communicator, HorizontalListView.Communicator, NotificationsListView.Communicator, UploadPicture.Communicator, com.burntout.burntout.GetNewNotifiications.Communicator {
	
	Button ba;
	Post login;
	ProgressDialog pm;
	String myemail, userId;
	ArrayList<Vehicle> vehicleList;
	ArrayList<Notification> notificationList;
	UserData userData;
	View pictureButton;
	VehicleViewer arrayAdapter;
	NotificationsAdapter notificationsAdapter;
	VehicleEditor vehicleEditor;
	HorizontalListView vehicleScroller; 
	NotificationsListView notificationScroller;
	LinearLayout notificationContainer;
	ImageButton notificationButton, settingsButton;
	SharedPreferences sharedPref;
	TextView tv;
	Context context;
	int lt;
	SharedPreferences.Editor editors;
	int pos;
	CircularImageView r;
	Bundle bundle;
	boolean popoverIsOut;
	PageMarkers pageMarkers, pageMarkersNotifications;
	int totalNotifications;
	GetNewNotifiications notiUpdater;
	boolean firstLogin;
	private Uri mImageUri;
	boolean canClick = true;
	
	String lastFilename;
	
	ArrayList<TaskManager> taskManagers;
	
	 @SuppressWarnings("unused")
	private final ImageDownloader mDownload = new ImageDownloader();
	private Button reportButton;
	@SuppressLint("CutPasteId")
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_screen);
		
		ThreadPolicy tp = ThreadPolicy.LAX;
		StrictMode.setThreadPolicy(tp);
		fixLayout();
		
		context=this;
		
 		 Bundle bundle = getIntent().getExtras();
 		 
 		 vehicleList = new ArrayList<Vehicle>();
 		 notificationList = new ArrayList<Notification>();
 		 
 		popoverIsOut = false;
 		String userId = bundle.getString("user_id");
 		String usersrealname = bundle.getString("fname") + " " + bundle.getString("lname");
 		String first = bundle.getString("fname");
 		String last = bundle.getString("lname");
 		String picurl = bundle.getString("picture");
 		String phone = bundle.getString("phone");
 		String email = bundle.getString("email");
 		String password = bundle.getString("password");
 		String logintype = bundle.getString("logintype");
 		
 		lt = Integer.parseInt(logintype);
 		
 	//	 Toast.makeText(this, logintype,Toast.LENGTH_LONG).show();
 		notiUpdater = null;
 		firstLogin = true;
 		
 		 
 		 if(picurl.length()>0)
 		 {
 			 setProfileImage(picurl);
 		 }
 		 
 		
 		tv  = (TextView)findViewById(R.id.theusersname);
 		tv.setText(Html.fromHtml("<b>" + first + "</b> " + last));
 		pageMarkers = (PageMarkers)findViewById(R.id.pageMarkers1);
 		
 		RelativeLayout Holder = (RelativeLayout)findViewById(R.id.profilepicholder);
 		
 		Holder.setClipChildren(true);
 		
 		reportButton = (Button)findViewById(R.id.reportbout);
 		pictureButton = (View)findViewById(R.id.profilepic);
 		settingsButton = (ImageButton)findViewById(R.id.imageButton2);
 		notificationContainer = (LinearLayout)findViewById(R.id.notifications_container);
 		notificationContainer.setVisibility(View.GONE);
 		
 		pageMarkersNotifications = (PageMarkers)findViewById(R.id.pageMarkersNotifications);
 		
 		vehicleScroller = (HorizontalListView)findViewById(R.id.vehicles_listview);
 		vehicleScroller.setComm(this);
 		notificationScroller = (NotificationsListView)findViewById(R.id.notifications_listview);
 		notificationScroller.setComm(this);
 		notificationButton = (ImageButton)findViewById(R.id.edit_button);
 		notificationButton.setVisibility(View.GONE);
 		notificationButton.bringToFront();
 
 		
 		

		notificationsAdapter = new NotificationsAdapter(this, R.layout.notification, notificationList);
 		notificationsAdapter.setCommunicator(this);
 		notificationScroller.setAdapter(notificationsAdapter);
 		
 		r = (CircularImageView)findViewById(R.id.profilepic);
		r.setBorderColor(getResources().getColor(R.color.l_blue));
		r.setBorderWidth(10);
		r.addShadow();
 		
 		
		arrayAdapter = new VehicleViewer(this, R.layout.view_cars_item, vehicleList);
		arrayAdapter.setCommunicator(this);
	
		vehicleScroller.setAdapter(arrayAdapter);
		
 		//HorizontalListView vehicleScroller = (HorizontalListView)findViewById(R.id.viewvehicles_listview);
 		
 		//ImageView img= (ImageView) findViewById(R.id.profilepic);
 	
    

 	
 		myemail = email;
		
		Context context = this;
    	sharedPref = context.getSharedPreferences(
    	        getString(R.string.pref), Context.MODE_PRIVATE);
    
    	SharedPreferences.Editor editor = sharedPref.edit();
  		editor.putString("fname", first);
  		editor.putString("logintype", logintype);
  		editor.putString("lname", last);
  		editor.putString("picurl", picurl);
  		editor.putString("email", email);
  		editor.putString("password", password);
  		editor.putString("profEdited", "0");
  		editor.commit();
  		
  		
  		getProfileInformation();
  	
  		
  		ImageButton addCar = (ImageButton)findViewById(R.id.addcarbutton);
  		addCar.bringToFront();
  		
  		
  		taskManagers = new ArrayList<TaskManager>();
  		TaskManager addCarTask = new TaskManager(addCar);
  		taskManagers.add(addCarTask);
  		/*
  		TaskManager editCarTask = new TaskManager(arrayAdapter);
  		taskManagers.add(editCarTask);
  		*/
  		TaskManager notificationTask = new TaskManager(notificationButton);
  		taskManagers.add(notificationTask);
  		TaskManager settingsTask = new TaskManager(settingsButton);
  		taskManagers.add(settingsTask);
  		TaskManager pictureTask = new TaskManager(pictureButton);
  		taskManagers.add(pictureTask);
  		TaskManager reportTask = new TaskManager(reportButton);
  		taskManagers.add(reportTask);
  		
  		
  		
	}
	
	
	public void getProfileInformation()
	{
		
		login = new Post();
		login.setCommunicator(this);
		

		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
         nameValuePairs.add(new BasicNameValuePair("email", myemail));
       
         login.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/profile.php");
	}
	
	
	//goes to settings screen
	public void goSettings(View v)
	{
		
		unsetClickables();
		Intent i = new Intent(this, SettingsActivity.class);
		
		SerializedObject serializedObject = new SerializedObject(vehicleList);

		i.putExtra("vehicles", serializedObject);
		if(canClick) {
		this.startActivityForResult(i, 1);
		   this.overridePendingTransition(R.anim.stay,
	               R.anim.slide_up_in);
		   canClick = false;
		}
	}
	
	
	public void addNewCar(View v)
	{
		unsetClickables();
		Intent i = new Intent(this,AddCarActivity.class);
		if(canClick)
		{
			this.startActivityForResult(i, 3);
			   this.overridePendingTransition(R.anim.stay,
		               R.anim.slide_up_in);
			   canClick = false;
		}
	}
	
	
	//fixes the layouts to fit screen
	public void fixLayout()
	{
		 int wid =   (int) (this.getResources().getDisplayMetrics().widthPixels/3);
		int[]	labels = {R.id.reportedlabel,R.id.rankinglabel,R.id.recievedlabel,R.id.reportedcount,R.id.rankingcount,R.id.recievedcount};
		for(int i=0;i<labels.length;i++)
		{
			TextView b =  (TextView)findViewById(labels[i]);
			b.getLayoutParams().width = wid;
		}
		
		
			int wids =   (int) (this.getResources().getDisplayMetrics().widthPixels * .92);
			ba = (Button)findViewById(R.id.reportbout);
			ba.getLayoutParams().width = wids;

		
	}
	
	
	
	
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		setClickables();
		canClick = true;
		checkForNotifications();
	    if (requestCode == 1) {
	    	//settings return	    
    		
	    	
	    	String profEdited = sharedPref.getString("profEdited", null);
	    	String vehiclesEdited = data.getStringExtra("vehiclesEdited");
	    	
	    	
            if(profEdited.equals("1")) {
            		String newRealName = sharedPref.getString("fname", null) + " " + sharedPref.getString("lname", null);
            		Log.d("newRealName", newRealName);
            		tv.setText(newRealName);
            		arrayAdapter.notifyDataSetChanged();
            }	
	        if(resultCode == RESULT_OK){
	            String logout=data.getStringExtra("logout");
	            if(logout.equals("yes"))
	            {	
	            	SharedPreferences sharedPref = this.getSharedPreferences(
	        	        getString(R.string.pref), Context.MODE_PRIVATE);
	            
	        	SharedPreferences.Editor editor = sharedPref.edit();
	        					editor.clear().commit();
	            	 
	            
	        					
	        					if(checkLoginType() == 1)
	        					{
	        						   Intent output = new Intent();
	        						   setResult(RESULT_OK, output);
	        						   this.overridePendingTransition(R.anim.stay,
	        					               R.anim.slide_down_out);
	        					       finish();
	        					}
	        					else
	        					{
	        							Intent output = new Intent();
	        							setResult(RESULT_CANCELED, output);
	        							   this.overridePendingTransition(R.anim.stay,
	        						               R.anim.slide_down_out);
	        					        finish();
	        					}
	        					           	
	            }
	            if(vehiclesEdited.equals("1"))
                {
                    
                    
                    SerializedObject serializedObject = (SerializedObject)data.getSerializableExtra("vehicles");
                    
                    arrayAdapter.notifyDataSetInvalidated();
                    vehicleList = serializedObject.getMyVehicles();
                    vehicleScroller.setNumberofItems(vehicleList.size());
                    arrayAdapter = new VehicleViewer(this, R.layout.view_cars_item, vehicleList);
                    arrayAdapter.setCommunicator(this);
                    
                    vehicleScroller.setAdapter(arrayAdapter);
                    pageMarkers.setTotalPages(vehicleList.size());
                    pageMarkers.makeView(0);
                    
                    
                }
	            
	        
	        }
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result

	        }
	    }
	    
	    if(requestCode == 2) {
	    	
	    	
	    }
	    
	    if(requestCode == 3) {
	    	
	    	if(resultCode == RESULT_OK) {
	    		Vehicle addedVehicle = (Vehicle)data.getSerializableExtra("addedVehicle");
	    		vehicleList.add(addedVehicle);
	    		arrayAdapter.notifyDataSetChanged();
	    		 vehicleScroller.setNumberofItems(vehicleList.size());
	    		 int currentPage = pageMarkers.getCurrentPage();
	    		 pageMarkers.setTotalPages(vehicleList.size());
	    		 pageMarkers.makeView(currentPage);
	    	}
	    }
	    
	    if(requestCode == 4) {
	    	
	    	if(resultCode == RESULT_OK) {
	    			TextView reportee = (TextView)findViewById(R.id.reportedcount);
	    			String reporteeString = reportee.getText().toString();
	    			int prevReports = Integer.parseInt(reporteeString);
	    			int incReports = data.getIntExtra("increment", 0);
	    			String newReporteeString = Integer.toString(prevReports + incReports);
	    			reportee.setText(newReporteeString);
	    			
	    	}
	    }
	    //EDIT VEHICLE RETURN
	    if(requestCode == 5) {
	    	
	    	if(resultCode == RESULT_OK) {
	            Vehicle serializedObject = (Vehicle)data.getSerializableExtra("vehicle");
	            int thisPosition = data.getIntExtra("position", -1);
	            vehicleList.remove(thisPosition);
	            vehicleList.add(thisPosition, serializedObject);
	            arrayAdapter.notifyDataSetChanged();
	            vehicleScroller.setNumberofItems(vehicleList.size());
	    		
	    		
	    		
	    	}
	    }
	    
	  //take picture return
	    if(requestCode == 6) {
	    	if(resultCode == RESULT_OK) {
	    		
	    		
	    		Bitmap bitmap = (Bitmap) data.getExtras().get("bitmap");
	    						
							
				File file = new File(mImageUri.getPath());
				
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
	            
	            FileOutputStream out = null;
				
					try {
						out = new FileOutputStream(file);
						Log.d("success", "filemade");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
	        	byte[] byteArray = stream.toByteArray();
	            try {
					out.write(byteArray);
					out.flush();
		            out.close();
		            Log.d("file written", "file written");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            
				Log.d("filepath", file.getAbsolutePath());
				
				UploadPicture uploadPicture = new UploadPicture();
				pm = new ProgressDialog(this, R.style.MyTheme);
				pm.show();
				uploadPicture.setCommunicator(this);
				uploadPicture.executePosts("http://combustioninnovation.com/production/Goodyear/php/changeprofilepicture.php", file.getAbsolutePath(), myemail);
				
				r.setImageBitmap(bitmap);
				
				pm.dismiss();
				//Pi//casso.with(this).load(uri).placeholder(R.drawable.profileplaceholder).error(R.drawable.profileplaceholder).transform(new RoundTransform()).into(r);
				Log.d("pics", "set");
				//bitmap.recycle();
				//resizedbitmap1.recycle();
				}
	    
	    }
	    //choose gallery return
	    if(requestCode == 7) {
	    	if(resultCode == RESULT_OK && data != null) {
	    		
				
				
				//r.setImageBitmap(null);
	    		
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	            Cursor cursor = getContentResolver().query(selectedImage,
	                    filePathColumn, null, null, null);
	            cursor.moveToFirst();
	 
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);
	            cursor.close();
	            
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            Bitmap bitmapImage = BitmapFactory.decodeFile(picturePath);
	            String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/";
	            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	            String filename = timeStamp + "_" + userId + ".jpg";
	            File file =new File(path,filename);
	            
	            
	            ExifInterface exif = null;
	            Bitmap bmRotated = null;
				try {
					exif = new ExifInterface(filename);
					int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
					Log.d("orientation", Integer.toString(orientation));
		            bmRotated = rotateBitmap(bitmapImage, orientation); 
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
	            
				if(bmRotated != null) {
	            
					bmRotated.compress(Bitmap.CompressFormat.JPEG, 70, stream);
				}
				else {
					bitmapImage.compress(Bitmap.CompressFormat.JPEG, 70, stream);
				}
	            
	           
	            
	            byte[] byteArray = stream.toByteArray();
	            try {
	            	FileOutputStream out = new FileOutputStream(file);
					out.write(byteArray);
					MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
					out.flush();
		            out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           
	            String newPath = path + filename;
	            
	            
	            //Log.d("filepath", newPath);
	            
	            
	           
	            
	            
	            UploadPicture uploadPicture = new UploadPicture();
	            uploadPicture.setCommunicator(this);
	            uploadPicture.executePosts("http://combustioninnovation.com/production/Goodyear/php/changeprofilepicture.php", newPath, myemail);
	            
	            if(bmRotated != null) {
	            	r.setImageBitmap(bmRotated);
	            }
	            else {
	            	r.setImageBitmap(bitmapImage);
	            }
	            
		         
		        
			}
	    	
	    }
	  
	}//onActivityResult
	
	public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		Log.d("width", Integer.toString(width));
        Log.d("height", Integer.toString(height));
		

	    Matrix matrix = new Matrix();
		switch (orientation) {
		    case ExifInterface.ORIENTATION_NORMAL:
		        return bitmap;
		    case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
		        matrix.setScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_ROTATE_180:
		        matrix.setRotate(180);
		        break;
		    case ExifInterface.ORIENTATION_FLIP_VERTICAL:
		        matrix.setRotate(180);
		        matrix.postScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_TRANSPOSE:
		        matrix.setRotate(90);
		        matrix.postScale(-1, 1);
		        break;
		   case ExifInterface.ORIENTATION_ROTATE_90:
		       matrix.setRotate(90);
		       break;
		   case ExifInterface.ORIENTATION_TRANSVERSE:
		       matrix.setRotate(-90);
		       matrix.postScale(-1, 1);
		       break;
		   case ExifInterface.ORIENTATION_ROTATE_270:
		       matrix.setRotate(-90);
		       break;
		   case ExifInterface.ORIENTATION_UNDEFINED:
			   if(height >= width) {
				   matrix.setRotate(0);
			   }
			   else {
				   matrix.setRotate(90);
			   }
			   break;
		   
		   default:
		       return bitmap;
		}
		try {
		    Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		    //bitmap.recycle();
		    return bmRotated;
		}
		catch (OutOfMemoryError e) {
		    e.printStackTrace();
		    return null;
		}
		
	    
	}


	@Override
	public void gotResponse(JSONObject s)  {
		// TODO Auto-generated method stub
		try{
			JSONArray resultArray  = new 	JSONArray(s.getString("results"));
			
			JSONObject obj = resultArray.getJSONObject(0);
			
			
			
			JSONObject statsObj = new JSONObject(obj.getString("stats"));
			Log.d("statsObj", statsObj.toString());
			
			//gets the statistics object from the results array
			//JSONObject statsObj = obj.getJSONObject("stats");
			
			JSONArray usr = new JSONArray(obj.getString("userinfo"));
			JSONObject thisman = usr.getJSONObject(0);
			String ur = thisman.getString("picture");
			String isfb  = thisman.getString("user_isFB");
			String email = thisman.getString("email");
			userId = thisman.getString("user_id");
			lt = Integer.parseInt(isfb);
		//	Toast.makeText(this, ur, Toast.LENGTH_LONG).show();
			
			if(checkLoginType() == 1)
			{
			//	Toast.makeText(this,"DDDD",Toast.LENGTH_LONG).show();
				 if(ur.length()>0)
		 		 {
					 setProfileImagetwo(ur);
		 		 }
			}	
			else
			{
			
		//		Toast.makeText(this,"no",Toast.LENGTH_LONG).show();
			}
			
			
			String carsReported = statsObj.getString("reported");
			String carReportee = statsObj.getString("reportee");
			
			
			Log.d("reported", carsReported);
			//Toast.makeText(this, statstwoObj.toString(), Toast.LENGTH_LONG).show();
			
			
			
			
			TextView mr = (TextView)findViewById(R.id.myrank);
			String myRanking = statsObj.getString("ranking");
			String rankNum = statsObj.getString("my_rank");
			mr.setText(myRanking);
			
			
			String[] stat = {carsReported,carReportee,rankNum};
			int[]	labels = {R.id.reportedcount,R.id.recievedcount,R.id.rankingcount};
		
		
			
			for(int i=0;i<stat.length;i++)
			{
				TextView b =  (TextView)findViewById(labels[i]);
				b.setText(stat[i]);
			}
			
			//preferences objects
			JSONObject prefObj = new JSONObject( obj.getString("preferences"));
			String push_notifications = prefObj.getString("push_notifications");
			String facebook_notifications = prefObj.getString("facebook_notifications");
			
			
			
			Context context = this;
	    	SharedPreferences sharedPref = context.getSharedPreferences(
	    	        getString(R.string.pref), Context.MODE_PRIVATE);
	    
	    	SharedPreferences.Editor editor = sharedPref.edit();
	  		editor.putInt("push_notifications", Integer.parseInt(push_notifications));
	  		editor.putInt(" facebook_notifications", Integer.parseInt(facebook_notifications));
	  		editor.putString("isFB", isfb);
	  		editor.putString("email", email);
	  		editor.commit();
	  		
	  		JSONArray carsObj = new JSONArray(obj.getString("vehicles"));
	  		JSONArray notificationsObj = new JSONArray(obj.getString("notifications"));
	  		
	  		
	  		
	  		createVehiclesArray(carsObj);
	  		createNotificationsArray(notificationsObj);
		
		
		}catch (Exception e){
			
		}
		
	}
	
	public void createVehiclesArray(JSONArray carsObj) throws JSONException {
		
		//get total # vehicles
		int totalVehicles = carsObj.length();
		
		//create array to hold vehicle objects
	//	ArrayList<Vehicle> carsArray = new ArrayList<Vehicle>();
		
		//create a vehicle object for each vehicle
		for(int i=0; i<totalVehicles; i++) {
			
			JSONObject thisCar = carsObj.getJSONObject(i);
			String vehicle_id = thisCar.getString("vehicle_id");
			String vehicle_type_id = thisCar.getString("vehicle_type_id");
			String car_model = thisCar.getString("car_model");
			String plate_number = thisCar.getString("plate_number");
			String plate_state = thisCar.getString("plate_state");
			String created = thisCar.getString("created");
			
			Vehicle userCar = new Vehicle(vehicle_id, vehicle_type_id, car_model, plate_number, plate_state, created);
			vehicleList.add(userCar);
			
			Log.d("car_model", vehicleList.get(i).getCarModel());
		}
		
	
		vehicleScroller.setNumberofItems(totalVehicles);
		pageMarkers.setTotalPages(totalVehicles);
		pageMarkers.makeView(0);
		arrayAdapter.notifyDataSetChanged();		
		
		
		
	}
	
	public void createNotificationsArray(JSONArray notificationsObj) throws JSONException {
		Notification userNotification;
		totalNotifications = notificationsObj.length();
		
		
		
		
		
		
		if(totalNotifications > 0) {
			notificationButton.setVisibility(View.VISIBLE);
			
		}

		for(int j=0; j<totalNotifications; j++) {
			
			JSONObject thisNotification = notificationsObj.getJSONObject(j);
			
			
			
			String notificationId = thisNotification.getString("notification_id");
			String vehicleType = thisNotification.getString("vehicle_type");
			String message = thisNotification.getString("message");
			String lightsOut = thisNotification.getString("lights_out");
			String notificationFromId = thisNotification.getString("notification_fromID");
			String userFname = thisNotification.getString("user_fname");
			String userLname = thisNotification.getString("user_lname");
			String created = thisNotification.getString("created");
			String notifierReportedCount = thisNotification.getString("notifier_reported_count");
			String notifierReporterCount = thisNotification.getString("notifier_reporter_count");
			String notifierRanking = thisNotification.getString("notifier_ranking");
			String plateNumber = thisNotification.getString("plate_number");
			String picture = thisNotification.getString("picture");
			String theRanking = thisNotification.getString("theranking");
			
						
			userNotification = new Notification(notificationId, vehicleType, message, 
					lightsOut, notificationFromId, userFname, userLname, created, notifierReportedCount, 
					notifierReporterCount, notifierRanking, plateNumber, picture, theRanking);
			
		
			
			
			notificationList.add(userNotification);			
		
		}
		
	
 		notificationsAdapter.setTotalNotifications(notificationList.size());
 		notificationScroller.setNumberofItems(notificationList.size());
 		pageMarkersNotifications.setTotalPages(notificationList.size());
 		notificationsAdapter.notifyDataSetChanged();
		pageMarkersNotifications.makeView(0);
		
	}
	
	public void goToReport(View v) {
		unsetClickables();
		Intent i = new Intent(this, SendReportActivity.class);
		if(canClick) {
		startActivityForResult(i, 4);
		   this.overridePendingTransition(R.anim.stay,
	               R.anim.slide_up_in);
		   canClick = false;
		}
	}
	
	
	
	public void editThisVehicle(Vehicle vehicle, int position) {
		unsetClickables();
		Intent intent = new Intent(this, EditVehicleActivity.class);
		intent.putExtra("vehicle", vehicle);
		intent.putExtra("position", position);
		//   this.overridePendingTransition(R.anim.stay,
	     //          R.anim.slide_up_in);
		if(canClick) {
		startActivityForResult(intent, 5);
		   this.overridePendingTransition(R.anim.stay,
	               R.anim.slide_up_in);
		   canClick = false;
		}
		
		  
		
	}
	
	


	@Override
	public void deleteThisVehicle(Vehicle rowItem, int position) {
		// TODO Auto-generated method stub
		unsetClickables();
		pos = position;
		vehicleEditor = new VehicleEditor(rowItem, this, position, myemail);
		vehicleEditor.setCommunicator(this);
		vehicleEditor.deleteThisVehicle();
		//if(vehicleEditor.getResult()) {
		//	vehicleList.remove(position);
		//	arrayAdapter.notifyDataSetChanged();
	//	}
		
		vehicleScroller.scrollBack();
		setClickables();
	}
	
	public void vehicleDeleted(int position) {
		
		vehicleList.remove(position);
		arrayAdapter.notifyDataSetChanged();
		vehicleScroller.setNumberofItems(vehicleList.size());
		
		pageMarkers.setTotalPages(vehicleList.size());
		if(position > 0) {
			pageMarkers.makeView(0);
		}

	}
	
	public void fixNotificationArray(int position) {

		
		notificationList.remove(position);
		notificationsAdapter.notifyDataSetChanged();
		notificationScroller.setNumberofItems(notificationList.size());
		
		notificationScroller.post(new Runnable() {
            public void run() {
                notificationScroller.scrollBack();
            }
        });
		
		
		
		totalNotifications = totalNotifications - 1;
		
		if(notificationList.size() > 1) {
			pageMarkersNotifications.setTotalPages(notificationList.size());
			pageMarkersNotifications.makeView(position - 1);
		}
		else {
			pageMarkersNotifications.setTotalPages(0);
			pageMarkersNotifications.makeView(0);
		}
		
		
		
		
		if(notificationList.size() == 0) {
			notificationButton.setVisibility(View.GONE);
			
			notificationContainer.setVisibility(View.GONE);
			doGoodbyeNotification();
			
		}
		
	}
	
	public void notificationsClick(View v) {
		if(canClick) {
			if(!popoverIsOut) {
				popoverIsOut = true;
				// TODO Auto-generated method stub
				
				//animOpenNote();
				animOpenButton();
			
			}
			else {
				
				
			}
		
			canClick = false;
		}
	}
	
	
	
	@SuppressLint("SimpleDateFormat")
	public void changePicture(View v)
	{
		
		
		CharSequence colors[] = new CharSequence[] {"Take Picture", "From Gallery", "Cancel"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Do this!");
		builder.setItems(colors, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // the user clicked on colors[which]
		    	if(which == 0)
		    	{
		    		
		    		//Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		    		File directory = new File(Environment.getExternalStorageDirectory() + "/BurntOut/media/");
		    		if(!directory.exists()) {
		    			directory.mkdirs();
		    		}
		    		
		    		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		    		lastFilename = timeStamp + "_" + userId + ".jpg";
		    		
		    		File newImage = new File(directory + lastFilename);
		    		
		    	
		    		
		    		Intent intent = new Intent(context, ShootAndCropActivity.class);
		    	    
		    	    /*
		    	    try
		    	    {
		    	        // place where to store camera taken picture
		    	        photo = createTemporaryFile("JPG_" + userId, ".jpg");
		    	        photo.delete();
		    	    }
		    	    catch(Exception e)
		    	    {
		    	        
		    	        Toast.makeText(context, "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();;
		    	        
		    	    }
		    	    */
		    	    mImageUri = Uri.fromFile(newImage);
		    	    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		    	    
		    	    //start camera intent
		    	    if(canClick) {
		    	    	canClick = false;
			    		startActivityForResult(intent, 6);
			    		
			    		}
		    		
		    	}
		    	else if(which == 1)
		    	{
		    		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		    		if(canClick) {
		    		startActivityForResult(i, 7);
		    		canClick = false;
		    		}
		    		
		    	}
		    	else
		    	{
		    		canClick = true;
		    	}
		    }
		});
		builder.show();
	}
	
	@SuppressWarnings("unused")
	private File createTemporaryFile(String part, String ext) throws Exception
	{
	    File tempDir= Environment.getExternalStorageDirectory();
	    tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
	    if(!tempDir.exists())
	    {
	        tempDir.mkdir();
	    }
	    return File.createTempFile(part, ext, tempDir);
	}

	
	@Override
	public void onBackPressed() 
	{
		
		if(popoverIsOut)
		{
			doGoodbyeNotification();
		}
		else
		{
			
			new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Burnt Out")
	        .setMessage("Are you sure you want to Logout?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	Intent returnIntent = new Intent();
				returnIntent.putExtra("logout","yes");
				setResult(RESULT_CANCELED,returnIntent);
				finish();
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();
			/*
			if(checkLoginType() == 1)
			{
				   Intent output = new Intent();
				   setResult(RESULT_OK, output);
				   this.overridePendingTransition(R.anim.stay,
			               R.anim.slide_down_out);
			       finish();
			}
			else
			{
				 new AlertDialog.Builder(this)
			        .setIcon(android.R.drawable.ic_dialog_alert)
			        .setTitle("Burnt Out")
			        .setMessage("Are you sure you want to Logout?")
			        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
			    {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			        	Intent returnIntent = new Intent();
						returnIntent.putExtra("logout","yes");
						setResult(RESULT_CANCELED,returnIntent);
						finish();
			        }

			    })
			    .setNegativeButton("No", null)
			    .show();
					
			}
			*/
			
		}
		
		
	}
	
	
	//check for more notifications
public void checkForNotifications()
{
	notiUpdater =  new GetNewNotifiications();
	String e = myemail;
	notiUpdater.setCommunicator(this);
	String lastid = "0";
	if(notificationList.size()>0)
	{
		Notification n = notificationList.get(notificationList.size() -1);
		lastid = n.getNotification_id();
	};
	
	
	notiUpdater.startFetchingNotifications(lastid,this,e);
}
	
	
	
	
	
	
	public void setProfileImagetwo(String picurl)
	{

 	
		
		Picasso.with(this).load(picurl).placeholder(R.drawable.profileplaceholder).error(R.drawable.profileplaceholder).transform(new RoundTransform()).into(r);
	}

	
	
	
	public void setProfileImage(String picurl)
	{
		CircularImageView r	 = (CircularImageView)findViewById(R.id.profilepic);
 		
		Picasso.with(this).load(picurl).placeholder(R.drawable.profileplaceholder).error(R.drawable.profileplaceholder).transform(new RoundTransform()).into(r);
	
	}

	
	
	public int checkLoginType()
	{
		
		Context context =this; 
		SharedPreferences sharedPref = context.getSharedPreferences(
		  getString(R.string.pref), Context.MODE_PRIVATE);
		
		editors = sharedPref.edit();
		

		String ltype = sharedPref.getString("logintype","0");
		
		Log.d(" they login type",ltype);
		return lt;

	}

	
	
	public void hideNotificationView(View v)
	{
		doGoodbyeNotification();
	}
	
	
	public void doGoodbyeNotification()
	{
		//notificationContainer.setVisibility(View.GONE);
		popoverIsOut = false;
		canClick = true;
		notificationsAdapter.sayGoodByAni();
		//animCloseNote();
		animCloseButton();
		if(notificationList.size() == 0) {
			notificationButton.setVisibility(View.GONE);
		}
	}


	@Override
	public void setPageMarkers(int currentPage) {
		// TODO Auto-generated method stub
		pageMarkers.makeView(currentPage);
	}


	@Override
	public void setPageMarkersNotifications(int currentPage) {
		// TODO Auto-generated method stub
		pageMarkersNotifications.makeView(currentPage);
		
	}


	@Override
	public void gotSilentResponse() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if(firstLogin)
		{
			firstLogin = false;
		}
		else
		{
		
		}
	
	}

	@Override
	public void onPause()
	{
		super.onPause();
	
	}
	
	public void setClickables() {
		for(int i=0; i<taskManagers.size(); i++) {
			taskManagers.get(i).setClickable();
			
			
		}
	}
	
	public void unsetClickables() {
		for(int i=0; i<taskManagers.size(); i++) {
			taskManagers.get(i).unsetClickable();
		}
	}

	
	//from new notifications

	@Override
	public void gotNotificationResponse(JSONObject s) {
		// TODO Auto-generated method stub
		try {
			JSONArray resultArray  = new 	JSONArray(s.getString("results"));
			createNotificationsArray(resultArray);
			notificationButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					notificationsClick(v);
				}
			});
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void setClickable() {
		// TODO Auto-generated method stub
		canClick = true;
	}
	
	@SuppressWarnings("unused")
	public void animOpenNote() {
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int dpi = (int)metrics.density;
		
		checkForNotifications();
		
		notificationButton.setVisibility(View.GONE);
		
		
		ImageView button = (ImageView)findViewById(R.id.dummy_button);
		//button.setVisibility(View.VISIBLE);
		
		
		AnimatorSet animSet = new AnimatorSet();
		
		TranslateAnimation anim1 = new TranslateAnimation(0, -60 * dpi, 0, 0);
		
		
		//ObjectAnimator anim1 = ObjectAnimator.ofFloat(notificationButton, "X", origX, destX);
		//ObjectAnimator anim2 = ObjectAnimator.ofFloat(notificationButton, "Y", origY, destY);
		anim1.setDuration(500);
		//anim2.setDuration(800);
		
		//animSet.playTogether(anim1, anim2);
		
		notificationContainer.setVisibility(View.VISIBLE);
		notificationContainer.bringToFront();
		notificationContainer.setAlpha(0f);
		
		int width = notificationScroller.getWidth();
		int height = notificationScroller.getHeight();
		//notificationContainer.setVisibility(View.GONE);
		Log.d("width", Integer.toString(width));
		Log.d("height", Integer.toString(height));
		
		
		anim1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
				//notificationContainer.setVisibility(View.VISIBLE);
				notificationContainer.setAlpha(1f);
				/*
				notificationContainer.bringToFront();
				notificationContainer.forceLayout();
				*/
				Log.d("animation", "end");
				
				
				
				DisplayMetrics metrics = getResources().getDisplayMetrics();
				int dpi = (int)metrics.density;
				
				
				ImageView button = (ImageView)findViewById(R.id.dummy_button);
				button.setVisibility(View.GONE);
				
				
				
				//notificationScroller.setVisibility(View.VISIBLE);
				
				//notificationScroller.bringToFront();
				
				
				
				int width = notificationScroller.getWidth();
				int height = notificationScroller.getHeight();
				
				Log.d("width", Integer.toString(width));
				Log.d("height", Integer.toString(height));
				//notificationScroller.bringToFront();
				
				
				ValueAnimator anim1 = ValueAnimator.ofInt(0, width);
				anim1.addUpdateListener(new AnimatorUpdateListener() {
					
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						// TODO Auto-generated method stub
						int val = (Integer) animation.getAnimatedValue();
						LayoutParams params = notificationScroller.getLayoutParams();
						params.width = val;
						notificationScroller.setLayoutParams(params);
					}
				});
				ValueAnimator anim2 = ValueAnimator.ofInt(0, height);
				anim2.addUpdateListener(new AnimatorUpdateListener() {
					
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						// TODO Auto-generated method stub
						int val = (Integer) animation.getAnimatedValue();
						LayoutParams params = notificationScroller.getLayoutParams();
						params.height = val;
						notificationScroller.setLayoutParams(params);
					}
				});
				AnimatorSet animSet = new AnimatorSet();
				
				anim1.setDuration(800);
				anim2.setDuration(800);
				
				
				animSet.playTogether(anim1, anim2);
				
				
				animSet.addListener(new AnimatorListener() {
					
					@Override
					public void onAnimationStart(Animator animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animator animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animator animation) {
						// TODO Auto-generated method stub
						//notificationContainer.setVisibility(View.VISIBLE);
						//notificationScroller.setVisibility(View.VISIBLE);
						
						//notificationScroller.forceLayout();
						notificationContainer.bringToFront();
						checkForNotifications();
						//notificationContainer.bringToFront();
						//FrameLayout frame = (FrameLayout)findViewById(R.id.frameLayouts);
						//frame.setBackgroundResource(R.color.transparent_black);
						
						
					}
					
					@Override
					public void onAnimationCancel(Animator animation) {
						// TODO Auto-generated method stub
						
					}
				});
				
				
				
				animSet.start();
				
			}
				
			

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		//notificationButton.bringToFront();
		//notificationContainer.setVisibility(View.VISIBLE);
		//notificationContainer.setAlpha(0);
		//notificationContainer.bringChildToFront(button);
		//notificationButton.setVisibility(View.GONE);
		//notificationContainer.setVisibility(View.GONE);
		button.setVisibility(View.VISIBLE);
		button.startAnimation(anim1);
		
		//animSet.start();
		
	}
	
	@SuppressWarnings("unused")
	public void animCloseNote() {
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int dpi = (int)metrics.density;
		
		
		ImageView button = (ImageView)findViewById(R.id.dummy_button);
		button.setVisibility(View.GONE);
		
		
		
		//notificationScroller.setVisibility(View.VISIBLE);
		
		//notificationScroller.bringToFront();
		
		
		
		int width = notificationScroller.getWidth();
		int height = notificationScroller.getHeight();
		
		Log.d("width", Integer.toString(width));
		Log.d("height", Integer.toString(height));
		//notificationScroller.bringToFront();
		
		
		ValueAnimator anim1 = ValueAnimator.ofInt(width, 0);
		anim1.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				int val = (Integer) animation.getAnimatedValue();
				LayoutParams params = notificationScroller.getLayoutParams();
				params.width = val;
				notificationScroller.setLayoutParams(params);
			}
		});
		ValueAnimator anim2 = ValueAnimator.ofInt(height, 0);
		anim2.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				int val = (Integer) animation.getAnimatedValue();
				LayoutParams params = notificationScroller.getLayoutParams();
				params.height = val;
				notificationScroller.setLayoutParams(params);
			}
		});
		AnimatorSet animSet = new AnimatorSet();
		
		anim1.setDuration(800);
		anim2.setDuration(800);
		
		
		animSet.playTogether(anim1, anim2);
		
		
		animSet.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				ImageView button = (ImageView)findViewById(R.id.dummy_button);
				button.setVisibility(View.VISIBLE);
				button.bringToFront();
				
				DisplayMetrics metrics = getResources().getDisplayMetrics();
				int dpi = (int)metrics.density;
				
				notificationContainer.setVisibility(View.GONE);
				
				LayoutParams params = notificationScroller.getLayoutParams();
				params.width = 300 * dpi;
				params.height = 480 * dpi;
				notificationScroller.setLayoutParams(params);
							
				
				TranslateAnimation anim1 = new TranslateAnimation(-60 * dpi, 0, 0, 0);
				anim1.setDuration(500);
				
				anim1.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						ImageView button = (ImageView)findViewById(R.id.dummy_button);
						button.setVisibility(View.GONE);
						notificationButton.setVisibility(View.VISIBLE);
						
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
				button.startAnimation(anim1);
				
				
				
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		animSet.start();
		
	}
	
	@SuppressWarnings("unused")
	public void animOpenButton() {
		
		canClick = false;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int dpi = (int)metrics.density;
		
		checkForNotifications();
		
		notificationButton.setVisibility(View.GONE);
		
		
		ImageView button = (ImageView)findViewById(R.id.dummy_button);
		button.setVisibility(View.VISIBLE);
		AnimatorSet animSet = new AnimatorSet();
		
		TranslateAnimation anim1 = new TranslateAnimation(0, -60 * dpi, 0, 0);
		
		anim1.setDuration(500);
		
		anim1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				notificationContainer.setVisibility(View.VISIBLE);
				notificationContainer.bringToFront();
				ImageView button = (ImageView)findViewById(R.id.dummy_button);
				button.setVisibility(View.GONE);
				canClick = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		button.startAnimation(anim1);
		
	}
	
	public void animCloseButton() {
		
		canClick = false;
		ImageView button = (ImageView)findViewById(R.id.dummy_button);
		button.setVisibility(View.VISIBLE);
		button.bringToFront();
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int dpi = (int)metrics.density;
		
		notificationContainer.setVisibility(View.GONE);
		
		TranslateAnimation anim1 = new TranslateAnimation(-60 * dpi, 0, 0, 0);
		anim1.setDuration(500);
		
		anim1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				ImageView button = (ImageView)findViewById(R.id.dummy_button);
				button.setVisibility(View.GONE);
				if(notificationList.size() == 0) {
					notificationButton.setVisibility(View.GONE);
				}
				else {
					notificationButton.setVisibility(View.VISIBLE);
				}
				canClick = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		button.startAnimation(anim1);
		
		
	}
	
}


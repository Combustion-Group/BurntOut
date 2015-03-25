package com.burntout.burntout;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnTouchListener;


@SuppressLint({ "ClickableViewAccessibility", "InflateParams" })
public class AutoPicker extends HorizontalScrollView implements OnTouchListener {
	private static final int SWIPE_MIN_DISTANCE = 5;
	private static final int SWIPE_THRESHOLD_VELOCITY = 300;
	LinearLayout internalWrapper;
	int width;
	Context c;
	int height;


	private int mItems = 4;
	private GestureDetector mGestureDetector;
	private int mActiveFeature = 0;
	
	private int[] rIDs;
	private String[] strings;

	public AutoPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(!isInEditMode()){
			   // Your custom code that is not letting the Visual Editor draw properly
			   // i.e. thread spawning or other things in the constructor
				initItems(context);
			}
	
	}

	public AutoPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!isInEditMode()){
			   // Your custom code that is not letting the Visual Editor draw properly
			   // i.e. thread spawning or other things in the constructor
				initItems(context);
			}
	
	}

	public AutoPicker(Context context) {
		super(context);
		if(!isInEditMode()){
			   // Your custom code that is not letting the Visual Editor draw properly
			   // i.e. thread spawning or other things in the constructor
				initItems(context);
			}
	
		
	
	}
	
	public int getCurrentVehicle() {
		
		return mActiveFeature;
	}
	
	public int getAPWidth() {
		return width;
	}
	
	
	
	
	
	
	public void initItems(Context context)
	{
		rIDs = new int[4];
		rIDs[0] = R.drawable.frontcars;
		rIDs[1] = R.drawable.frontbikes;
		rIDs[2] = R.drawable.fronttrucks;
		rIDs[3] = R.drawable.frontbuss;
		
		strings = new String[4];
		strings[0] = "Car";
		strings[1] = "Bike";
		strings[2] = "Truck";
		strings[3] = "Bus";
		
		
		
		this.c = context;
		
	      width =   context.getResources().getDisplayMetrics().widthPixels;
	      height =   context.getResources().getDisplayMetrics().heightPixels;
	      this.setVerticalScrollBarEnabled(false);
	      this.setHorizontalScrollBarEnabled(false);
	      this.setOnTouchListener(this);
	      setFeatureItems(context);
	}

	@SuppressWarnings("deprecation")
	public void setFeatureItems(Context c){
		ViewHolder holder = null;
		LayoutInflater mInflater = (LayoutInflater) c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		internalWrapper = new LinearLayout(c);
		internalWrapper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
		internalWrapper.getLayoutParams().width = width * 5;
		this.addView(internalWrapper);
		
		for(int i=0; i<rIDs.length; i++) {
			
			View convertView = mInflater.inflate(R.layout.autopicker_item, null);
			
			holder = new ViewHolder();
			holder.vehicleImage = (ImageView)convertView.findViewById(R.id.ap_item_vtype);
			holder.vehicleText = (TextView)convertView.findViewById(R.id.ap_item_text);
			
			
			holder.vehicleImage.setImageDrawable(c.getResources().getDrawable(rIDs[i]));
			holder.vehicleText.setText(strings[i]);
			internalWrapper.addView(convertView);
			
			convertView.getLayoutParams().width = width ;
			convertView.getLayoutParams().height = height ;
			
			
		}
		
		

	
		 
		
	
	
		

	//	imagethree.setOnClickListener(new View.OnClickListener() {

		//	@Override
		//	public void onClick(View v) {
				// TODO Auto-generated method stub
			//	TutorialActivity activity = (TutorialActivity)TutorialScrollView.this.c;
			
			//	activity.finish();
		
//	}
	///	});
	
		
		
 		mGestureDetector = new GestureDetector(new MyGestureDetector());
 		//mGestureDetector = new GestureDetector(new MyGestureDetector());
 	}
 	 	class MyGestureDetector extends SimpleOnGestureListener {
 		@Override
 		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
 			try {
 				//right to left
  				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					int featureWidth = getMeasuredWidth();
					mActiveFeature = (mActiveFeature < (mItems))? mActiveFeature + 1:mItems;
 					smoothScrollTo(mActiveFeature*featureWidth, 0);
 			
 					return true;
 				}
   				//left to right
 				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					int featureWidth = getMeasuredWidth();
					mActiveFeature = (mActiveFeature > 0)? mActiveFeature - 1:0;
					smoothScrollTo(mActiveFeature*featureWidth, 0);
			
					return true;
				}
			} catch (Exception e) {
			        Log.e("Fling", "There was an error processing the Fling event:" + e.getMessage());
			}
			return false;
		}
	}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
 				//If the user swipes
 				if (mGestureDetector.onTouchEvent(event)) {
 				
 					
 					return true;
 				}
 				
 				else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL ){
 					int scrollX = getScrollX();
 					int featureWidth = v.getMeasuredWidth();
 					mActiveFeature = ((scrollX + (featureWidth/2))/featureWidth);
 					int scrollTo = mActiveFeature*featureWidth;
 					smoothScrollTo(scrollTo, 0);
 				
 					return true;
 				}
 				else{
 					return false;
 				}
 			}

		public void AddThisView(View v) {
			// TODO Auto-generated method stub
			internalWrapper.addView(v);
		}
		
		public void setVehicle(int vType) {
			mActiveFeature = vType;
			int featureWidth = getMeasuredWidth();
			smoothScrollTo(vType * featureWidth, 0);
		}
		
		private class ViewHolder {
			@SuppressWarnings("unused")
			RelativeLayout listItem;
			ImageView vehicleImage;
			TextView vehicleText;
			
		}
	
}
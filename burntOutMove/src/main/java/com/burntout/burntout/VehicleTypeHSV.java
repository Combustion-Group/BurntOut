package com.burntout.burntout;


import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.view.View.OnTouchListener;


public class VehicleTypeHSV extends HorizontalScrollView implements OnTouchListener {
	private static final int SWIPE_MIN_DISTANCE = 5;
	private static final int SWIPE_THRESHOLD_VELOCITY = 300;
	LinearLayout internalWrapper;
	HorizontalScrollView siblingScroll;
	ArrayList<VehicleTypeManager>managers;
	
	Communicator comm;
	
	
	int width;

	@SuppressWarnings("rawtypes")
	private ArrayList mItems = null;
	private GestureDetector mGestureDetector;
	private int mActiveFeature = 0;
	int oldNumber  = 0;

	public VehicleTypeHSV(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initItems(context);
	}

	public VehicleTypeHSV(Context context, AttributeSet attrs) {
		super(context, attrs);
		initItems(context);
	}

	public VehicleTypeHSV(Context context) {
		super(context);
		initItems(context);
		
	
	}
	
	
	public void setScroller(HorizontalScrollView v)
	{
		this.siblingScroll = v;
	}
	
	@SuppressLint("ClickableViewAccessibility")
	public void initItems(Context context)
	{

			managers = new ArrayList<VehicleTypeManager>();
			//friends.setBackgroundColor(Color.parseColor("#cccccc"));
		
			width =   context.getResources().getDisplayMetrics().widthPixels;
	      
			this.setVerticalScrollBarEnabled(false);
			this.setHorizontalScrollBarEnabled(false);
			this.setOnTouchListener(this);
	}
	
	
	public int getActiveFeature() {
		return mActiveFeature;
	}
	
	
	
	public void addManagers(Context c)
	{
		int [] frontImages = {R.drawable.frontcar,R.drawable.frontbike,R.drawable.fronttruck,R.drawable.frontbus};
		int [] backImages = {R.drawable.backcar,R.drawable.backbike,R.drawable.backtruck,R.drawable.backbus};
		
		for(int i=0;i<4;i++)
		{
			VehicleTypeManager t = new VehicleTypeManager(c);
			t.initialSetUp(c,i,frontImages[i],backImages[i]);
			managers.add(t);
			addViewToLv(t);
			t.getLayoutParams().width = width;
			if(i == 0)
			{
				setManagerImage(0);
			}
		}
		
	}

	
	

	
	
public void	setManagerImage(int position)
	{
		
	
		VehicleTypeManager m =  managers.get(position);
		m.setImages();
		m.resetLights();
		if(position != oldNumber)
		{
			VehicleTypeManager oldM = managers.get(oldNumber);
			oldM.destroyImages();
			oldM.turnLightsOff();
			oldNumber = position;
		}
		
	}
	
	
	

	
	public void addViewToLv(View v)
	{
		
		internalWrapper.addView(v);
	}
	
	@SuppressWarnings("deprecation")
	public void setFeatureItems(){
	  internalWrapper = new LinearLayout(getContext());
		internalWrapper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
		internalWrapper.getLayoutParams().width = width * 4;
		this.addView(internalWrapper);
 		mGestureDetector = new GestureDetector(new MyGestureDetector());
 	}
 	 	class MyGestureDetector extends SimpleOnGestureListener {
 		@Override
 		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
 			try {
 				//right to left
  				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					int featureWidth = getMeasuredWidth();
					mActiveFeature = (mActiveFeature < (mItems.size() - 1))? mActiveFeature + 1:mItems.size() -1;
 					smoothScrollTo(mActiveFeature*featureWidth, 0);
 					setManagerImage(mActiveFeature);
 					comm.setPageMarkers(mActiveFeature);

 					return true;
 				}
   				//left to right
 				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					int featureWidth = getMeasuredWidth();
					mActiveFeature = (mActiveFeature > 0)? mActiveFeature - 1:0;
					smoothScrollTo(mActiveFeature*featureWidth, 0);
					setManagerImage(mActiveFeature);
					comm.setPageMarkers(mActiveFeature);
					return true;
				}
			} catch (Exception e) {
			        Log.e("Fling", "There was an error processing the Fling event:" + e.getMessage());
			}
 			comm.setPageMarkers(mActiveFeature);
			return false;
		}
	}
		@SuppressLint("ClickableViewAccessibility")
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
 					setManagerImage(mActiveFeature);
 					comm.setPageMarkers(mActiveFeature);
 					return true;
 				}
 				else{
 					return false;
 				}
 			}
		
		public interface Communicator {
			
			public void setPageMarkers(int currentPage);
		}
		

		public void setComm(Communicator comm) {
			this.comm = comm;
		}
	
}
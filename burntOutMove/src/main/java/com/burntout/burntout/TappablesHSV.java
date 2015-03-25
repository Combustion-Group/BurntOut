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
import android.widget.TextView;
import android.view.View.OnTouchListener;


@SuppressLint("ClickableViewAccessibility")
public class TappablesHSV extends HorizontalScrollView implements OnTouchListener, tappableManager.Communicator {
	private static final int SWIPE_MIN_DISTANCE = 5;
	private static final int SWIPE_THRESHOLD_VELOCITY = 300;
	LinearLayout internalWrapper;
	LinearLayout textHolder;
	TextView frontText, backText;
	HorizontalScrollView siblingScroll;
	ArrayList<tappableManager>managers;
	Communicator comm;
	
	int pictureWidth;
	
	String reported;
	String viewedReported;
	
	Context c;
	
	
	int width;

	@SuppressWarnings("rawtypes")
	private ArrayList mItems = null;
	private GestureDetector mGestureDetector;
	private int mActiveFeature = 0;
	int oldNumber  = 0;

	public TappablesHSV(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initItems(context);
	}

	public TappablesHSV(Context context, AttributeSet attrs) {
		super(context, attrs);
		initItems(context);
	}

	public TappablesHSV(Context context) {
		super(context);
		initItems(context);
		
	
	}
	
	
	public void setScroller(HorizontalScrollView v)
	{
		this.siblingScroll = v;
	}
	
	public void initItems(Context context)
	{

			managers = new ArrayList<tappableManager>();
			//friends.setBackgroundColor(Color.parseColor("#cccccc"));
		
			width =   context.getResources().getDisplayMetrics().widthPixels;
	      
			this.setVerticalScrollBarEnabled(false);
			this.setHorizontalScrollBarEnabled(false);
			this.setOnTouchListener(this);
			
			this.c = context;
			
			
			
	}
	
	
	
	
	public void addManagers(Context c)
	{
		int [] frontImages = {R.drawable.frontcars,R.drawable.frontbikes,R.drawable.fronttrucks,R.drawable.frontbuss};
		int [] backImages = {R.drawable.backcars,R.drawable.backbikes,R.drawable.backtrucks,R.drawable.backbuss};
		
		
		
		
		for(int i=0;i<4;i++)
		{
			tappableManager t = new tappableManager(c);
			t.setCommunicator(this);
			t.initialSetUp(c,i,frontImages[i],backImages[i]);
			managers.add(t);
			addViewToLv(t);
			t.getLayoutParams().width = width;
			if(i == 0)
			{
				tappableManager m =  managers.get(0);
				m.setImages();
				m.resetLights();
			}
		}
		
	}

	
	

	
	
public void	setManagerImage(int position)
	{
		
	
	
		if(position != oldNumber)
		{
			tappableManager m =  managers.get(position);
			m.setImages();
			m.resetLights();
			tappableManager oldM = managers.get(oldNumber);
			oldM.destroyImages();
			oldM.turnLightsOff();
			m.setImages();
			oldNumber = position;
			comm.setReports("", "");
		}
		
	}

	public int getActiveFeature() {
		return mActiveFeature;
	}
	
	
	

	
	public void addViewToLv(View v)
	{
		
		internalWrapper.addView(v);
		
		comm.setTextFields(mActiveFeature);
		
		
		
		
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
 					comm.setTextFields(mActiveFeature);
 					

 					return true;
 				}
   				//left to right
 				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					int featureWidth = getMeasuredWidth();
					mActiveFeature = (mActiveFeature > 0)? mActiveFeature - 1:0;
					smoothScrollTo(mActiveFeature*featureWidth, 0);
					setManagerImage(mActiveFeature);
					comm.setPageMarkers(mActiveFeature);
					comm.setTextFields(mActiveFeature);
					
					return true;
				}
			} catch (Exception e) {
			        Log.e("Fling", "There was an error processing the Fling event:" + e.getMessage());
			}
 			comm.setPageMarkers(mActiveFeature);
 			comm.setTextFields(mActiveFeature);
 			
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
 					setManagerImage(mActiveFeature);
 					comm.setPageMarkers(mActiveFeature);
 					comm.setTextFields(mActiveFeature);
 					return true;
 				}
 				else{
 					return false;
 				}
 			}
		
		public void setCommunicator(Communicator c) {
			this.comm = c;
		}

		@Override
		public void getReports(ArrayList<String> brokenLights,
				ArrayList<String> viewedBrokenLights) {
			// TODO Auto-generated method stub
			
			reported = "";
			viewedReported = "";
			if(brokenLights.size() != 0) {
				
			
				for(int i=0; i<brokenLights.size(); i++) {
					reported += brokenLights.get(i);
					viewedReported += viewedBrokenLights.get(i);
					if(i != brokenLights.size() - 1) {
						reported += ", ";
						viewedReported += ", ";
					}
				}	
			}
			
			comm.setReports(reported, viewedReported);
			
		}
		
		public interface Communicator {
			
			public void setReports(String reported, String viewedReported);
			public void setPageMarkers(int currentPage);
			public void setTextFields(int width);
		}
		
		
		
		public String getReported() {
			return reported;
		}
	
}
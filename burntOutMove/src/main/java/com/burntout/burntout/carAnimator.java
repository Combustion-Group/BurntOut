package com.burntout.burntout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class carAnimator extends FrameLayout{
		ArrayList<littleLight>lights;
		int carType;
		String [] labels;
		FrameLayout frontView;
		FrameLayout backView;
		ImageView frontImage;
		ImageView backImage;
		boolean isfronton;
		boolean isbackon;
		boolean animrunning = false;
		FrameLayout parent;
		int numberofviews;
		int currentview;
		Context c;
		float density;
		int animDuration;
		boolean frontIsShowing;
		Handler handler = new Handler();
		Runnable r;
		Communicator comm;
		
	public carAnimator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	
		if(!this.isInEditMode())
		{
			isfronton = false;
			isbackon = false;
			this.parent = new FrameLayout(context);
			currentview = 0;
			numberofviews = 0;
			this.c = context;
			density = context.getResources().getDisplayMetrics().density;
			animDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
			frontIsShowing = true;
			r = null;
		}
	}

	public carAnimator(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(!this.isInEditMode())
		{
			isfronton = false;
			isbackon = false;
			this.parent = new FrameLayout(context);
			currentview = 0;
			numberofviews = 0;
			this.c = context;
			density = context.getResources().getDisplayMetrics().density;
			animDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
			frontIsShowing = true;
			r = null;
		}
	}

	public carAnimator(Context context) {
		super(context);
		if(!this.isInEditMode())
		{
			isfronton = false;
			isbackon = false;
			this.parent = new FrameLayout(context);
			currentview = 0;
			numberofviews = 0;
			this.c = context;
			density = context.getResources().getDisplayMetrics().density;
			animDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
			frontIsShowing = true;
			r = null;
		}
	}
	
	
	public void attachView(FrameLayout l){
		
		this.parent.removeAllViews();
		numberofviews = 0;
		isfronton = false;
		isbackon = false;
		this.parent = l;
		
		this.parent.addView(this);
		
		this.setX(23 * density);
		this.setY(15 * density);
		
		this.getLayoutParams().height=(int) (55*density);
		this.getLayoutParams().width =(int) (55 * density);

	//	this.parent = null;
		//currentview = 0;
	}
	
	
	@SuppressLint("NewApi") public void changeView(int current)
	{
		
	//	if(currentview != current)
		//{
		
	
			Log.d("tried","it tried" + Integer.toString(current));
		
			if(r != null)
			{
				handler.removeCallbacks(r);
				r = null;
			}
				if(isfronton)
				{
					this.frontView.removeAllViews();
					this.frontImage.setBackground(null);
					this.frontImage = null;
					this.removeView(this.frontView);
					this.frontView = null;
					
				}
				if(isbackon)
				{
					this.backView.removeAllViews();
					this.backImage = null;
					this.removeView(this.backView);
					this.backView = null;
				}
			
				numberofviews = 0;
				isfronton = false;
				isbackon = false;
				
	
		//		handler.
					
	}
	public void addLights(String  labels, int carType)
	{	
		int thisLight;
		this.carType = carType;
		List<int[]> positions = getPositionsForType(carType);
		List<String> list = new ArrayList<String>(Arrays.asList(labels.split(" ")));
		List<String> vehicleLabels = getLabelsForType(carType);
		List<Integer> vehicleColors = getColorsForType(carType);
		@SuppressWarnings("unused")
		List<Integer> frames = getFrames(carType);
		lights = new ArrayList<littleLight>();
		List<Integer> vehicleFrames = getFrames(carType);
		
		
		
		//goes through all the strings conttain front or back it will add the views if they arenot added yet, and increment the number of views. we then have to loop thorugh
		//each string to check the index of the value ie. Front left headlight has to map to an index in our static array. we then find the index positiobn of the light we want to add and then add it to the 
		//front or back view depending on the first wod in our string.
		
		Log.d("test", list.toString());
		
		if (list.contains("Front")){
		         // found a match to "software"
			
			checkIfFront();
			numberofviews++;
			
		}
		if (list.contains("Back")) {
			checkIfBack();
			numberofviews++;
		
		}
		
		
		list = new ArrayList<String>(Arrays.asList(labels.split(", ")));
		Log.d("listsize", Integer.toString(list.size()));
		Log.d("numviews", Integer.toString(numberofviews));
		
		
		for(int i=0; i<list.size(); i++) {
			
			String thisLabel = list.get(i);
			Log.d("thislabel", thisLabel);
			
			if(vehicleLabels.contains(thisLabel)) {
				thisLight = vehicleLabels.indexOf(thisLabel);
				
				
				int posX = (int) (positions.get(thisLight)[0] * density);
				int posY = (int) (positions.get(thisLight)[1] * density);
				int w = (int) (positions.get(thisLight)[2] * density);
				int h = (int) (positions.get(thisLight)[3] * density);
				int color = vehicleColors.get(thisLight);
				FrameLayout parentView = null;
				
				
				
				if(vehicleFrames.get(thisLight) == 0) {
					parentView = frontView;
				}
				else {
					parentView = backView;
				}
				
				
				
				littleLight light = new littleLight(c);
				light.addLight(posX, posY, h, w, color, parentView);
				lights.add(light);
			}
		}
		
		Log.d("lightssize", Integer.toString(lights.size()));
		
		if(frontImage != null) {
			frontImage.bringToFront();
		}
		if(backImage != null) {
			backImage.bringToFront();
		}
		
		
		if(numberofviews == 2)
		{
			
			setAnimationLoop();
			comm.setHandler(handler, r);
			
		}
		
		
	}
	
	
	
	public void checkIfFront()
	{
		
		
		if(!isfronton)
		{
			
			isfronton=true;
			addFrontView();
			
			
		}
		
	}
	
	public void checkIfBack()
	{
		
		if(!isbackon)
		{
			isbackon=true;
			addBackView();
			
		}
		
	
	}
	
	
	public void addFrontView()
	{
		this.frontView = new FrameLayout(this.c);
		this.frontImage = new ImageView(this.c);
		switch(this.carType)
		{
			case 0:
				frontImage.setImageResource(R.drawable.frontcars);
				break;
			case 1:
				frontImage.setImageResource(R.drawable.frontbikes);
				break;
			case 2:
				frontImage.setImageResource(R.drawable.fronttrucks);
				break;
			case 3:
				frontImage.setImageResource(R.drawable.frontbuss);
				break;
		}
	
		this.addView(this.frontView);
		this.frontView.getLayoutParams().width=(int) (55 * density);
		this.frontView.getLayoutParams().height = (int) (55 * density);
	//	this.frontView.setX(15);
		this.frontView.addView(this.frontImage);
		this.frontImage.getLayoutParams().width=(int) (55 * density);
		this.frontImage.getLayoutParams().height =(int) (55 * density);
		
		Log.d("test", "1");
	}
	
	
	public void addBackView()
	{
		this.backView = new FrameLayout(this.c);
		this.backImage = new ImageView(this.c);
		switch(this.carType)
		{
			case 0:
				backImage.setImageResource(R.drawable.backcars);
				break;
			case 1:
				backImage.setImageResource(R.drawable.backbikes);
				break;
			case 2:
				backImage.setImageResource(R.drawable.backtrucks);
				break;
			case 3:
				backImage.setImageResource(R.drawable.backbuss);
				break;
		}
	
		this.addView(this.backView);
		this.backView.getLayoutParams().width=(int) (55 * density);
		this.backView.getLayoutParams().height = (int) (55 * density);
	//	this.frontView.setX(15);
		this.backView.addView(this.backImage);
		this.backImage.getLayoutParams().width=(int) (55 * density);
		this.backImage.getLayoutParams().height =(int) (55 * density);
		
		if(this.frontView != null) {
			this.backView.setAlpha(0f);
		}
		
		
	}
	
	
	public void setAnimationLoop()
	{
		

		r = new Runnable()
		{
		    public void run() 
		    {
		        startAnimation();
		        handler.postDelayed(this, 5000);
		    }
		};

		handler.postDelayed(r, 1000);

		
	}
	
	
	
	public void startAnimation(){
		
		Log.d("numviews", Integer.toString(numberofviews));
		
		final View front = frontView;
		final View back = backView;
		animrunning = true;
		@SuppressWarnings("unused")
		int counter = 0;
		
		
		
			Log.d("anim", "1");
		//	back.setAlpha(0f);
			back.setVisibility(View.VISIBLE);
			
			//if we are going to animate to back
			Float frontf = 1f;
			Float backf = 0f;
			
			if(frontIsShowing)
			{
				frontIsShowing= false;
				frontf = 0f;
				backf =1f;
				
			}
			else
			{
				frontIsShowing = true;
				 frontf = 1f;
				 backf = 0f;
			}
			
			back.animate()
				.alpha(backf)
				.setDuration(3000)
				.setListener(null);
			
			front.animate()
				.alpha(frontf)
				.setDuration(3000)
				.setListener(null);

			
			
			counter++;
			
		
		
	}
	
	
	public void stopAnimation()
	{
		animrunning = false;
		if(r!= null)
		{
			handler.removeCallbacks(r);
			r = null;
		}
	}
	
	
	
	public void removeMe()
	{
		
	}
	
	public List<int[]> getPositionsForType(int vType) {
		
		
		List<int[]> positions = new ArrayList<int[]>();
		int[] position = null;
		
		switch(vType) {
		case 0:
			
			
			position = new int[] {3, 25, 7, 7};
			positions.add(position);
			position = new int[] {3, 33, 6, 6};
			positions.add(position);
			position = new int[] {45, 25, 7, 7};
			positions.add(position);
			position = new int[] {46, 33, 6, 6};
			positions.add(position);
			position = new int[] {4, 25, 7, 7};
			positions.add(position);
			position = new int[] {1, 32, 6, 6};
			positions.add(position);
			position = new int[] {22, 2, 12, 2};
			positions.add(position);
			position = new int[] {20, 40, 15, 5};
			positions.add(position);
			position = new int[] {44, 25, 7, 7};
			positions.add(position);
			position = new int[] {48, 32, 6, 6};
			positions.add(position);
			break;
		case 1:
			
			
			position = new int[] {23, 4, 8, 9};
			positions.add(position);
			position = new int[] {16, 38, 4, 4};
			positions.add(position);
			position = new int[] {22, 34, 11, 6};
			positions.add(position);
			position = new int[] {34, 38, 4, 4};
			positions.add(position);
			break;
		case 2:
			position = new int[] {15, 36, 7, 5};
			positions.add(position);
			position = new int[] {33, 36, 7, 5};
			positions.add(position);
			position = new int[] {10, 2, 3, 3};
			positions.add(position);
			position = new int[] {9, 30, 5, 5};
			positions.add(position);
			position = new int[] {9, 35, 5, 5};
			positions.add(position);
			position = new int[] {22, 1, 12, 3};
			positions.add(position);
			position = new int[] {42, 2, 3, 3};
			positions.add(position);
			position = new int[] {41, 30, 5, 5};
			positions.add(position);
			position = new int[] {41, 35, 5, 5};
			positions.add(position);
			break;
		case 3:
			position = new int[] {13, 1, 3, 3};
			positions.add(position);
			position = new int[] {10, 41, 5, 4};
			positions.add(position);
			position = new int[] {37, 1, 3, 3};
			positions.add(position);
			position = new int[] {39, 41, 5, 4};
			positions.add(position);
			position = new int[] {8, 4, 4, 4};
			positions.add(position);
			position = new int[] {8, 32, 5, 5};
			positions.add(position);
			position = new int[] {13, 32, 5, 5};
			positions.add(position);
			position = new int[] {20, 0, 15, 2};
			positions.add(position);
			position = new int[] {24, 26, 7, 4};
			positions.add(position);
			position = new int[] {42, 4, 4, 4};
			positions.add(position);
			position = new int[] {36, 32, 5, 5};
			positions.add(position);
			position = new int[] {42, 32, 5, 5};
			positions.add(position);
			
			break;
		
		
		
		}
		
		
		
		return positions;
	}
	
	public List<String> getLabelsForType(int vType) {
		
		List<String> labels = new ArrayList<String>();
		String[] label = null;
		
		switch(vType) {
		case 0:
			label = new String[] {"Front Right Headlight", "Front Right Fog Light", "Front Left Headlight", "Front Left Fog Light", "Back Left Brake Light", "Back Left Tail Light", "Back Center Brake Light", "Back License Plate Light", "Back Right Brake Light", "Back Right Tail Light"};
			
			break;
		case 1:
			label = new String[] {"Front Headlight", "Back Left Turn Signal", "Back Brake Light", "Back Right Turn Signal"};
			
			break;
		case 2:
			label = new String[] {"Front Right Headlight", "Front Left Headlight", "Back Left Marker Light", "Back Left Tail Light", "Back Left Brake Light", "Back Center Marker Light", "Back Right Marker Light", "Back Right Tail Light", "Back Right Brake Light"};
			break;
		case 3:
			label = new String[] {"Front Right Marker Light", "Front Right Headlight", "Front Left Marker Light", "Front Left Headlight", "Back Left Marker Light", "Back Left Brake Light", "Back Left Tail Light", "Back Center Marker Light", "Back Center Brake Light", "Back Right Marker Light", "Back Right Tail Light", "Back Right Brake Light"};
			break;
		}
		
		for(int i=0; i<label.length; i++) {
			labels.add(label[i]);
		}
		
		return labels;
		
		
		
	}
	
	public List<Integer> getColorsForType(int vType) {
		
		List<Integer> colors = new ArrayList<Integer>();
		int[] vehicleColors = null;
		
		int red = Color.parseColor("#ff0000");
		int yellow = Color.parseColor("#f1e472");
		int amber = Color.parseColor("#ffbf00");
		
		switch(vType) {
		case 0:
			vehicleColors = new int[]{yellow, yellow, yellow, yellow, red, red, red, yellow, red, red};
			break;
			
		case 1:
			vehicleColors = new int[] {yellow, amber, red, amber};
			break;
			
		case 2:
			vehicleColors = new int[] {yellow, yellow, red, red, red, red, red, red, red};
			break;
			
		case 3:
			vehicleColors = new int[] {amber, yellow, amber, yellow, red, red, red, red, red, red, red, red};
			break;
		
		}
		
		for(int i=0; i<vehicleColors.length; i++) {
			
			colors.add(vehicleColors[i]);
		}
		
		
		
		
		return colors;
	}
	
	public List<FrameLayout> getFramesForType(int vType) {
		
		List<FrameLayout> frames = new ArrayList<FrameLayout>();
		int[] vehicleFrames = null;
		
		switch(vType) {
		case 0:
			vehicleFrames = new int[] {0, 0, 0, 0, 1, 1, 1, 1, 1, 1 };
			break;
		case 1:
			vehicleFrames = new int[] {0, 1, 1, 1};
			break;
		case 2:
			vehicleFrames = new int[] {0, 0, 1, 1, 1, 1, 1, 1, 1};
			break;
		case 3:
			vehicleFrames = new int[] {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1};
			break;
		}
		
		for(int i=0; i<vehicleFrames.length; i++) {
			
			if(vehicleFrames[i] == 0) {
				frames.add(frontView);
			}
			else {
				frames.add(backView);
			}
		}
		
		
		
		return frames;
		
	}
	
public List<Integer> getFrames(int vType) {
		
		List<Integer> frames = new ArrayList<Integer>();
		int[] vehicleFrames = null;
		
		switch(vType) {
		case 0:
			vehicleFrames = new int[] {0, 0, 0, 0, 1, 1, 1, 1, 1, 1 };
			break;
		case 1:
			vehicleFrames = new int[] {0, 1, 1, 1};
			break;
		case 2:
			vehicleFrames = new int[] {0, 0, 1, 1, 1, 1, 1, 1, 1};
			break;
		case 3:
			vehicleFrames = new int[] {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1};
			break;
		}
		
		for(int i=0; i<vehicleFrames.length; i++) {
			
			frames.add(vehicleFrames[i]);
		}
		
		
		
		return frames;
		
	}

public void setCommunicator(Communicator c) {
	this.comm = c;
}

public interface Communicator {
	
	public void setHandler(Handler h, Runnable r);
}
}

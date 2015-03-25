package com.burntout.burntout;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


@SuppressLint("NewApi")
public class tappableManager extends FrameLayout implements TappableLight.Communicator {
	
	
	Communicator comm;
	Context context;
	ArrayList<TappableLight>brokenLights;
	ArrayList<String>brokenLightLabels;
	ArrayList<String>brokenLightViewedLabels;
	ArrayList<String>actualBrokenLights;
	ArrayList<String>actualViewedBrokenLights;
	
	
	ImageView frontImage;
	ImageView backImage;
	int frontimg;
	int backimg;
	
	int type;
	
	
	public tappableManager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	 
	}

	public tappableManager(Context context, AttributeSet attrs) {
		super(context, attrs);
		

	}

	public tappableManager(Context context) {
		super(context);
		
	}
	
	@SuppressLint("NewApi") 
	public void initialSetUp(Context c, int type, int frontImg, int backImg)
	{
		
		context = c;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		
		int screenMidPt = metrics.widthPixels/2;
		int densityDpi = (int)(metrics.density );
		
		this.brokenLights = new ArrayList<TappableLight>();
		this.brokenLightLabels = new ArrayList<String>();
		this.brokenLightViewedLabels = new ArrayList<String>();
		this.actualBrokenLights = new ArrayList<String>();
		this.actualViewedBrokenLights = new ArrayList<String>();
		
		
		int red = Color.parseColor("#ff0000");
		int yellow = Color.parseColor("#f1e472");
		int amber = Color.parseColor("#ffbf00");
		
		int sizew = 0;
		int sizeh =150;
		if(type == 0)
		{
			sizew =150; 
		}
		else if(type == 2)
		{
			sizew = 110;
		}
		else
		{
			sizew = 120;
		}
		
		int imgShift = (screenMidPt + (10 * densityDpi));
		int frontPad = (screenMidPt - (sizew * densityDpi) - (10 * densityDpi));
		if(type == 0)
		{
			int [] posx = {9 * densityDpi + frontPad, 10 * densityDpi + frontPad, 123 * densityDpi + frontPad, 126  * densityDpi + frontPad, imgShift + 11 * densityDpi, imgShift + 2  * densityDpi, imgShift + 60  * densityDpi, imgShift + 57 * densityDpi, imgShift + 118 * densityDpi, imgShift + 132 * densityDpi};
			int []posy= {69 * densityDpi, 91 * densityDpi, 68 * densityDpi, 91 * densityDpi, 68 * densityDpi, 88 * densityDpi, 3 * densityDpi, 109 * densityDpi, 67 * densityDpi, 88 * densityDpi};
			int [] w = {19 * densityDpi, 15 * densityDpi, 20 * densityDpi, 16 * densityDpi, 19 * densityDpi, 16 * densityDpi, 30 * densityDpi, 36 * densityDpi, 20 * densityDpi, 16 * densityDpi};
			int [] h = {19 * densityDpi, 17 * densityDpi, 20 * densityDpi, 17 * densityDpi, 19 * densityDpi, 16 * densityDpi, 7 * densityDpi, 14 * densityDpi, 20 * densityDpi, 16 * densityDpi};
			int [] onColor = {yellow, yellow, yellow, yellow, red, red, red, yellow, red, red};
			String[] labels = {"Front Right Headlight", "Front Right Fog Light", "Front Left Headlight", "Front Left Fog Light", "Back Left Brake Light", "Back Left Tail Light", "Back Center Brake Light", "Back License Plate Light", "Back Right Brake Light", "Back Right Tail Light"};
			String[] viewedLabels = {"Right Headlight", "Right Fog Light", "Left Headlight", "Left Fog Light", "Left Brake Light", "Left Tail Light", "Center Brake Light", "License Plate Light", "Right Brake Light", "Right Tail Light"};
			
			
			for(int i=0; i<labels.length; i++) {
				this.brokenLightLabels.add(labels[i]);
				this.brokenLightViewedLabels.add(viewedLabels[i]);
			}
			
			createTappableViews(posx,posy,h,w,onColor, labels, viewedLabels);
		}
		else if(type == 1)
		{
			int [] posx = {49 * densityDpi + frontPad,  28 * densityDpi + imgShift, 44 * densityDpi + imgShift, 80 * densityDpi + imgShift};
			int []posy= {11 * densityDpi, 105 * densityDpi, 92 * densityDpi, 104 * densityDpi};
			int [] w = {22 * densityDpi, 12 * densityDpi, 30 * densityDpi, 12 * densityDpi};
			int [] h  = {24 * densityDpi, 13 * densityDpi, 16 * densityDpi, 13 * densityDpi};
			int []onColor = {yellow, amber, red, amber};
			String[] labels = {"Front Headlight", "Back Left Turn Signal", "Back Brake Light", "Back Right Turn Signal"};
			String[] viewedLabels = {"Headlight", "Left Turn Signal", "Brake Light", "Right Turn Signal"};
			
			for(int i=0; i<labels.length; i++) {
				brokenLightLabels.add(labels[i]);
				brokenLightViewedLabels.add(viewedLabels[i]);
			}
			
			createTappableViews(posx,posy,h,w,onColor, labels, viewedLabels);
		}
		else if(type == 2)
		{
			int [] posx = {17 * densityDpi + frontPad, 70 * densityDpi + frontPad, 6 * densityDpi + imgShift, 3 * densityDpi + imgShift, 3 * densityDpi + imgShift, 38 * densityDpi + imgShift, 95 * densityDpi + imgShift, 94 * densityDpi + imgShift, 94 * densityDpi + imgShift};
			int []posy= {99 * densityDpi, 99 * densityDpi, 5 * densityDpi, 82 * densityDpi, 96 * densityDpi, 3 * densityDpi, 5 * densityDpi, 82 * densityDpi, 96 * densityDpi};
			int [] w = {24 * densityDpi, 24 * densityDpi, 9 * densityDpi, 13 * densityDpi, 13 * densityDpi, 34 * densityDpi, 9 * densityDpi, 13 * densityDpi, 13 * densityDpi};
			int [] h  = {12 * densityDpi, 12 * densityDpi, 8 * densityDpi, 14 * densityDpi, 14 * densityDpi, 6 * densityDpi, 9 * densityDpi, 14 * densityDpi, 14 * densityDpi};
			int []onColor = {yellow, yellow, red, red, red, red, red, red, red};
			String[] labels = {"Front Right Headlight", "Front Left Headlight", "Back Left Marker Light", "Back Left Tail Light", "Back Left Brake Light", "Back Center Marker Light", "Back Right Marker Light", "Back Right Tail Light", "Back Right Brake Light"};
			String[] viewedLabels = {"Right Headlight", "Left Headlight", "Left Marker Light", "Left Tail Light", "Left Brake Light", "Center Marker Light", "Right Marker Light", "Right Tail Light", "Right Brake Light"};
			
			
			for(int i=0; i<labels.length; i++) {
				this.brokenLightLabels.add(labels[i]);
				this.brokenLightViewedLabels.add(viewedLabels[i]);
			}
			
			
			
			createTappableViews(posx,posy,h,w,onColor, labels, viewedLabels);
		}
		else
		{
			int [] posx = {18 * densityDpi + frontPad, 10 * densityDpi + frontPad, 88 * densityDpi + frontPad, 96 * densityDpi + frontPad, 5 * densityDpi + imgShift, 3 * densityDpi + imgShift, 19 * densityDpi + imgShift, 41 * densityDpi + imgShift, 51 * densityDpi + imgShift, 103 * densityDpi + imgShift, 88 * densityDpi + imgShift, 104 * densityDpi + imgShift};
			int []posy= {2 * densityDpi, 113 * densityDpi, 2 * densityDpi, 113 * densityDpi, 11 * densityDpi, 89 * densityDpi, 89 * densityDpi, 1 * densityDpi, 70 * densityDpi, 11 * densityDpi, 89 * densityDpi, 89 * densityDpi};
			int [] w = {12 * densityDpi, 15 * densityDpi, 12 * densityDpi, 15 * densityDpi, 12 * densityDpi, 12 * densityDpi, 12 * densityDpi, 38 * densityDpi, 18 * densityDpi, 12 * densityDpi, 13 * densityDpi, 13 * densityDpi};
			int [] h  = {9 * densityDpi, 10 * densityDpi, 9 * densityDpi, 10 * densityDpi, 13 * densityDpi, 13 * densityDpi, 13 * densityDpi, 6 * densityDpi, 10 * densityDpi, 13 * densityDpi, 13 * densityDpi, 13 * densityDpi};
			int []onColor = {amber, yellow, amber, yellow, red, red, red, red, red, red, red, red};
			String[] labels = {"Front Right Marker Light", "Front Right Headlight", "Front Left Marker Light", "Front Left Headlight", "Back Left Marker Light", "Back Left Brake Light", "Back Left Tail Light", "Back Center Marker Light", "Back Center Brake Light", "Back Right Marker Light", "Back Right Tail Light", "Back Right Brake Light"};
			String[] viewedLabels = {"Right Marker Light", "Right Headlight", "Left Marker Light", "Left Headlight", "Left Marker Light", "Left Brake Light", "Left Tail Light", "Center Marker Light", "Center Brake Light", "Right Marker Light", "Right Tail Light", "Right Brake Light"};
			
			for(int i=0; i<labels.length; i++) {
				this.brokenLightLabels.add(labels[i]);
				this.brokenLightViewedLabels.add(viewedLabels[i]);
			}
			
			createTappableViews(posx,posy,h,w,onColor, labels, viewedLabels);
		}
		
		
		Log.d("array", brokenLightLabels.toString());
		
		
		
		
		frontImage = new ImageView(c);
		
		this.addView(frontImage);
		this.frontImage.getLayoutParams().width = sizew * densityDpi;
		this.frontImage.getLayoutParams().height = sizeh * densityDpi;
		
		this.frontImage.setX((screenMidPt) - (this.frontImage.getLayoutParams().width + (10 * densityDpi)));
		this.frontImage.setScaleType(ScaleType.FIT_CENTER);
		
		backImage = new ImageView(c);
		this.addView(backImage);
			
		this.backImage.getLayoutParams().width = sizew * densityDpi;
		this.backImage.getLayoutParams().height = sizeh * densityDpi;
		
		this.backImage.setX(screenMidPt + (10 * densityDpi));
		this.type = type;
		this.backImage.setScaleType(ScaleType.FIT_CENTER);
		this.frontimg = frontImg;
		this.backimg = backImg;
		
		
		
		
		
		this.comm.getReports(actualBrokenLights, actualViewedBrokenLights);
		
		
	}
	
	
	
	public void setImages()
	{
		this.frontImage.setBackgroundResource(this.frontimg);
		this.backImage.setBackgroundResource(this.backimg);
	}
	
	
	@SuppressLint("NewApi") public void destroyImages()
	{
		this.frontImage.setBackground(null);
		this.backImage.setBackground(null);
	}
	
	
	public void resetLights()
	{
		this.actualBrokenLights.clear();
		this.actualViewedBrokenLights.clear();
		
		for (int i =0;i<brokenLights.size();i++)
		{
			TappableLight t = brokenLights.get(i);
			t.setOn();
		}
		
	}
	
	
	public void turnLightsOff()
	{
		for (int i =0;i<brokenLights.size();i++)
		{
			TappableLight t = brokenLights.get(i);
			t.setOff();
		}
		
	}	
	
	@SuppressLint("NewApi")
	public void createTappableViews(int[] posx,int[] posy,int[] h,int[] w, int[] onColor, String[] labels, String[] viewedLabels)
	{
		for(int i=0; i<posx.length; i++) {
			TappableLight t = new TappableLight(context);
			t.setCommunicator(this);
			
			this.brokenLights.add(t);
			
			t.initData(posx[i], posy[i], w[i], h[i], labels[i], viewedLabels[i], i);
			t.setOnColor(onColor[i]);
			
			this.addView(t);
			t.setLayoutParams(new LayoutParams(w[i], h[i]));
			t.setX(posx[i]);
			t.setY(posy[i]);
			
			
		
			
			
		}
		
	}
	
	public void setCommunicator(Communicator c) {
		
		this.comm = c;
	}

	@Override
	public void addLightToList(int position) {
		// TODO Auto-generated method stub
		
		
		this.actualBrokenLights.add(brokenLightLabels.get(position));
		this.actualViewedBrokenLights.add(brokenLightViewedLabels.get(position));
		comm.getReports(actualBrokenLights, actualViewedBrokenLights);
				
	}

	@Override
	public void deleteLightFromList(int position) {
		// TODO Auto-generated method stub
		
		this.actualBrokenLights.remove(brokenLightLabels.get(position));
		this.actualViewedBrokenLights.remove(brokenLightViewedLabels.get(position));
		comm.getReports(actualBrokenLights, actualViewedBrokenLights);
		
	}
	
	
	public interface Communicator {
		
		public void getReports(ArrayList<String> brokenLights, ArrayList<String> viewedBrokenLights);
	}
	
	
	
		
	
}

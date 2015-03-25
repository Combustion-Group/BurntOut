package com.burntout.burntout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.view.View;
import android.view.View.OnClickListener;




public class TappableLight extends FrameLayout implements OnClickListener {
	final int offColor = Color.parseColor("#00000000");
	int onColor;
	boolean isOn;
	int x, y, w, h, position;
	String label, viewedLabel;
	Communicator comm;
	
	public TappableLight(Context context, AttributeSet attrs, int defStyle) {
	
		super(context, attrs, defStyle);
		this.isOn  = true;
	}

	public TappableLight(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.isOn  = true;
	}

	public TappableLight(Context context) {
		super(context);
		this.isOn  = true;
		this.setOnClickListener(this);
	}
	
	
	public void setOnColor(int color)
	{
		this.onColor = color;
		
	}
	
	
	public void setOn()
	{
		this.isOn  = true;
		this.setBackgroundColor(this.onColor);
	}
	
	public void setOff()
	{
		this.isOn  = false;
		this.setBackgroundColor(this.offColor);
	}
	
	public void initData(int x, int y, int w, int h, String label, String viewedLabel, int position) {
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.label = label;
		this.viewedLabel = viewedLabel;
		this.position = position;
	}


	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d("clicked", "clicked");
		if(this.isOn)
		{
			setOff();
			comm.addLightToList(this.position);
			
		}
		else
		{
			setOn();
			comm.deleteLightFromList(this.position);
			
		}
		//
		//wasTapped(this.isOn);
	}
	
	
	
	public void wasTapped(boolean isLightOn)
	{
		
//if the light is now turned off you are going to return false to the manager + the position so you can remove the string array of 
		//visible strings in that array and then re render all the strings that are currently available in the array
		
		//if the light is then set to on, do the opposite, add it to the array 
		if(isLightOn) {
			comm.addLightToList(position);
		}
		else {
			comm.deleteLightFromList(position);
		}
		
	}
	
	

	public void setCommunicator(Communicator c) {
		this.comm = c;
	}

	public interface Communicator {
		
		public void addLightToList(int position);
		public void deleteLightFromList(int position);
	}
	
}

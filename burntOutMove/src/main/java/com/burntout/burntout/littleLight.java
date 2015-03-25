package com.burntout.burntout;

import android.content.Context;
import android.util.AttributeSet;

import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class littleLight extends RelativeLayout{
	int myColor;
	int posX;
	int posY;
	int h;
	int w;
	FrameLayout parentView;
	
	
	
	
	public littleLight(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	 
	}

	public littleLight(Context context, AttributeSet attrs) {
		super(context, attrs);
		

	}

	public littleLight(Context context) {
		super(context);
		
	}
	
	
	public void addLight(int posX, int posY, int h, int w, int color, FrameLayout parentView)
	{
		this.myColor = color;
		this.parentView = parentView;
		this.posX = posX;
		this.posY = posY;
		this.h = h;
		this.w = w;
		
		this.setBackgroundColor(this.myColor);
		this.setX(this.posX);
		this.setY(this.posY);
		this.parentView.addView(this);
		this.getLayoutParams().height = this.h;
		this.getLayoutParams().width = this.w;
		
	}
	
	
	
}

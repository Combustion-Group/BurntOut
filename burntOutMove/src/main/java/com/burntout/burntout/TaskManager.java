package com.burntout.burntout;


import android.view.View;


public class TaskManager {
	
	public boolean clickable;
	
	public View view;
	
	public TaskManager(View v) {
		
		this.view = v;
		this.clickable = true;
		
	}
	
	
	public void setClickable() {
		this.clickable = true;
		this.view.setClickable(true);
		
	}
	
	public void unsetClickable() {
		this.clickable = false;
		this.view.setClickable(false);
	}

	
	
	
	
}

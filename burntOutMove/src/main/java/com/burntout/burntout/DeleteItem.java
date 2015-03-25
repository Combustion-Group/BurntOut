package com.burntout.burntout;


import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class DeleteItem extends RelativeLayout {

	
	public DeleteItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		   LayoutInflater inflater = (LayoutInflater) context
		            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        inflater.inflate(R.layout.deleteitem, this, true);
	}
	
	public DeleteItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	       LayoutInflater inflater = (LayoutInflater) context
		            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        inflater.inflate(R.layout.deleteitem, this, true);
	}

}

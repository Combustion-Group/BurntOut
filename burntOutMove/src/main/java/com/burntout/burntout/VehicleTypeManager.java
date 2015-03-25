package com.burntout.burntout;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class VehicleTypeManager extends LinearLayout {
	
	Context context;
	
	
	
	ImageView frontImage;
	ImageView backImage;
	TextView vehicleText;
	String vtype;
	int frontimg;
	int backimg;
	
	int type;
	
	
	public VehicleTypeManager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	 
	}

	public VehicleTypeManager(Context context, AttributeSet attrs) {
		super(context, attrs);
		

	}

	public VehicleTypeManager(Context context) {
		super(context);
		
	}
	
	@SuppressWarnings({ "unused", "deprecation" })
	@SuppressLint("NewApi") 
	public void initialSetUp(Context c, int type, int frontImg, int backImg)
	{
		
		context = c;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		
		int screenMidPt = metrics.widthPixels/2;
		int densityDpi = (int)(metrics.density );
		
		
		
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
		
		
		if(type == 0)
		{
			vtype = "Car";
		}
		else if(type == 1)
		{
			vtype = "Bike";
		}
		else if(type == 2)
		{
			vtype = "Truck";
		}
		else
		{
			vtype = "Bus";
		}
		
		
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		
		
		frontImage = new ImageView(c);
		
		this.addView(frontImage);
		this.frontImage.getLayoutParams().width = sizew * densityDpi;
		this.frontImage.getLayoutParams().height = sizeh * densityDpi;
		
		vehicleText = new TextView(c);
		vehicleText.setGravity(Gravity.CENTER);
		vehicleText.setTextColor(Color.WHITE);
		vehicleText.setText(Html.fromHtml("<b><i><u>" + vtype + "</u></i></b>"));
		this.addView(vehicleText);
		
		
		
		this.type = type;
		
		this.frontimg = frontImg;
		
	
		
		
	}
	
	
	
	public void setImages()
	{
		this.frontImage.setBackgroundResource(this.frontimg);
		
	}
	
	
	@SuppressLint("NewApi") public void destroyImages()
	{
		this.frontImage.setBackground(null);
		
	}
	
	
	public void resetLights()
	{
		
		
	}
	
	
	public void turnLightsOff()
	{
		
		
	}	
	
	
	
	
	
		
	
}

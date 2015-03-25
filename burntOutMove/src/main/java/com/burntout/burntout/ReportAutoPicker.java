package com.burntout.burntout;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;







public class ReportAutoPicker extends ArrayAdapter<RAPItem> {

	Resources res;
	
	Context context;
 	RAPItem rowItem;
	double height;
	double width;
	int adjustedwidth;
	int iconwidth;
	int iconwidthtwo;
	int iconwidththree;
	int pos;
	
	
	
	
	

	
public ReportAutoPicker(Context context, int resourceId, List<RAPItem> rIDs) {
    super(context, resourceId, rIDs);
     this.context = context;

     height =  context.getResources().getDisplayMetrics().heightPixels;
     width =  context.getResources().getDisplayMetrics().widthPixels;
    adjustedwidth = (int) (width * .9);
    iconwidth =  (int) (width * .275);
    iconwidthtwo =  (int) (width * .25);
    iconwidththree = (int) (width * .30);
    
    
    
    
    
    
    //Resources res = context.getResources();
    /*
    carpic = res.getDrawable(R.drawable.frontcar_2x);
    bikepic = res.getDrawable(R.drawable.frontcar_2x);
    truckpic = res.getDrawable(R.drawable.frontcar_2x);
    buspic = res.getDrawable(R.drawable.frontcar_2x);
    */
    /*
    carpic = res.getDrawable(R.drawable.fronttruck_2x);
	bikepic = res.getDrawable(R.drawable.frontbike_2x);
	truckpic = res.getDrawable(R.drawable.frontcar_2x);
	buspic = res.getDrawable(R.drawable.frontbus_2x);
	*/
	
	 
}




/*private view holder class*/
private class ViewHolder {

	@SuppressWarnings("unused")
	RelativeLayout listItem;
	ImageView rapFrontPic, rapBackPic;
	
	
	
}
public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder = null;
    rowItem = getItem(position);
  
    pos = position;
  
    
    
    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    

	   if (convertView == null) {
    		convertView = mInflater.inflate(R.layout.report_autopicker_item,parent,  false);
    	   	   holder = new ViewHolder();
    	  
    	   		holder.rapFrontPic= (ImageView) convertView.findViewById(R.id.rap_frontpic);
    	   		holder.rapBackPic= (ImageView) convertView.findViewById(R.id.rap_backpic);
    	   		
    	   		    			  
    		   convertView.setTag(holder);
	   } else{
		        holder = (ViewHolder) convertView.getTag();
		    
    			
	   }
	   
	   
	     
	   
	   holder.rapFrontPic.setImageResource(rowItem.getFrontrID());
	   holder.rapBackPic.setImageResource(rowItem.getBackrID());
	   
	   
	   


	 //holder.story_holder.setText("@");
	 
   return convertView;
}

public int getPosition() {
	return pos;
}








		
		
}


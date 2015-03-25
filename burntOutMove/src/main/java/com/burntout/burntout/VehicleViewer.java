package com.burntout.burntout;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;






public class VehicleViewer extends ArrayAdapter<Vehicle> implements Post.Communicator {

	Resources res;
	
	Context context;
 	Vehicle rowItem;
	double height;
	double width;
	int adjustedwidth;
	int iconwidth;
	int iconwidthtwo;
	int iconwidththree;
	Communicator comm;
	
	
	public int[] rIDs;
	

	
public VehicleViewer(Context context, int resourceId, List<Vehicle> items) {
    super(context, resourceId, items);
     this.context = context;

     height =  context.getResources().getDisplayMetrics().heightPixels;
     width =  context.getResources().getDisplayMetrics().widthPixels;
    adjustedwidth = (int) (width * .9);
    iconwidth =  (int) (width * .275);
    iconwidthtwo =  (int) (width * .25);
    iconwidththree = (int) (width * .30);
    
    rIDs = new int[4];
    
    rIDs[0] = R.drawable.frontcars;
	rIDs[1] = R.drawable.frontbikes;
	rIDs[2] = R.drawable.fronttrucks;
	rIDs[3] = R.drawable.frontbuss;
    
    
    
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
	ImageView carImage;
	TextView carModel, carPlate;
	ImageButton editButton;
	
}
public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder = null;
    rowItem = getItem(position);
  
  
    
    
    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    

	   if (convertView == null) {
    		convertView = mInflater.inflate(R.layout.view_cars_item,parent,  false);
    	   	   holder = new ViewHolder();
    	  
    	   		holder.carModel= (TextView) convertView.findViewById(R.id.viewvehicles_makemodel);
    	   		holder.carPlate= (TextView) convertView.findViewById(R.id.viewvehicles_plate);
    	   		holder.carImage = (ImageView)convertView.findViewById(R.id.viewvehicles_vtype);
    	   		holder.editButton = (ImageButton)convertView.findViewById(R.id.edit_button);
    	   		
    	   		
    
    
  
    	
    			  
    		   convertView.setTag(holder);
	   } else{
		        holder = (ViewHolder) convertView.getTag();
		    
    			
	   }
	   
	   

	   
	   holder.carModel.setText(rowItem.getCarModel() + " | ");
	   holder.carPlate.setText(rowItem.getPlateNumber());
	   int vType = Integer.parseInt(rowItem.getVehicleTypeId());
	   
	   
		   holder.carImage.setImageResource(rIDs[vType]);

	   final int line = position;
	   
	   holder.editButton.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CharSequence editOptions[] = new CharSequence[] {"Edit", "Delete", "Cancel"};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Edit/Delete Vehicle");
			
			builder.setItems(editOptions, new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        // the user clicked on colors[which]
			    	if(which == 0)
			    	{
			    		comm.editThisVehicle(rowItem, line);
			    	}
			    	else if(which == 1)
			    	{
			  	
			    		comm.deleteThisVehicle(rowItem, line);
			    	}
			    	else
			    	{
			    		
			    	}
			    }
			});
			builder.show();
			
		}
	});
	   


	 //holder.story_holder.setText("@");
	 
   return convertView;
}



@Override
public void gotResponse(JSONObject s) {
	// TODO Auto-generated method stub
	
}

public interface Communicator {
	
	public void editThisVehicle(Vehicle rowItem, int position);
	public void deleteThisVehicle(Vehicle rowItem, int position);
}

public void setCommunicator(Communicator c) {
	comm = c;
}




		
}


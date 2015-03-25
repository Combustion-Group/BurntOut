package com.burntout.burntout;





import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;






public class DeleteVehiclesArrayAdapter extends ArrayAdapter<Vehicle> {

	Resources res;
	Communicator comm;
	Context context;
 	Vehicle rowItem;
	double height;
	double width;
	int adjustedwidth;
	int iconwidth;
	int iconwidthtwo;
	int iconwidththree;
	
	
	public int[] rIDs;
	

	
public DeleteVehiclesArrayAdapter(Context context, int resourceId, List<Vehicle> items) {
    super(context, resourceId, items);
     this.context = context;

     height =  context.getResources().getDisplayMetrics().heightPixels;
     width =  context.getResources().getDisplayMetrics().widthPixels;
    adjustedwidth = (int) (width * .9);
    iconwidth =  (int) (width * .275);
    iconwidthtwo =  (int) (width * .25);
    iconwidththree = (int) (width * .30);
    
    rIDs = new int[4];
    
    rIDs[0] = R.drawable.frontcar_2x;
	rIDs[1] = R.drawable.frontbike_2x;
	rIDs[2] = R.drawable.fronttruck_2x;
	rIDs[3] = R.drawable.frontbus_2x;
    
    
    
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
	Button deleteBtn;
}
public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder = null;
   rowItem = getItem(position);
  
   
    
    
    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    

	   if (convertView == null) {
    		convertView = mInflater.inflate(R.layout.deleteitem,parent,  false);
    	   	   holder = new ViewHolder();
    	  
    	   		holder.carModel= (TextView) convertView.findViewById(R.id.deleteCarModel);
    	   		holder.carPlate= (TextView) convertView.findViewById(R.id.deleteCarPlate);
    	   		holder.carImage = (ImageView)convertView.findViewById(R.id.deleteCarType);
    	   		holder.deleteBtn = (Button)convertView.findViewById(R.id.deleteButton);
    	   		
    
    
  
    	
    			  
    		   convertView.setTag(holder);
	   } else{
		        holder = (ViewHolder) convertView.getTag();
		    
    			
	   }
	   
	   

	   
	   holder.carModel.setText(rowItem.getCarModel() + " | ");
	   holder.carPlate.setText(rowItem.getPlateNumber());
	   int vType = Integer.parseInt(rowItem.getVehicleTypeId());

	   holder.carImage.setImageResource(rIDs[vType]);
	   final int line = position;
	   holder.deleteBtn.setOnClickListener(new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String plate_number = rowItem.getPlateNumber();
			comm.deleteThisVehicle(plate_number, line);
			
		}
		   
		   
	   });


	 //holder.story_holder.setText("@");
	 Log.d("check", "check");
   return convertView;
}
public interface Communicator
{
	public void deleteThisVehicle(String plate_number, int pos);
}

public void setCommunicator(Communicator c) {
	comm = c;
}

		
		
}


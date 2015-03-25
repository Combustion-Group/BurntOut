package com.burntout.burntout;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;

import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NotificationsAdapter extends ArrayAdapter<Notification> implements Post.Communicator, carAnimator.Communicator {
	
	Context c;
	LayoutInflater mInflater;
	ProgressDialog pm;
	Post updateHelpful, updateWasRead;
	final carAnimator carAni;
	carAnimator fakeAni;
	ArrayList<Notification> myNotifications;
	Handler handler;
	Runnable runnable;
	Notification rowItem;
	
	
//	private final ImageDownloader mDownload = new ImageDownloader();
	
	
	ViewHolder holder;
	
	Communicator comm;
	
	
	int totalNotifications, thisNotification;
	int pos;

	public NotificationsAdapter(Context context, int resourceId, ArrayList<Notification> items) {
		super(context, resourceId, items);
		// TODO Auto-generated constructor stub
		c = context;
		
		fakeAni = null;
		carAni	= new carAnimator(c);
		
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		rowItem = getItem(position);
		thisNotification = position;
		
		pos = position;
		
		Log.d("pic", rowItem.getPicture());
		
		LayoutInflater mInflater = (LayoutInflater) c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			
			
			
	         convertView = mInflater.inflate(R.layout.notification ,parent,  false);
	         holder = new ViewHolder();
	        
	         handler = null;
	         runnable = null;
	         
	         
	         holder.hasReportedLights = (TextView)convertView.findViewById(R.id.has_reported_lights);
	 		 holder.reportedAgo = (TextView)convertView.findViewById(R.id.reported_ago);
	 		 holder.custom_message = (TextView)convertView.findViewById(R.id.custom_message);
	 		 holder.reporter_name = (TextView)convertView.findViewById(R.id.other_person_name);
	 		 holder.reporter_rank = (TextView)convertView.findViewById(R.id.other_person_rank);
	 		 holder.anio = (carAnimator)convertView.findViewById(R.id.carAnimator);
	 		holder.otherReportedCount = (TextView)convertView.findViewById(R.id.otherreportedcount);
	 		holder.otherRankingCount = (TextView)convertView.findViewById(R.id.otherrankingcount);
	 		holder.otherReceivedCount = (TextView)convertView.findViewById(R.id.otherrecievedcount);
	 		holder.reporter_rank = (TextView)convertView.findViewById(R.id.other_person_rank);
	 		holder.animatorHolder = (FrameLayout)convertView.findViewById(R.id.animatorHolder);
	 		
	 		
	 		holder.wasHelpful = (Button)convertView.findViewById(R.id.washelpful);
	 		holder.wasNotHelpful = (Button)convertView.findViewById(R.id.wasnothelpful);
	 		holder.dismiss = (Button)convertView.findViewById(R.id.dissmissbutton);
	 		holder.wasHelpfulLabel = (TextView)convertView.findViewById(R.id.was_helpful_label);
	 		holder.notification_plate = (TextView)convertView.findViewById(R.id.notification_plate);
	 		
	 	//	holder.carpic = (ImageView)convertView.findViewById(R.id.notification_carpic);
	 	//	holder.profpic = (ImageView)convertView.findViewById(R.id.profpics);
	 		holder.mypropic = (com.pkmmte.circularimageview.CircularImageView)convertView.findViewById(R.id.mypropic);
	 		holder.carHolder = (RelativeLayout)convertView.findViewById(R.id.car_holder);
	 		holder.personProPicHolder = (RelativeLayout)convertView.findViewById(R.id.personpropicholder);
	 		
	 	//	mDownload.download(rowItem.getPicture(), holder.profpic, null);
	 		
	 		holder.anio.setCommunicator(this);
		
	    	 convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();   
 			
    	}
		
		
		holder.anio.changeView(position);
		
		
		//carAni.attachView(holder.animatorHolder);
		holder.anio.addLights(rowItem.getLights_out(), Integer.parseInt(rowItem.getVehicle_type()));
		
	//	mDownload.download(rowItem.getPicture(), holder.profpic, null);
//
		String reportedLightsText = rowItem.getUser_fname() + " " + rowItem.getUser_lname().substring(0,1) + " has reported that your ";
		String underlinedLights = rowItem.getLights_out().replace("Front","").replace("Back","");
		SpannableString underLights = new SpannableString(underlinedLights);
		underLights.setSpan(new UnderlineSpan(), 0, underlinedLights.length(), 0);
		//underLights.setSpan(new StyleSpan(Typeface.ITALIC), 0, underLights.length(), 0);
		holder.hasReportedLights.setText(Html.fromHtml(reportedLightsText + "<u><i>" + underlinedLights + "</i></u>" + " has burnt out."));
		if(rowItem.getCreated().equals("now")) {
			holder.reportedAgo.setText("Just Now");
		}
		else {
			holder.reportedAgo.setText(rowItem.getCreated() + " Ago");
		}
		holder.custom_message.setText(rowItem.getMessage());
		holder.reporter_name.setText(rowItem.getUser_fname() + " " + rowItem.getUser_lname().substring(0,1));
		holder.otherReportedCount.setText(rowItem.getNotifier_reported_count());
		holder.otherRankingCount.setText(rowItem.getTheranking());
		holder.otherReceivedCount.setText(rowItem.getNotifier_reported_count());
		holder.notification_plate.setText(rowItem.getPlate_number());
		holder.reporter_rank.setText(rowItem.getNotifier_ranking());
		
		
		String vTypeString = rowItem.getVehicle_type();
		int vType = Integer.parseInt(vTypeString);
		
		switch(vType) {
		
		case 0:
		//	holder.carpic.setImageResource(R.drawable.frontcar_2x);
			break;
		case 1:
	//		holder.carpic.setImageResource(R.drawable.frontbike_2x);
			break;
		case 2:
	//		holder.carpic.setImageResource(R.drawable.fronttruck_2x);
			break;
		case 3:
		//	holder.carpic.setImageResource(R.drawable.frontbus_2x);
			break;
		
		}
		

		/*
		ImageView profPic = new ImageView(c);
		
		Bitmap bm = null;
 		try {
 	        URL aURL = new URL(rowItem.picture);
 	        URLConnection conn = aURL.openConnection();
 	        conn.connect();
 	        InputStream is = conn.getInputStream();
 	        BufferedInputStream bis = new BufferedInputStream(is);
 	        bm = BitmapFactory.decodeStream(bis);
 	         Drawable d = new BitmapDrawable(c.getResources(),bm);
 	        //r.setBackgroundDrawable(d);
 	        profPic.setImageDrawable(d);
 	        
 	        profPic.setBorderWidth(10);
 	        profPic.setBorderColor(c.getResources().getColor(R.color.button_blue));
 	        
 	        
 	        bis.close();
 	        is.close();

 	    } catch (Exception e) {
 	           Log.v("EXCEPTION", "Error getting bitmap", e);
 	    }

 		holder.personProPicHolder.addView(profPic);
 		*/
 		//build buttons
 		
 	//	holder.dismiss.setVisibility(View.INVISIBLE);
 		
		holder.dismiss.setVisibility(View.VISIBLE);
 	//	holder.wasHelpfulLabel.setVisibility(View.GONE);
		holder.wasHelpfulLabel.setText("");
 		final ViewHolder vholder = holder;
 		holder.wasHelpful.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vholder.wasNotHelpful.setEnabled(false);
				vholder.wasNotHelpful.setClickable(false);
				rowItem.setWasHelpful(1);
				
				rowItem.setWasRead(1);
				processNotification();
				
				
			}
		});
 		
 		holder.wasNotHelpful.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vholder.wasHelpful.setEnabled(false);
				vholder.wasHelpful.setClickable(false);
				rowItem.setWasHelpful(0);
		
				rowItem.setWasRead(1);
				processNotification();
				
			}
		});
 		
 		holder.dismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
				vholder.wasHelpfulLabel.setText("Was This Report Helpful?");
				vholder.dismiss.setVisibility(View.GONE);
				
				
			}
 			
 			
 		});
 		
 	String p = rowItem.getPicture();
 	if(p.length()>0)
 	{
 		Picasso.with(c).load(p).placeholder(R.drawable.profileplaceholder).error(R.drawable.profileplaceholder).transform(new RoundTransform()).into(holder.mypropic);
 	
 	}
 		
 	
 	fakeAni = holder.anio;
 	
 		return convertView;
		
		
	}
	
	public void switchButtons() {
		holder.wasHelpful.setVisibility(View.GONE);
		holder.wasNotHelpful.setVisibility(View.GONE);
		holder.dismiss.setVisibility(View.VISIBLE);
		
	}
	
	
	
	public void processNotification() {
		
		
		
		rowItem.setWasRead(1);
		
		updateHelpful = new Post();
		updateHelpful.setCommunicator(this);
				
		
		String notification_id = rowItem.getNotification_id();
		String was_help = Integer.toString(rowItem.getWasHelpful());
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("notification_id", notification_id));
		nameValuePairs.add(new BasicNameValuePair("was_help", was_help));
		
		updateHelpful.executePosts(nameValuePairs, "http://combustioninnovation.com/production/Goodyear/php/helpfulreport.php");
		
		comm.fixNotificationArray(pos);
		comm.setClickable();
		
		
	}

	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		
	}
	
	public void setCommunicator(Communicator c) {
		comm = c;
	}
	
	public class ViewHolder {
		com.pkmmte.circularimageview.CircularImageView mypropic;
		TextView hasReportedLights, reportedAgo, custom_message, reporter_name, reporter_rank;
		TextView otherReportedCount, otherRankingCount, otherReceivedCount, wasHelpfulLabel, notification_plate;
		ImageView carpic;
		ImageView profpic;
		Button wasHelpful, wasNotHelpful, dismiss;
		RelativeLayout carHolder, personProPicHolder;
		FrameLayout animatorHolder;
		carAnimator anio;
		
	}
	
	public interface Communicator {
		
		public void fixNotificationArray(int position);
		public void setClickable();
	}
	
	public void setTotalNotifications(int total) {
		this.totalNotifications = total;
	}
	
	

	public void sayGoodByAni()
	{
		if(fakeAni != null)
		{
			fakeAni.stopAnimation();
		}
		
	}

	@Override
	public void setHandler(Handler h, Runnable r) {
		// TODO Auto-generated method stub
		handler = h;
		runnable = r;
	}
	
	
}

//how to use
// GetNewNotifications n = new GetNewNotifications();
//n.setCommunicatior(this)
//n.startFetchingNoticiations(notifications array last object's notification id.. if there is none put 0,this, and the global variable of the users email).
//the override of gotNotificationResponse will return you a results array of all updated notificaitons. add them to the array and update the hsv


package com.burntout.burntout;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;



import android.content.Context;

public class GetNewNotifiications implements Post.Communicator {
	String lastId;
	Post post;
	Context c;
	Communicator myc;
	
	public GetNewNotifiications(){}
	
	
	public void startFetchingNotifications(String l_id,Context c, String myemail)
	{
		this.lastId = l_id;
		this.c = c;
		post = new Post();
		post.setCommunicator(this);
		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
         nameValuePairs.add(new BasicNameValuePair("email", myemail));
         nameValuePairs.add(new BasicNameValuePair("last", l_id));
         post.executePosts(nameValuePairs,"http://combustioninnovation.com/production/Goodyear/php/getMoreNotifications.php");
		
	}


	@Override
	public void gotResponse(JSONObject s) {
		// TODO Auto-generated method stub
		myc.gotNotificationResponse(s);
	};
	
	
	
	
	public void setCommunicator (Communicator c)
	{
		myc = c;
	}
	
	
	
	public interface Communicator
	{
			
		public void gotNotificationResponse(JSONObject s);
		
	}
	    
	
}

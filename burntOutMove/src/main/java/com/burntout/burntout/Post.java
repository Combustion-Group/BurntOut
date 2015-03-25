package com.burntout.burntout;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.AsyncTask;
import android.util.Log;



public class Post{
	String ServerResponse;
	String url;
	ArrayList<NameValuePair> nameValuePairs;
	Communicator comm;
	JSONObject response;
	
	
	public void setCommunicator (Communicator c)
	{
		comm = c;
	}

	
	
	public Post()
	{

	}

	
	
	public void executePosts(ArrayList<NameValuePair> l, String ur)
	{
		this.url = ur;
		this.nameValuePairs = l;
		new PostStuff().execute("D");
	}
	
	
	public void setoff()
	{
		comm.gotResponse(this.response);
		
	}
	
private class PostStuff extends AsyncTask<String, Void, String>{
	   	
	
		
	@SuppressWarnings("unused")
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
	
		
			String d = params[0];
		
		
		   HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(Post.this.url);
	        StringBuilder stringBuilder = new StringBuilder();
	        try {
	            // Add your data
	  
	        //    nameValuePairs.add(new BasicNameValuePair("comment", thecomment.getText().toString().trim()));
	
	        	  httppost.setEntity(new UrlEncodedFormEntity(Post.this.nameValuePairs));
	            // Execute HTTP Post Request
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	   //    String responseBody = EntityUtils.toString(entity);
	        	InputStream inputStream = entity.getContent();
 				BufferedReader reader = new BufferedReader(
 						new InputStreamReader(inputStream));
 				String line;
 				while ((line = reader.readLine()) !=null){
 					stringBuilder.append(line);
 				}
 				inputStream.close();
	            
	        //    ServerResponse = responseBody;
	            
	            if(response != null){
	            	
	            	
	            
	            }
	            String dss =    stringBuilder.toString();
	      
	      try {
	  			JSONObject j = new JSONObject(dss);
	  			//	String s = j.getString("status");
	  			
	  			Post.this.response = j;
	  			
	  		
	  		} catch (JSONException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	      
	      Log.d("D",dss);
	        } catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	        }
	    	return null;
	} 
	
    protected void onPostExecute(String result){
    	
    	Post.this.setoff();
    	
    	//Post.this.comm.gotResponse("DD");
    
    
    	}

}


public String getUrl()
{
	return this.url;
}



public interface Communicator
{
		
	public void gotResponse(JSONObject s);
	
}
    
 }
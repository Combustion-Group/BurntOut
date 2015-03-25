package com.burntout.burntout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONObject;





import android.os.AsyncTask;
import android.util.Log;



public class GetJson {
	String ServerResponse;
	String url;
	ArrayList<NameValuePair> nameValuePairs;
	Communicator comm;
	JSONObject response;
	
	
	public void setCommunicator (Communicator c)
	{
		comm = c;
	}

	
	
	public GetJson()
	{

	}

	
	
	public void executePosts(ArrayList<NameValuePair> l, String ur)
	{
		this.url = ur;
		this.nameValuePairs = l;
	      new ReadWeatherJSONFeedTask().execute(
	    		  		ur
		    		 );
	}
	
	
	public void setoff()
	{
		comm.gotResponse(this.response);
		Log.d("Ddsdasdasd","Dasds");
	}
	
	
	
	
	
	
	
	public String readJSONFeed (String URL){

		 StringBuilder stringBuilder = new StringBuilder();
	    	HttpClient httpClient = new DefaultHttpClient();
	    	HttpGet httpGet = new HttpGet(URL);

	    	try{
	    			HttpResponse response = httpClient.execute(httpGet);
	    			StatusLine statusLine = response.getStatusLine();
	    			int statusCode = statusLine.getStatusCode();
	    			if(statusCode == 200){
	    				HttpEntity entity = response.getEntity();
	    				InputStream inputStream = entity.getContent();
	    				BufferedReader reader = new BufferedReader(
	    						new InputStreamReader(inputStream));
	    				String line;
	    				while ((line = reader.readLine()) !=null){
	    					stringBuilder.append(line);
	    				}
	    				inputStream.close();
	    			}else{
	    				Log.d("readJSONFeed", "Failed to download file");
	    			}
	    		}catch (Exception e){ 
	    			Log.d("readJSONFeed", e.getLocalizedMessage());
	    		}
	    	return stringBuilder.toString();
	    }
	    
	    private class ReadWeatherJSONFeedTask extends AsyncTask<String, Void, String>{
	    	protected String doInBackground(String...urls){
	    		return readJSONFeed(urls[0]);
	    	}
	    protected void onPostExecute(String result){
	    	try{
	    		JSONObject jsonObject = new JSONObject(result);
	    		//JSONArray resultArray  = new 	JSONArray(jsonObject.getString("results"));
	    		GetJson.this.response = jsonObject;
	    		GetJson.this.setoff();
	    	}catch (Exception e){
	    		Log.d("ReadWeatherJSONFeedTask", e.getLocalizedMessage());
	    	}


	   };
		 
	 }
	    
	    
	    public interface Communicator
	    {
	    		
	    	public void gotResponse(JSONObject s);
	    	
	    }
	
}

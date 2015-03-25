package com.burntout.burntout;

import java.util.ArrayList;

import android.app.Application;

public class UserData extends Application {
	
	
	private String user_id;
	private String email;
	private String picture;
	private String fname;
	private String lname;
	
	
	
	private ArrayList<Vehicle> vehicles;
	
	public UserData() {
		this.user_id = null;
		this.email = null;
		this.picture = null;
		this.fname = null;
		this.lname = null;
		
		this.vehicles = null;
	}
	
	public UserData(String user_id, String email, String picture, String fname, String lname, ArrayList<Vehicle> vehicles) {
		
		this.user_id = user_id;
		this.email = email;
		this.picture = picture;
		this.fname = fname;
		this.lname = lname;
		
		this.vehicles = vehicles;
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}
	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	
	
	
	
	
	

}

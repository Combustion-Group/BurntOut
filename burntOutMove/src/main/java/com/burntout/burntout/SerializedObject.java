package com.burntout.burntout;

import java.io.Serializable;
import java.util.ArrayList;

public class SerializedObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Vehicle> myVehicles;
	private Vehicle vehicle;
	
	public SerializedObject(ArrayList<Vehicle> myVehicles) {
		super();
		this.myVehicles = myVehicles;
	}
	
	public SerializedObject(Vehicle vehicle) {
		super();
		this.vehicle = vehicle;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public ArrayList<Vehicle> getMyVehicles() {
		return myVehicles;
	}

	public void setMyVehicles(ArrayList<Vehicle> myVehicles) {
		this.myVehicles = myVehicles;
	}
	
	
	
	
	

	

}

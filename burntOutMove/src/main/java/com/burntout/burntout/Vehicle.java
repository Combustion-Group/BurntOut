package com.burntout.burntout;


import java.io.Serializable;




public class Vehicle implements Serializable {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String vehicle_id, vehicle_type_id, car_model, plate_number, plate_state, created;
	
	public Vehicle(String vehicle_id, String vehicle_type_id, String car_model, String plate_number, String plate_state, String created) {
		
		this.vehicle_id = vehicle_id;
		this.vehicle_type_id = vehicle_type_id;
		this.car_model = car_model;
		this.plate_number = plate_number;
		this.plate_state = plate_state;
		this.created = created;
		
	}
	
public Vehicle(String vehicle_id, String vehicle_type_id, String car_model, String plate_number, String plate_state) {
		
		this.vehicle_id = vehicle_id;
		this.vehicle_type_id = vehicle_type_id;
		this.car_model = car_model;
		this.plate_number = plate_number;
		this.plate_state = plate_state;
		this.created = "";
		
		
	}
	
	public String getVehicleId() {
		return this.vehicle_id;
	}
	
	public void setVehicleId(String s) {
		this.vehicle_id = s;
	}
	
	public String getVehicleTypeId() {
		return this.vehicle_type_id;
	}
	
	public void setVehicleTypeId(String s) {
		this.vehicle_type_id = s;
	}
	public String getCarModel() {
		return this.car_model;
	}
	
	public void setCarModel(String s) {
		this.car_model = s;
	}
	public String getPlateNumber() {
		return this.plate_number;
	}
	
	public void setPlateNumber(String s) {
		this.plate_number = s;
	}
	public String getPlateState() {
		return this.plate_state;
	}
	
	public void setPlateState(String s) {
		this.plate_state = s;
	}
	
	
}

package com.burntout.burntout;

import java.io.Serializable;

public class Notification implements Serializable {
	

	private static final long serialVersionUID = 1L;
	String notification_id;
	String vehicle_type;
	String message;
	String lights_out;
	String notification_fromID;
	String user_fname;
	String user_lname;
	String created;
	String notifier_reported_count;
	String notifier_reporter_count;
	String notifier_ranking;
	String plate_number;
	String picture;
	String theranking;
	
	int wasHelpful;
	int wasRead;
	
	public Notification(String notification_id, String vehicle_type,
			String message, String lights_out, String notification_fromID,
			String user_fname, String user_lname, String created,
			String notifier_reported_count, String notifier_reporter_count,
			String notifier_ranking, String plate_number, String picture,
			String theranking) {
	//	super();
		this.notification_id = notification_id;
		this.vehicle_type = vehicle_type;
		this.message = message;
		this.lights_out = lights_out;
		this.notification_fromID = notification_fromID;
		this.user_fname = user_fname;
		this.user_lname = user_lname;
		this.created = created;
		this.notifier_reported_count = notifier_reported_count;
		this.notifier_reporter_count = notifier_reporter_count;
		this.notifier_ranking = notifier_ranking;
		this.plate_number = plate_number;
		this.picture = picture;
		this.theranking = theranking;
		
	}

	public int getWasHelpful() {
		return wasHelpful;
	}

	public void setWasHelpful(int wasHelpful) {
		this.wasHelpful = wasHelpful;
	}

	public int getWasRead() {
		return wasRead;
	}

	public void setWasRead(int wasRead) {
		this.wasRead = wasRead;
	}

	public String getNotification_id() {
		return this.notification_id;
	}

	public void setNotification_id(String notification_id) {
		this.notification_id = notification_id;
	}

	public String getVehicle_type() {
		return this.vehicle_type;
	}

	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLights_out() {
		return this.lights_out;
	}

	public void setLights_out(String lights_out) {
		this.lights_out = lights_out;
	}

	public String getNotification_fromID() {
		return notification_fromID;
	}

	public void setNotification_fromID(String notification_fromID) {
		this.notification_fromID = notification_fromID;
	}

	public String getUser_fname() {
		return user_fname;
	}

	public void setUser_fname(String user_fname) {
		this.user_fname = user_fname;
	}

	public String getUser_lname() {
		return user_lname;
	}

	public void setUser_lname(String user_lname) {
		this.user_lname = user_lname;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getNotifier_reported_count() {
		return notifier_reported_count;
	}

	public void setNotifier_reported_count(String notifier_reported_count) {
		this.notifier_reported_count = notifier_reported_count;
	}

	public String getNotifier_reporter_count() {
		return notifier_reporter_count;
	}

	public void setNotifier_reporter_count(String notifier_reporter_count) {
		this.notifier_reporter_count = notifier_reporter_count;
	}

	public String getNotifier_ranking() {
		return notifier_ranking;
	}

	public void setNotifier_ranking(String notifier_ranking) {
		this.notifier_ranking = notifier_ranking;
	}

	public String getPlate_number() {
		return plate_number;
	}

	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getTheranking() {
		return theranking;
	}

	public void setTheranking(String theranking) {
		this.theranking = theranking;
	}
	
	

}

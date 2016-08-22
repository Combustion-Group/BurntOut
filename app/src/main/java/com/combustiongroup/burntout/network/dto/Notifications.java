package com.combustiongroup.burntout.network.dto;

import java.io.Serializable;

/**
 * Created by Candy on 6/27/16.
 */
public class Notifications implements Serializable {

    public String user_fname,
            user_lname,
            picture,
            message,
            notifier_ranking,
            lights_out,
            created,
            plate_number,
            notifier_reported_count,
            notifier_reporter_count,
            theranking,
            notification_id,
            vehicle_type;


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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotifier_ranking() {
        return notifier_ranking;
    }

    public void setNotifier_ranking(String notifier_ranking) {
        this.notifier_ranking = notifier_ranking;
    }

    public String getLights_out() {
        return lights_out;
    }

    public void setLights_out(String lights_out) {
        this.lights_out = lights_out;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
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

    public String getTheranking() {
        return theranking;
    }

    public void setTheranking(String theranking) {
        this.theranking = theranking;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }
}

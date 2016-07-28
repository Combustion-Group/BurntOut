package com.combustiongroup.burntout;

import java.io.Serializable;

/**
 * Created by Candy on 6/27/16.
 */
public class Notifications implements Serializable {

    public String firstname, lastname,profile_picture, message, ranking_title,lights,
            created, plate;
    public int reported, received, ranking, notification_id;

    public Notifications() {
    }

    public Notifications(String firstname, String lastname, String profile_picture, String message,
                         String ranking_title, String lights,String created, String plate,
                         int reported, int received, int ranking, int notification_id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.profile_picture = profile_picture;
        this.message = message;
        this.ranking_title = ranking_title;
        this.lights = lights;
        this.created = created;
        this.plate = plate;
        this.reported = reported;
        this.received = received;
        this.ranking = ranking;
        this.notification_id = notification_id;
    }


}

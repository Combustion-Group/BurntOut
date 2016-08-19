package com.combustiongroup.burntout;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

/**
 * Created by Candy on 5/6/16.
 */
public class Net {

    public static Net singleton = new Net();
    public int liveRequests = 0;

    static final String base = "http://combustioninnovation.com/production/Goodyear/php/";

    public enum Urls {
        FBLogin(base + "loginFB.php"),
        Signup(base + "adduser.php"),
        ForgotPassword(base + "forgotPW.php"),
        SetProfileImage(base + "changeprofilepicture.php"),
        EditPreferences(base + "editpreferences.php"),
        ProfileInformation(base + "profile.php"),
        AddVehicle(base + "addvehicle.php"),
        EditVehicle(base + "editVehicle.php"),
        DeleteVehicle(base + "deletecar.php"),
        EditUserInfo(base + "editUserInfo.php"),
        ChangePassword(base + "changepassword.php"),
        ReportBurnout(base + "sendmessage.php"),
        GetNotification(base + "getMoreNotifications.php"),
        DeleteNotification(base + "deletenotification.php"),
        GooglePushRegister(base + "setgooglepushid.php"),
        PostTest("http://httpbin.org/post");

        public final String value;

        Urls(String value) {
            this.value = value;
        }
    }//Urls

    public RequestQueue requestQueue;

    //cached results of common calls
    public HashMap<String, String> cache = new HashMap<>();

    public void init(Context c) {

        requestQueue = Volley.newRequestQueue(c);

        //track requests and automatically dismiss spinner?
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {

                liveRequests--;
//                Log.w("#app", "finished - live requests: "+liveRequests);

                if (liveRequests <= 0) {
//                    Log.w("#app", "close spinner");
                    liveRequests = 0;
                    Spinner.close();
                }
            }
        });
    }//init

    public void addRequest(Context c, Request r) {
        if (liveRequests == 0) {
            Intent i = new Intent(c, Spinner.class);
            c.startActivity(i);
        }
        liveRequests++;
        Log.w("#app", "added - live requests: "+liveRequests);

        requestQueue.add(r);
    }//add request

    //called when heavy processes unrelated to network activity need to lock the ui
    public void startHeavyTask(Context c) {
        if (liveRequests == 0) {
            Intent i = new Intent(c, Spinner.class);
            c.startActivity(i);
        }
        liveRequests++;
    }//start heavy task

    public void finishHeavyTask() {
        liveRequests--;
        if (liveRequests <= 0) {
            liveRequests = 0;
            Spinner.close();
        }
    }//finish heavy task
}//Net
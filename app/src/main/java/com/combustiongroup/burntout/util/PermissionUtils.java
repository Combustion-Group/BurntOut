package com.combustiongroup.burntout.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;



/**
 * Created by WarMachine on 8/24/16.
 */

public class PermissionUtils {

    public static final int PermissionsRequestCode = 4;

    //Ask for Permission to use external storage and location  because we need to send the location
    // to the Server and we need external storage to access the pictures.
    public static void requestAppPermissions(Activity activity){
        String s = com.combustiongroup.burntout.Manifest.permission.C2D_MESSAGE;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                    PermissionsRequestCode);

        }
    }
}

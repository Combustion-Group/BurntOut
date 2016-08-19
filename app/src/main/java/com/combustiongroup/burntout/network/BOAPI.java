package com.combustiongroup.burntout.network;

/**
 * Created by william Matias on 8/18/16.
 */


import com.combustiongroup.burntout.network.dto.Notifications;
import com.combustiongroup.burntout.network.dto.Preferences;
import com.combustiongroup.burntout.network.dto.Stats;
import com.combustiongroup.burntout.network.dto.UserInfo;
import com.combustiongroup.burntout.network.dto.Vehicle;
import com.combustiongroup.burntout.network.dto.response.LoginResponse;
import com.combustiongroup.burntout.network.dto.response.UploadPictureResponse;
import com.combustiongroup.burntout.network.dto.response.UserProfileResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BOAPI {

    // Response Objects
    public static LoginResponse loginResponse;
    public static UserProfileResponse userProfileResponse;
    public static UploadPictureResponse uploadPictureResponse;

    // UserProfileResponse's Objects
    public static UserInfo userInfo;
    public static Preferences userPreferences;
    public static Stats userStats;
    public static List<Vehicle> userVehicles;
    public static List<Notifications> userNotifications = new ArrayList<Notifications>();


    public static BOService service = new Retrofit.Builder()
            .baseUrl(Urls.base)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BOService.class);


}

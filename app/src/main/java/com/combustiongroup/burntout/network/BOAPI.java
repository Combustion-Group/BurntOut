package com.combustiongroup.burntout.network;

/**
 * Created by william Matias on 8/18/16.
 */


import android.util.Log;

import com.combustiongroup.burntout.network.dto.Notifications;
import com.combustiongroup.burntout.network.dto.Preferences;
import com.combustiongroup.burntout.network.dto.Stats;
import com.combustiongroup.burntout.network.dto.UserInfo;
import com.combustiongroup.burntout.network.dto.Vehicle;
import com.combustiongroup.burntout.network.dto.response.LoginResponse;
import com.combustiongroup.burntout.network.dto.response.UserProfileResponse;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BOAPI {

    // Response Objects
    //LoginResponse Object come with a status that tell us one for success and other fields
    //like email, fname, lname. if the email and password match on the database
    public static LoginResponse gLoginResponse;
    //UserProfileResponse has most of the user relate information a list of other objects that
    //this response fills can be found below
    public static UserProfileResponse gUserProfileResponse;


    // UserProfileResponse's Objects
    public static UserInfo gUserInfo;
    public static Preferences gUserPreferences;
    public static Stats gUserStats;
    public static List<Vehicle> gUserVehicles;
    public static List<Notifications> gUserNotifications;

    private static OkHttpClient getClient(){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
        return client;
    }
    public static BOService service = new Retrofit.Builder()
            .baseUrl(Urls.base)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BOService.class);



    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Log.d("STAPI", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.d("STAPI",String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            final String responseString = new String(response.body().bytes());
            Log.d("STAPI", "Response: " + responseString);
            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), responseString))
                    .build();
        }
    }
}


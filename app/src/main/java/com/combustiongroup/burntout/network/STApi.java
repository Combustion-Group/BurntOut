package com.combustiongroup.burntout.network;

/**
 * Created by william Matias on 8/18/16.
 */


import com.combustiongroup.burntout.network.dto.response.UserResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class STApi {

    public static UserResponse userResponse;
    public static String status;

    public static STService service = new Retrofit.Builder()
            .baseUrl("http://combustioninnovation.com/production/Goodyear/php/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(STService.class);
}

package com.combustiongroup.burntout.network;

/**
 * Created by William Matias on 8/18/16.
 */


import com.combustiongroup.burntout.network.dto.request.LoginRequest;
import com.combustiongroup.burntout.network.dto.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface STService {

    @POST("login.php")
    Call<STResponse<UserResponse>> login(@Body LoginRequest body);
}

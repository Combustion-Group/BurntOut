package com.combustiongroup.burntout.network;

/**
 * Created by William Matias on 8/18/16.
 */


import com.combustiongroup.burntout.network.dto.response.LoginResponse;
import com.combustiongroup.burntout.network.dto.response.UserProfileResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface BOService {

    @FormUrlEncoded
    @POST(Urls.login)
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device") String device,
            @Field("pushkey") String pushkey);

    @FormUrlEncoded
    @POST(Urls.profile)
    Call<UserProfileResponse> getUserProfile(
            @Field("email") String email);

    @Multipart
    @POST(Urls.uploadProfilePicture)
    Call<ResponseBody> uploadProfilePicure(
            @Field("email") String email,
            @Part byte[] file);

    @FormUrlEncoded
    @POST(Urls.deleteCar)
    Call<UserProfileResponse> deleteCar(
            @Field("email") String email,
            @Field("plate_number") String plateNumber);

}

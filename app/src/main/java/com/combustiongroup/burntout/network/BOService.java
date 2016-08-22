package com.combustiongroup.burntout.network;

/**
 * Created by William Matias on 8/18/16.
 */


import com.combustiongroup.burntout.network.dto.response.LoginResponse;
import com.combustiongroup.burntout.network.dto.response.ReportResponse;
import com.combustiongroup.burntout.network.dto.response.SignUpResponse;
import com.combustiongroup.burntout.network.dto.response.StatusResponse;
import com.combustiongroup.burntout.network.dto.response.UserProfileResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface BOService {

//    Login Calls
    //App Login
    @FormUrlEncoded
    @POST(Urls.login)
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device") String device,
            @Field("pushkey") String pushkey);

    //facebook Login
    @FormUrlEncoded
    @POST(Urls.mFacebookLogin)
    Call<LoginResponse> facebookLogin(
            @Field("email") String email,
            @Field("fname") String firstName,
            @Field("lname") String lastName,
            @Field("fbid") String facebookId,
            @Field("pushkey") String pushkey,
            @Field("device") String device);

    //User Calls
    @FormUrlEncoded
    @POST(Urls.profile)
    Call<UserProfileResponse> getUserProfile(
            @Field("email") String email);

    @FormUrlEncoded
    @POST(Urls.mSignUp)
    Call<SignUpResponse> signUp(
            @Field("fname") String firstName,
            @Field("lname") String lastName,
            @Field("email") String email,
            @Field("password") String password);

    //Edit Profile Information EndPoint
    @FormUrlEncoded
    @POST(Urls.mEditProfile)
    Call<StatusResponse> editProfileInformation(
            @Field("f_name") String firstName,
            @Field("l_name") String lastName,
            @Field("oldEmail") String oldEmail,
            @Field("newEmail") String newEmail);

    @Multipart
    @POST(Urls.uploadProfilePicture)
    Call<ResponseBody> uploadProfilePicure(
            @Part("email") String email,
            @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST(Urls.mChangePassword)
    Call<StatusResponse> changePassword(
            @Field("email") String email,
            @Field("oldpassword") String oldPassword,
            @Field("newpassword") String newPassword);

    @FormUrlEncoded
    @POST(Urls.mResetPassword)
    Call<StatusResponse> resetPassword(
            @Field("email") String email);

    @FormUrlEncoded
    @POST(Urls.mPushSwitch)
    Call<StatusResponse> pushSwitch(
            @Field("email") String email,
            @Field("push_notifications") String binaryValue);

    @FormUrlEncoded
    @POST(Urls.mDeleteNotifications)
    Call<ResponseBody> deleteNotification(
            @Field("notification_id") String notificationId);

//    Vehicles Calls
    @FormUrlEncoded
    @POST(Urls.deleteVehicle)
    Call<StatusResponse> deleteVehicle(
            @Field("email") String email,
            @Field("plate_number") String plateNumber);

    @FormUrlEncoded
    @POST(Urls.registerVehicle)
    Call<StatusResponse> registerVehicle(
            @Field("email") String email,
            @Field("vehicle_type") String vehicleType,
            @Field("car_model") String carModel,
            @Field("plate_state") String plateState,
            @Field("plate_number") String plateNumber);

    @FormUrlEncoded
    @POST(Urls.editVehicle)
    Call<StatusResponse> editVehicle(
            @Field("email") String email,
            @Field("vehicle_id") String vehicleId,
            @Field("vehicle_type_id") String vehicleTypeID,
            @Field("car_model") String carModel,
            @Field("state") String plateState,
            @Field("plate_number") String plateNumber);


    @FormUrlEncoded
    @POST(Urls.reportVehicle)
    Call<ReportResponse> reportVehicle(
            @Field("email") String email,
            @Field("vehicle_type") String vehicleType,
            @Field("plate_state") String plateState,
            @Field("license_plate") String licensePlate,
            @Field("lights_out") String lightsOut,
            @Field("special_message") String specialMessage);


}

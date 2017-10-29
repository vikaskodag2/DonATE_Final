package com.sdl.app.tempdonate.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by vishvanatarajan on 3/10/17.
 */

public interface APIService {

    // sending user registration details
    @Headers("Content-Type: application/json")
    @POST("users/register")
    Call<MSG> userSignUp(@Body User user);

    // checking user Log in details
    @Headers("Content-Type: application/json")
    @POST("users/authenticate")
    Call<List<MSG>> userLogIn(@Body User user);

    // checking if email ID specified by user during forgot password is valid or not
    @Headers("Content-Type: application/json")
    @POST("users/forgot/{email}")
    Call<List<MSG>> userForgot(@Path("email") String email);

    // checking if OTP for resetting password is valid or not
    @Headers("Content-Type: application/json")
    @POST("users/checkOtp")
    Call<List<MSG>> OTPVerify(@Body OTP otp);

    // sending the new password of user to server
    @Headers("Content-Type: application/json")
    @PUT("users/profile/password")
    Call<List<MSG>> updatePassword(@Body User user, @Header("Authorization") String token);

    // getting the profile of user from server
    @Headers("Content-Type: application/json")
    @GET("users/profile")
    Call<List<Profile>> userProfile(@Header("Authorization") String token);

    // sending the updated details of profile to server
    @Headers("Content-Type: application/json")
    @PUT("users/profile")
    Call<List<MSG>> userUpdate(@Body User user, @Header("Authorization") String token);

    //sending donor info to server.
    @Headers("Content-Type: application/json")
    @POST("requests/donate")
    Call <List<NGOList>> sendDonorInfo(@Body NGOList infos, @Header("Authorization") String token);

    //getting receiver available to be receive.
    @Headers("Content-Type: application/json")
    @GET("users/ngos")
    Call<List<NGOList>> getOrgList();

    //getting donations of user from token
    @Headers("Content-Type: application/json")
    @GET("requests/donate")
    Call<List<NGOList>> getUserDonations(@Header("Authorization") String token);
}

package ru.serguun42.android.airportenhanced.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JSONPlaceholder {
    @GET("flights/list")
    Call<List<Flight>> listFlights(@Query("skip") int skip);

    @GET("flights/get")
    Call<Flight> getFlight(@Query("id") String id);

    @POST("flights/create")
    Call<Object> createFlight(@Header("X-Token") String token, @Body Flight body);

    @POST("flights/edit")
    Call<Object> editFlight(@Header("X-Token") String token, @Body FlightEditPayload body);

    @POST("flights/delete")
    Call<Flight> deleteFlight(@Header("X-Token") String token, @Body FlightDeletePayload body);

    @GET("account/check")
    Call<Object> checkAccount(@Header("X-Token") String token);

    @POST("account/signin")
    Call<SuccessWithToken> signIn(@Body UserPayload body);

    @POST("account/signup")
    Call<SuccessWithToken> signUp(@Body UserPayload body);
}

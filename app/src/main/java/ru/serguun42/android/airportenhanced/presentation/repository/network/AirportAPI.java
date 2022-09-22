package ru.serguun42.android.airportenhanced.presentation.repository.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.serguun42.android.airportenhanced.domain.model.Flight;

public interface AirportAPI {
    String API_BASE_URL = "https://airport.serguun42.ru/api/v1/";

    @GET("flights/list")
    Call<List<Flight>> listFlights(@Query("skip") int skip);

    @GET("flights/get")
    Call<Flight> getFlight(@Query("id") String id);

    @POST("flights/create")
    Call<Object> createFlight(@Header("X-Token") String token, @Body Flight body);

    @POST("flights/edit")
    Call<Object> editFlight(@Header("X-Token") String token, @Body APIMethods.FlightEditRequest body);

    @POST("flights/delete")
    Call<Flight> deleteFlight(@Header("X-Token") String token, @Body APIMethods.FlightDeleteRequest body);

    @GET("account/check")
    Call<Object> checkAccount(@Header("X-Token") String token);

    @POST("account/signin")
    Call<APIMethods.LoginResponse> signIn(@Body APIMethods.LoginRequestPayload body);

    @POST("account/signup")
    Call<APIMethods.LoginResponse> signUp(@Body APIMethods.LoginRequestPayload body);
}

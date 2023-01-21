package ru.serguun42.android.airportenhanced.presentation.repository.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.domain.model.UserRecord;
import ru.serguun42.android.airportenhanced.presentation.repository.RepositoryActions;

public interface AirportAPI {
    String API_BASE_URL = "https://airport.serguun42.ru/api/v1.2/";

    @GET("flights/list")
    Call<List<Flight>> listFlights(@Query("skip") int skip);

    @GET("flights/get")
    Call<Flight> getFlight(@Query("id") String id);

    @POST("flights/create")
    Call<Flight> createFlight(@Header("X-Token") String token, @Body Flight body);

    @POST("flights/edit")
    Call<Flight> editFlight(@Header("X-Token") String token, @Body APIRepository.FlightEditRequest body);

    @POST("flights/delete")
    Call<RepositoryActions.FlightDeleteResponse> deleteFlight(@Header("X-Token") String token, @Body APIRepository.FlightDeleteRequest body);

    @GET("account/check")
    Call<Session> checkAccount(@Header("X-Token") String token);

    @POST("account/signin")
    Call<Session> signIn(@Body APIRepository.LoginRequestPayload body);

    @POST("account/signup")
    Call<Session> signUp(@Body APIRepository.LoginRequestPayload body);

    @POST("account/signout")
    Call<Session> signOut(@Header("X-Token") String token);

    @GET("admin/listUsers")
    Call<List<UserRecord>> listUsers(@Header("X-Token") String token, @Query("skip") int skip);
}

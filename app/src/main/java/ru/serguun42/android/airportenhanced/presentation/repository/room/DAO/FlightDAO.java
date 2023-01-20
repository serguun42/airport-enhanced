package ru.serguun42.android.airportenhanced.presentation.repository.room.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.serguun42.android.airportenhanced.domain.model.Flight;

@Dao
public interface FlightDAO {
    @Query("SELECT * FROM flight")
    LiveData<List<Flight>> getAllFlights();

    @Query("SELECT * FROM flight SKIP LIMIT 10 OFFSET :skip")
    LiveData<List<Flight>> listItems(int skip);

    @Query("SELECT * FROM flight WHERE id = :id")
    LiveData<Flight> getFlightById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFlight(Flight flight);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFlights(List<Flight> flights);

    @Update
    void updateFlight(Flight flight);

    @Update
    void updateFlights(List<Flight> flights);

    @Delete
    void deleteFlight(Flight flight);

    @Query("DELETE FROM flight WHERE id = :id")
    int deleteFlightById(String id);

    @Query("DELETE FROM flight")
    int deleteAllFlights();
}

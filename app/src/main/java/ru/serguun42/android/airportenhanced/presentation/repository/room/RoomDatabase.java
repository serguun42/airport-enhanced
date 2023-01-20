package ru.serguun42.android.airportenhanced.presentation.repository.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.room.DAO.FlightDAO;
import ru.serguun42.android.airportenhanced.presentation.repository.room.DAO.SessionDAO;

@Database(entities = {Flight.class, Session.class}, version = 1, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {
    public abstract FlightDAO flightDAO();
    public abstract SessionDAO sessionDAO();

    private static volatile RoomDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RoomDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (RoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RoomDatabase.class,
                            "airport_app_database"
                    ).build();
                }
            }
        }
        return instance;
    }
}

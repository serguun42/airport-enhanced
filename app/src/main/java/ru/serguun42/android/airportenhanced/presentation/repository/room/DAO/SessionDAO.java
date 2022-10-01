package ru.serguun42.android.airportenhanced.presentation.repository.room.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ru.serguun42.android.airportenhanced.domain.model.Session;

@Dao
public interface SessionDAO {
    @Query("SELECT * FROM session WHERE token = :token")
    LiveData<Session> getSessionByToken(String token);

    @Query("DELETE FROM session")
    int deletePrevious();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void storeSession(Session session);
}

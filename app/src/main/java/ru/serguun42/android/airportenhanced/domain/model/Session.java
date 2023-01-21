package ru.serguun42.android.airportenhanced.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "session")
public class Session {
    @NonNull
    @PrimaryKey
    private String token;
    private boolean success;
    private String username;
    private int level;

    public Session() {
        this.token = UUID.randomUUID().toString();
        this.success = false;
        this.username = "";
        this.level = 0;
    }

    public Session(@NonNull String token, boolean success, String username, int level) {
        this.token = token;
        this.success = success;
        this.username = username;
        this.level = level;
    }

    @NonNull
    public String getToken() {
        return token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getUsername() {
        return username;
    }

    public int getLevel() {
        return level;
    }

    public void setToken(@NonNull String token) {
        this.token = token;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @NonNull
    @Override
    public String toString() {
        return "Session (" +
                (success ? "valid" : "not valid") +
                "): username – " + (username.isEmpty() ? "<none>" : username) +
                ", token – " + (token.isEmpty() ? "<none>" : token) +
                ", level – " + level;
    }
}
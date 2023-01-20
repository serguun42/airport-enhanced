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
    private boolean can_edit;

    public Session() {
        this.token = UUID.randomUUID().toString();
        this.success = false;
        this.username = "";
        this.can_edit = false;
    }

    public Session(@NonNull String token, boolean success, String username, boolean can_edit) {
        this.token = token;
        this.success = success;
        this.username = username;
        this.can_edit = can_edit;
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

    public boolean canEdit() {
        return can_edit;
    }

    public boolean isCan_edit() {
        return can_edit;
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

    public void setCan_edit(boolean can_edit) {
        this.can_edit = can_edit;
    }

    @NonNull
    @Override
    public String toString() {
        return "Session (" +
                (success ? "valid" : "not valid") +
                "): username – " + (username.isEmpty() ? "<none>" : username) +
                ", token – " + (token.isEmpty() ? "<none>" : token) +
                ", can_edit – " + can_edit;
    }
}
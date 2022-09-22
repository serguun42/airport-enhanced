package ru.serguun42.android.airportenhanced.domain.model;

public class UserLocal {
    private String username;
    private String token;

    public UserLocal(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
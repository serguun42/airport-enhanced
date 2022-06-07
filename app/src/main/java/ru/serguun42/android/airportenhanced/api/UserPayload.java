package ru.serguun42.android.airportenhanced.api;

public class UserPayload {
    final String username;
    final String password;

    public UserPayload(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

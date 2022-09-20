package ru.serguun42.android.airportenhanced.domain.model;

public class User {
    final String username;
    final String password;

    public User(String username, String password) {
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

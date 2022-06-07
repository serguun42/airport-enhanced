package ru.serguun42.android.airportenhanced.storage;

public class Credentials {
    private String token;

    public Credentials() {
    }

    public Credentials(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Credentials. Token = " + token;
    }
}

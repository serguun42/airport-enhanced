package ru.serguun42.android.airportenhanced.api;

public class FlightDeletePayload {
    final String id;

    public FlightDeletePayload(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

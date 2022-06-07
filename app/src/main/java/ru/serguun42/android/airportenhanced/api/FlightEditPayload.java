package ru.serguun42.android.airportenhanced.api;

public class FlightEditPayload {
    final String id;
    final Flight updated;

    public FlightEditPayload(String id, Flight updated) {
        this.id = id;
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public Flight getUpdated() {
        return updated;
    }
}

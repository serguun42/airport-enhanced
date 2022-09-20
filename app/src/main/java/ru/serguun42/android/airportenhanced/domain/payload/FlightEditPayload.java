package ru.serguun42.android.airportenhanced.domain.payload;

import ru.serguun42.android.airportenhanced.domain.model.Flight;

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

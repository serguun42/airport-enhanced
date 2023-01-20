package ru.serguun42.android.airportenhanced.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "flight")
public class Flight {
    @NonNull
    @PrimaryKey
    private String id;
    private boolean incoming;
    private String target_name;
    private String target_iata;
    private String gate;
    private String flight_number;
    private String plane_model;
    private String departure;
    private String arrival;

    private String image;

    @Ignore
    public Flight() {
        this.id = UUID.randomUUID().toString();
        this.incoming = true;
        this.target_name = "";
        this.target_iata = "";
        this.gate = "";
        this.flight_number = "";
        this.plane_model = "";
        this.departure = "";
        this.arrival = "";
        this.image = "";
    }

    public Flight(@NonNull String id, boolean incoming, String target_name, String target_iata, String gate, String flight_number, String plane_model, String departure, String arrival, String image) {
        this.id = id;
        this.incoming = incoming;
        this.target_name = target_name;
        this.target_iata = target_iata;
        this.gate = gate;
        this.flight_number = flight_number;
        this.plane_model = plane_model;
        this.departure = departure;
        this.arrival = arrival;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public String getTargetName() {
        return target_name;
    }

    public String getTargetIATA() {
        return target_iata;
    }

    public String getGate() {
        return gate;
    }

    public String getFlightNumber() {
        return flight_number;
    }

    public String getPlaneModel() {
        return plane_model;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    public void setTargetName(String target_name) {
        this.target_name = target_name;
    }

    public void setTargetIATA(String target_iata) {
        this.target_iata = target_iata;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public void setFlightNumber(String flight_number) {
        this.flight_number = flight_number;
    }

    public void setPlaneModel(String plane_model) {
        this.plane_model = plane_model;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getTarget_name() {
        return target_name;
    }

    public void setTarget_name(String target_name) {
        this.target_name = target_name;
    }

    public String getTarget_iata() {
        return target_iata;
    }

    public void setTarget_iata(String target_iata) {
        this.target_iata = target_iata;
    }

    public String getFlight_number() {
        return flight_number;
    }

    public void setFlight_number(String flight_number) {
        this.flight_number = flight_number;
    }

    public String getPlane_model() {
        return plane_model;
    }

    public void setPlane_model(String plane_model) {
        this.plane_model = plane_model;
    }

    @NonNull
    @Override
    public String toString() {
        return "Flight " + (incoming ? "from " : "to ") +
                target_name +
                " (" + target_name + ") " +
                " #" + flight_number;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

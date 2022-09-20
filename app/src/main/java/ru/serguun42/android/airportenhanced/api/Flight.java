package ru.serguun42.android.airportenhanced.api;

public class Flight {
    private String id;
    private boolean incoming;
    private String target_name;
    private String target_iata;
    private String gate;
    private String flight_number;
    private String plane_model;
    private String departure;
    private String arrival;

    public Flight() {
        this.id = "";
        this.incoming = true;
        this.target_name = "";
        this.target_iata = "";
        this.gate = "";
        this.flight_number = "";
        this.plane_model = "";
        this.departure = "";
        this.arrival = "";
    }

    public Flight(String id, boolean incoming, String target_name, String target_iata, String gate, String flight_number, String plane_model, String departure, String arrival) {
        this.id = id;
        this.incoming = incoming;
        this.target_name = target_name;
        this.target_iata = target_iata;
        this.gate = gate;
        this.flight_number = flight_number;
        this.plane_model = plane_model;
        this.departure = departure;
        this.arrival = arrival;
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
}

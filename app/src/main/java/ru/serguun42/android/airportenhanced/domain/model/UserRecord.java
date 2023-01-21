package ru.serguun42.android.airportenhanced.domain.model;

public class UserRecord {
    private String username;
    private int level;

    public UserRecord() {
        this.username = "";
        this.level = 0;
    }

    public UserRecord(String username, int level) {
        this.username = username;
        this.level = level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

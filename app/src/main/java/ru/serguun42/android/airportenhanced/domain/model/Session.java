package ru.serguun42.android.airportenhanced.domain.model;

public class Session {
    private boolean success;
    private String username;
    private String token;
    private boolean can_edit;

    public Session() {
        this.success = false;
        this.username = "";
        this.token = "";
        this.can_edit = false;
    }

    public Session(boolean success, String username, String token, boolean can_edit) {
        this.success = success;
        this.username = username;
        this.token = token;
        this.can_edit = can_edit;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public boolean canEdit() {
        return can_edit;
    }

    @Override
    public String toString() {
        return "Session: success = " + success +
                ", username = " + (username.isEmpty() ? "<none>" : username) +
                ", token = " + (token.isEmpty() ? "<none>" : token) +
                ", can_edit = " + can_edit;
    }
}
package ru.serguun42.android.airportenhanced.api;

public class SuccessWithToken {
    private boolean success;
    private String token;

    public SuccessWithToken() {
    }

    public SuccessWithToken(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

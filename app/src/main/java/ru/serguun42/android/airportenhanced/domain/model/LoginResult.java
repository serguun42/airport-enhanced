package ru.serguun42.android.airportenhanced.domain.model;

import androidx.annotation.Nullable;

public class LoginResult {
    @Nullable
    private UserLocal success;
    @Nullable
    private Integer error;

    public LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    public LoginResult(@Nullable UserLocal success) {
        this.success = success;
    }

    @Nullable
    public UserLocal getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
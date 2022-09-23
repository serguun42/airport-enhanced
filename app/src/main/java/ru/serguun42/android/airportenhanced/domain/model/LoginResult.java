package ru.serguun42.android.airportenhanced.domain.model;

import androidx.annotation.Nullable;

public class LoginResult {
    @Nullable
    private Session success;
    @Nullable
    private Integer error;

    public LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    public LoginResult(@Nullable Session success) {
        this.success = success;
    }

    @Nullable
    public Session getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
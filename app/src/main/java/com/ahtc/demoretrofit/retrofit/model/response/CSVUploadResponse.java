package com.ahtc.demoretrofit.retrofit.model.response;

import com.google.gson.annotations.SerializedName;

public class CSVUploadResponse {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

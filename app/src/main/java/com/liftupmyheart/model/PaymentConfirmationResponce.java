package com.liftupmyheart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentConfirmationResponce implements Serializable {
    @SerializedName("response")
    @Expose
    int response;
    @SerializedName("is_active")
    @Expose
    int is_active;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("message")
    @Expose
    String message;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

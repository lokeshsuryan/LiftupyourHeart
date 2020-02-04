package com.liftupmyheart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentDetail implements Serializable {

    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("user_id")
    @Expose
    String user_id;
    @SerializedName("rency_code")
    @Expose
    String rency_code;
    @SerializedName("amount;")
    @Expose
    String amount;
    @SerializedName("plan_id")
    @Expose
    String plan_id;
    @SerializedName("plan_type")
    @Expose
    String plan_type;
    @SerializedName("invoice_number")
    @Expose
    String invoice_number;
    @SerializedName("create_time")
    @Expose
    String create_time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRency_code() {
        return rency_code;
    }

    public void setRency_code(String rency_code) {
        this.rency_code = rency_code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}

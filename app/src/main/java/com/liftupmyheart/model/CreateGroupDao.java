package com.liftupmyheart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupDao implements Serializable {
    @SerializedName("participant")
    @Expose
    private List<GroupItemDao> participant= new ArrayList<>();
    @SerializedName("groupName")
    @Expose
    String groupName;
    @SerializedName("rency_code")
    @Expose
    String rency_code;
    @SerializedName("amount")
    @Expose
    String amount;
    @SerializedName("create_time")
    @Expose
    String create_time;
    @SerializedName("invoice_number")
    @Expose
    String invoice_number;
    @SerializedName("type")
    @Expose
    String type;

    public List<GroupItemDao> getParticipant() {
        return participant;
    }

    public void setParticipant(List<GroupItemDao> participant) {
        this.participant = participant;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

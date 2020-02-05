package com.liftupmyheart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupDetailDao implements Serializable {
    @SerializedName("lastPayment")
    @Expose
    private String lastPayment;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("payment")
    @Expose
    private String payment;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("admin_id")
    @Expose
    private String admin_id;
    @SerializedName("participants")
    @Expose
    private ArrayList<Participants> participants;

    public String getLastPayment ()
    {
        return lastPayment;
    }

    public void setLastPayment (String lastPayment)
    {
        this.lastPayment = lastPayment;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPayment ()
    {
        return payment;
    }

    public void setPayment (String payment)
    {
        this.payment = payment;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getAdmin_id ()
    {
        return admin_id;
    }

    public void setAdmin_id (String admin_id)
    {
        this.admin_id = admin_id;
    }

    public ArrayList<Participants> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participants> participants) {
        this.participants = participants;
    }
}

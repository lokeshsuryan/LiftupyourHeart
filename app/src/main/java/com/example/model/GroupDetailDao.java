package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupDetailDao implements Serializable {
    private String lastPayment;

    private String id;

    private String payment;

    private String name;

    private String admin_id;

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

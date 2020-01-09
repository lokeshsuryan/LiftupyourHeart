package com.example.model;

public class Data {

    private String id;

    private String first_name;

    private String phone;

    private String email;

    private String name;

    private String last_name;

    private String is_paid;

    private String lastPayment;

    private String payment;

    private String admin_id;

    private Participants[] participants;

    public String getLastPayment ()
    {
        return lastPayment;
    }

    public void setLastPayment (String lastPayment)
    {
        this.lastPayment = lastPayment;
    }



    public String getPayment ()
    {
        return payment;
    }

    public void setPayment (String payment)
    {
        this.payment = payment;
    }


    public String getAdmin_id ()
    {
        return admin_id;
    }

    public void setAdmin_id (String admin_id)
    {
        this.admin_id = admin_id;
    }

    public Participants[] getParticipants ()
    {
        return participants;
    }

    public void setParticipants (Participants[] participants)
    {
        this.participants = participants;
    }


    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }

}

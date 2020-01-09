package com.example.model;

import com.google.gson.annotations.Expose;

public class ServerResponse {


    private String message;

    private int response;

    private String status;
    @Expose
    private Data data;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public int getResponse() {
        return response;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }




}

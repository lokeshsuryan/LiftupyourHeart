package com.example.utills;

import com.example.model.ContactDao;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AppContactList {

    @SerializedName("contactList")
    private List<ContactDao> contactList = new ArrayList<>();

    public List<ContactDao> getContactList() {
        return contactList;
    }

    public void setContactList(List<ContactDao> contactList) {
        this.contactList = contactList;
    }
}

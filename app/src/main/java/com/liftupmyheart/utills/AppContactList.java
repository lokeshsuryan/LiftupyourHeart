package com.liftupmyheart.utills;

import com.google.gson.annotations.SerializedName;
import com.liftupmyheart.model.ContactDao;

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

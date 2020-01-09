package com.example.model;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordDao {
    @SerializedName("old")
    String oldPassword;
    @SerializedName("new")
    String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

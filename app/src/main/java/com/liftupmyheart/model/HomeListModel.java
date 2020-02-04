package com.liftupmyheart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HomeListModel implements Serializable {
    @SerializedName("mResources")
    @Expose
    int mResources;
    @SerializedName("colorCode")
    @Expose
    int colorCode;
    @SerializedName("title")
    @Expose
    String title;

    public int getmResources() {
        return mResources;
    }

    public void setmResources(int mResources) {
        this.mResources = mResources;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

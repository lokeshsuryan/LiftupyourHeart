package com.liftupmyheart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NextPrayerListItem implements Serializable {
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("isSelected")
    @Expose
    boolean isSelected=false;
    @SerializedName("name")
    @Expose
    int name;
    @SerializedName("rawId")
    @Expose
    int rawId;
    @SerializedName("ringTime")
    @Expose
    int ringTime;
    @SerializedName("alarmId")
    @Expose
    int alarmId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getRawId() {
        return rawId;
    }

    public void setRawId(int rawId) {
        this.rawId = rawId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getRingTime() {
        return ringTime;
    }

    public void setRingTime(int ringTime) {
        this.ringTime = ringTime;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }
}

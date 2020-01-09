package com.example.model;

public class AlarmDao {
    String alarmTime;
    String prayerId;
    String prayerName;
    String description;
    int repeartAlaramType;
    int requestCode;


    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getPrayerId() {
        return prayerId;
    }

    public void setPrayerId(String prayerId) {
        this.prayerId = prayerId;
    }

    public String getPrayerName() {
        return prayerName;
    }

    public void setPrayerName(String prayerName) {
        this.prayerName = prayerName;
    }

    public int getRepeartAlaramType() {
        return repeartAlaramType;
    }

    public void setRepeartAlaramType(int repeartAlaramType) {
        this.repeartAlaramType = repeartAlaramType;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


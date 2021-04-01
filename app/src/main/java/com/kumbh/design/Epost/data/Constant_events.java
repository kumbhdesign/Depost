package com.kumbh.design.Epost.data;

public class Constant_events {

    String festival_name,date,category,time,id;

    public Constant_events(String festival_name, String date, String category, String time, String id) {
        this.festival_name = festival_name;
        this.date = date;
        this.category = category;
        this.time = time;
        this.id = id;
    }

    public Constant_events() {

    }

    public String getFestival_name() {
        return festival_name;
    }

    public void setFestival_name(String festival_name) {
        this.festival_name = festival_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

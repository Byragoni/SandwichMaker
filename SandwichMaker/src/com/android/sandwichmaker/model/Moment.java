package com.android.sandwichmaker.model;

import com.android.sandwichmaker.Utils;

public class Moment {

    private String description;
    private String date;
    private String location;
    private int rating;


    public static String MOMENT_DESC = "momenttask";
    public static String MOMENT_LOCATION = "momentlocation";
    public static String MOMENT_TIME = "momenttime";
    public static String MOMENT_RATING = "momentrating";

    public Moment(String description, int scale) {
        this.rating = scale;
        this.description = description;
        this.date = Long.toString(System.currentTimeMillis());
        this.location = Utils.getLocation();
    }


    public Moment(int rating, String description, String date, String location) {
        super();
        this.rating = rating;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }


}

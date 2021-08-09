package com.taximachine.machinelogger;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

/**
 *  * Created by alejandro.tkachuk
 *   */

public class GPSPoint {

    private BigDecimal lat, lon;
    private Date date;
    private Float speed = null;
    private float accuracy;

    public GPSPoint(BigDecimal latitude, BigDecimal longitude, float accuracy) {
        this.lat = latitude;
        this.lon = longitude;
        this.date = new Date();
        this.accuracy = accuracy;
    }

    public GPSPoint(Double latitude, Double longitude, float accuracy) {
        this(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude), accuracy);
    }

    public GPSPoint(BigDecimal latitude, BigDecimal longitude, float accuracy, float speed) {
        this(latitude, longitude, accuracy);
        this.speed = speed;
    }

    public GPSPoint(Double latitude, Double longitude, float accuracy, float speed) {
        this(latitude, longitude, accuracy);
        this.speed = speed;
    }

    public BigDecimal getLatitude() {
        return lat;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getLongitude() {

        return lon;
    }

    @Override
    public String toString() {
        return "(" + lat + ", " + lon + ")";
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}



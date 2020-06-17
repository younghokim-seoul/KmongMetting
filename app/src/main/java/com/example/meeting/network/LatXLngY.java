package com.example.meeting.network;

public class LatXLngY {
    private double lat;
    private double lng;

    private double x;
    private double y;

    public LatXLngY() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "LatXLngY{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}

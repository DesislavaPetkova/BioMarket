package com.desislava.market.beans;

public class GooglePlace {


    private double lat;
    private double lng;
    private boolean openNow;
    private String vicinity;

    public GooglePlace() {
    }

    public GooglePlace(double lat, double lng, boolean openNow, String vicinity) {
        this.lat = lat;
        this.lng = lng;
        this.openNow = openNow;
        this.vicinity = vicinity;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public String getVicinity() {
        return vicinity;
    }

    @Override
    public String toString() {
        return "GooglePlace{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", openNow=" + openNow +
                ", vicinity='" + vicinity + '\'' +
                '}';
    }
}

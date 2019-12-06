package com.khadejaclarke.mapboxlocationtracking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Truck {
    @SerializedName("TruckId")
    @Expose
    private String truck_id;

    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @SerializedName("longitude")
    @Expose
    private Double longitude;


    /**
     * @return The truck ID
     */
    public String getID() {
        return truck_id;
    }

    /**
     * @return The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

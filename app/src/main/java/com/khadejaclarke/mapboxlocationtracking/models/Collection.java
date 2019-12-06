package com.khadejaclarke.mapboxlocationtracking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Collection {
    @SerializedName("truckopcoor_item")
    @Expose
    private List<Truck> trucks = new ArrayList<>();

    /**
     *
     * @return The trucks
     */
    public List<Truck> getTrucks() {
        return trucks;
    }

    /**
     *
     * @param trucks The trucks
     */
    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }

}

package com.khadejaclarke.mapboxlocationtracking.utils;

import com.khadejaclarke.mapboxlocationtracking.models.Collection;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    String BASE_URL = "http://0.0.0.0/api/";

    @GET("truckstartingcoordinates/")
    Call<Collection> getTrucksFromAPI();
}

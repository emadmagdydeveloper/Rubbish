package com.creative.share.apps.rubbish.services;


import com.creative.share.apps.rubbish.models.PlaceGeocodeData;
import com.creative.share.apps.rubbish.models.PlaceMapDetailsData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Services {



    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);


}

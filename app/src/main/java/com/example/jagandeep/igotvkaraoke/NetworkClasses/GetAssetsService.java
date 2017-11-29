package com.example.jagandeep.igotvkaraoke.NetworkClasses;

import com.example.jagandeep.igotvkaraoke.DataModel.AssetsDataModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;


/**
 * Created by jagandeep on 5/22/17.
 */

public interface GetAssetsService {
    @POST("assets")
    Call<AssetsDataModel> callAssetsPath(
            @HeaderMap Map<String, String> header,
            @Body Map<String, String> body
    );

}

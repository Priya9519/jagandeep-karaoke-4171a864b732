package com.example.jagandeep.igotvkaraoke.NetworkClasses;

import com.example.jagandeep.igotvkaraoke.DataModel.KaraokeDataModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

/**
 * Created by jagandeep on 9/4/17.
 */

public interface karoakeService {
    @GET("api/p/karaoke")
    Call<KaraokeDataModel> getKaraokeData(
            @HeaderMap Map<String, String> header
    );
}

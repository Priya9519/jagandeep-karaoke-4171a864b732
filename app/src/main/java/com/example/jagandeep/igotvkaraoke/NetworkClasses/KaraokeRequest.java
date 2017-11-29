package com.example.jagandeep.igotvkaraoke.NetworkClasses;

import com.example.jagandeep.igotvkaraoke.DataModel.KaraokeDataModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jagandeep on 9/4/17.
 */

public class KaraokeRequest {
    private karoakeService karaokeService;


    public KaraokeRequest(){
        karaokeService = ApiClient.createService(karoakeService.class);

    }

    public void getKaraokeWithMap(Map header, final ServiceCallBack<KaraokeDataModel> callback){
        Call<KaraokeDataModel> call = karaokeService.getKaraokeData(header);

        ApiClient.hitServer(call, new ServiceCallBack<KaraokeDataModel>(){

            @Override
            public void onSuccess(Response<KaraokeDataModel> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(String header, String message) {
                callback.onError(header,message);

            }
        });
    }
}

package com.example.jagandeep.igotvkaraoke.NetworkClasses;

import com.example.jagandeep.igotvkaraoke.DataModel.KaraokeBuilderDataModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sierra1 on 10/30/17.
 */

public class KaraokeStatusCheckRequest {
    private KaraokeStatusCheckService karaokeService;


    public KaraokeStatusCheckRequest(){
        karaokeService = ApiClient.createService(KaraokeStatusCheckService.class);

    }

    public void statusKaraokeWithMap(String videoid, Map header, String body, final ServiceCallBack<KaraokeBuilderDataModel> callback){
        Call<KaraokeBuilderDataModel> call = karaokeService.statusOfKaraoke(videoid,header,body);

        ApiClient.hitServer(call, new ServiceCallBack<KaraokeBuilderDataModel>(){

            @Override
            public void onSuccess(Response<KaraokeBuilderDataModel> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(String header, String message) {
                callback.onError(header,message);

            }
        });
    }
}

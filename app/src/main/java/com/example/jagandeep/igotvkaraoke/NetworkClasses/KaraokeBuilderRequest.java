package com.example.jagandeep.igotvkaraoke.NetworkClasses;

import com.example.jagandeep.igotvkaraoke.DataModel.KaraokeBuilderDataModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jagandeep on 9/25/17.
 */

public class KaraokeBuilderRequest {
    private KaraokeBuilderService karaokeService;


    public KaraokeBuilderRequest(){
        karaokeService = ApiClient.createService(KaraokeBuilderService.class);

    }

    public void saveKaraokeWithMap(String videoid, Map header, Map<String,RequestBody> file, MultipartBody.Part body, final ServiceCallBack<KaraokeBuilderDataModel> callback){
        Call<KaraokeBuilderDataModel> call = karaokeService.saveKaraokeData(videoid,header,file,body);

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

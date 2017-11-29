package com.example.jagandeep.igotvkaraoke.NetworkClasses;

import com.example.jagandeep.igotvkaraoke.DataModel.AssetsDataModel;
import com.example.jagandeep.igotvkaraoke.Utility.LogUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jagandeep on 5/22/17.
 */

public class GetAssetsFolderRequest {

    private GetAssetsService getFolderAssets;


    public GetAssetsFolderRequest(){
        getFolderAssets = ApiClient.createService(GetAssetsService.class);

    }

    public void getAssetsPath (Map header,String deviceRes, final ServiceCallBack<AssetsDataModel> callBack){

        Map<String,String> map = new HashMap<>();
        map.put("folder", deviceRes);

        Call<AssetsDataModel> call = getFolderAssets.callAssetsPath(header,map);

        ApiClient.hitServer(call, new ServiceCallBack<AssetsDataModel>(){

            @Override
            public void onSuccess(Response<AssetsDataModel> response) {
                LogUtils.LOGD("ASSETS Path","GOT IT");
                callBack.onSuccess(response);
            }

            @Override
            public void onError(String header, String message) {
                LogUtils.LOGD("ASSETS Path","DENIED");
                callBack.onError(header,message);

            }
        });
    }
}

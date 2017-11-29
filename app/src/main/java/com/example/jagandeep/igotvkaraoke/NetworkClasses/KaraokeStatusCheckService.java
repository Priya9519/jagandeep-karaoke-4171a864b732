package com.example.jagandeep.igotvkaraoke.NetworkClasses;

import com.example.jagandeep.igotvkaraoke.DataModel.KaraokeBuilderDataModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by sierra1 on 10/30/17.
 */

public interface KaraokeStatusCheckService {
    @FormUrlEncoded
    @POST("api/p/kstatus/{uploadid}")
    Call<KaraokeBuilderDataModel> statusOfKaraoke(
            @Path("uploadid") String uploadid,
            @HeaderMap Map<String, String> header,
            @Field("key") String key

    );
}

package com.example.jagandeep.igotvkaraoke.NetworkClasses;

import com.example.jagandeep.igotvkaraoke.DataModel.KaraokeBuilderDataModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * Created by jagandeep on 9/25/17.
 */

public interface KaraokeBuilderService {
    @Multipart
    @POST("api/p/kupload/{uploadid}")
    Call<KaraokeBuilderDataModel> saveKaraokeData(
            @Path("uploadid") String uploadid,
            @HeaderMap Map<String, String> header,
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part filea

    );

}

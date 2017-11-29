package com.example.jagandeep.igotvkaraoke.NetworkClasses;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jagandeep on 5/3/17.
 */


public class ApiClient {


    private static Retrofit retrofit = null;
    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        }});

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(ApplicationConfig.getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }




    public static <T> void hitServer(Call<T> call, final ServiceCallBack<T> callback){
        if (isNetworkAvailable()) {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    callback.onSuccess(response);
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    ApplicationConfig.changeIndex();
                    callback.onError("Server Error", t.getMessage());
                }
            });
        }else {
            callback.onError("Internet Connection Error", "Please check your data connection!");
        }

    }

    private static boolean isNetworkAvailable() {
        return  true;
    }
}


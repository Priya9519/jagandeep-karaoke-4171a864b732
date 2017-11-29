package com.example.jagandeep.igotvkaraoke.NetworkClasses;

import retrofit2.Response;

/**
 * Created by jagandeep on 5/3/17.
 */


public interface ServiceCallBack<T>  {
    void onSuccess(Response<T> response);
    void onError(String header, String message);
}
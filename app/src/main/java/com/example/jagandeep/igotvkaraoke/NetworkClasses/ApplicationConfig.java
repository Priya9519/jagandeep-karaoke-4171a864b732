package com.example.jagandeep.igotvkaraoke.NetworkClasses;

import java.util.Random;

/**
 * Created by jagandeep on 5/3/17.
 */

public class ApplicationConfig {
    private static final String [] BASE_URL = {"http://api.msgnaa.info/","http://api.msgnaa.info/","http://api.msgnaa.info/"};
    private static final String AUTHORIZATION = "oauth";

    private static  int index = 0;

    public static String getBaseUrl(){
        return BASE_URL[index];
    }

    public static void changeIndex(){
        int ind = index;
        while (ind == index){
            ind = new Random().nextInt((BASE_URL.length - 0) + 1);
        }
        index = ind;
    }

    public static String getAuthorizationUrl(){
        return AUTHORIZATION;
    }

}

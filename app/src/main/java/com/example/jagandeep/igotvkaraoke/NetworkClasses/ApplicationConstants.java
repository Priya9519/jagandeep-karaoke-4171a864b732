package com.example.jagandeep.igotvkaraoke.NetworkClasses;

/**
 * Created by jagandeep on 9/27/17.
 */

public class ApplicationConstants{

    private static ApplicationConstants   objLogger;

    private ApplicationConstants(){
        //ToDo here
    }

    public static ApplicationConstants getInstance()
    {
        if (objLogger == null)
        {
            objLogger = new ApplicationConstants();
        }
        return objLogger;
    }

    public String packageName = "com.example.jagandeep.igotvomina";
    public String applicationFile = "application.txt";
    public String assetsFile = "assets.txt";

    //API's
//    public static final String getSubApplicationURL = "api/homedata";

}
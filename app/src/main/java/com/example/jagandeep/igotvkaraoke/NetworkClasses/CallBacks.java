package com.example.jagandeep.igotvkaraoke.NetworkClasses;

/**
 * Created by jagandeep on 5/11/17.
 */

public interface CallBacks {

    void callbackObserver(Object obj);
    public interface playerCallBack{
        void onItemClickOnItem(Integer albumId);
        void onPlayingEnd();
    }

}


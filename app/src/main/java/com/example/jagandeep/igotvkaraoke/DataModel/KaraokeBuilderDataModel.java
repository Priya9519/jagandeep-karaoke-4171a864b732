package com.example.jagandeep.igotvkaraoke.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jagandeep on 9/25/17.
 */

public class KaraokeBuilderDataModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("key")
    @Expose
    private String key;


    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String clientIp) {
        this.message = clientIp;
    }

    public String getfile() {
        return file;
    }

    public void setfile(String message) {
        this.file = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

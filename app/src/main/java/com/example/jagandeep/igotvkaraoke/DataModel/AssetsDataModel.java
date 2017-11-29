package com.example.jagandeep.igotvkaraoke.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jagandeep on 9/28/17.
 */

public class AssetsDataModel {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private FolderDataModel data;

    /**
     *
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @return
     *     The data
     */
    public FolderDataModel getData() {
        return data;
    }

    public class FolderDataModel{
        @SerializedName("folder")
        @Expose
        private ArrayList<String> folder;

        /**
         *
         * @return
         *     The Folder
         */
        public ArrayList<String> getFolder() {
            return folder;
        }
    }

}
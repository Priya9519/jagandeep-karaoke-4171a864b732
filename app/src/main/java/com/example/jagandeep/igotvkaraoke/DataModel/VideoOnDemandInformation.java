package com.example.jagandeep.igotvkaraoke.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jagandeep on 5/16/17.
 */

public class VideoOnDemandInformation {

    @SerializedName("video_name")
    @Expose
    private String video_name;
    @SerializedName("default_language")
    @Expose
    private String default_language;
    @SerializedName("other_audio")
    @Expose
    private String other_audio;
    @SerializedName("encryption_key")
    @Expose
    private String encryption_key;
    @SerializedName("cover_image")
    @Expose
    private String cover_image;
    @SerializedName("movie_length")
    @Expose
    private String movie_length;
    @SerializedName("subtitles_available")
    @Expose
    private String subtitles_available;
    @SerializedName("cast")
    @Expose
    private String cast;
    @SerializedName("subtitles_language")
    @Expose
    private String subtitles_language;
    @SerializedName("appUrl")
    @Expose
    private String appUrl;
    @SerializedName("codec")
    @Expose
    private VideoOnDemandCodec codec;
    @SerializedName("genre")
    @Expose
    private String[] genre;
    @SerializedName("category")
    @Expose
    private String[] category;
    @SerializedName("watermark")
    @Expose
    private String watermark;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("video_id")
    @Expose
    private Integer videoId;




    /**
     *
     * @return
     *     The cover_image
     */
    public String getCover_image(){return cover_image;}

    /**
     *
     * @return
     *     The video_id
     */
    public Integer getVideoId(){return videoId;}

    /**
     *
     * @return
     *     The appUrl
     */
    public String getAppUrl(){return appUrl;}

    /**
     *
     * @return
     *     The video title
     */
    public String getTitle() {
        return video_name;
    }

    /**
     *
     * @return
     *     The Star Cast
     */
    public String getStarCast() {
        return cast;
    }

    /**
     *
     * @return
     *     The default language
     */
    public String getDefault_language() {
        return default_language;
    }

    /**
     *
     * @return
     *     The default language
     */
    public VideoOnDemandCodec getVODCodecInfo() {
        return codec;
    }

    /**
     *
     * @return
     *     The Genre
     */
    public String[] getGenre() {
        return genre;
    }

    /**
     *
     * @return
     *     The category
     */
    public String[] getCategories() {
        return category;
    }

    /**
     *
     * @return
     *     The watermark
     */
    public String getWatermark() {
        return watermark;
    }

    /**
     *
     * @return
     *     The Token
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @return
     *     The Path
     */
    public String getPath() {
        return path;
    }

    /**
     *
     * @return
     *     The Artist
     */
    public String getArtist() {
        return artist;
    }


}




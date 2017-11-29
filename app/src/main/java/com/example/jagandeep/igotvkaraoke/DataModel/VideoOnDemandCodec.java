package com.example.jagandeep.igotvkaraoke.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jagandeep on 5/16/17.
 */

public class VideoOnDemandCodec {
    @SerializedName("low_4_3")
    @Expose
    private String[] low_4_3;
    @SerializedName("high_16_9")
    @Expose
    private String[] high_16_9;
    @SerializedName("low_4_3_urls")
    @Expose
    private String[] low_4_3_urls;
    @SerializedName("high_16_9_urls")
    @Expose
    private String[] high_16_9_urls;

    /**
     *
     * @return
     *     The high_16_9_urls
     */
    public String[] getHighResulutionURLs() {
        return high_16_9_urls;
    }

    /**
     *
     * @return
     *     The low_4_3_urls
     */
    public String[] getLowResulutionURLs() {
        return low_4_3_urls;
    }

    /**
     *
     * @return
     *     The high_16_9
     */
    public String[] getHighResulution() {
        return high_16_9;
    }

    /**
     *
     * @return
     *     The low_4_3
     */
    public String[] getLowResulution() {
        return low_4_3;
    }
}

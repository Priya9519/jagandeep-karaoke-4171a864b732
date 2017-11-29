package com.example.jagandeep.igotvkaraoke.DataModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jagandeep on 9/4/17.
 */

public class KaraokeDataModel implements Serializable{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("client_ip")
    @Expose
    private String clientIp;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("genre")
    @Expose
    private ArrayList<String> genre = null;
    @SerializedName("categories")
    @Expose
    private ArrayList<String> categories = null;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("current_page")
    @Expose
    private Integer currentPage;
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("last_page")
    @Expose
    private Integer lastPage;
    @SerializedName("next_page_url")
    @Expose
    private String nextPageUrl;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("prev_page_url")
    @Expose
    private String prevPageUrl;
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("data")
    @Expose
    private ArrayList<VideoOnDemandInformation> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public String getBackground() {
        return background;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ArrayList<VideoOnDemandInformation> getData() {
        return data;
    }

    public void setData(ArrayList<VideoOnDemandInformation> data) {
        this.data = data;
    }
}

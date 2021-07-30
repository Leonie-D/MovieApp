package com.leoniedusart.android.movieapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieList {
    @SerializedName(value = "search", alternate = {"Search"})
    private ArrayList<Movie> search;
    @SerializedName(value = "error", alternate = {"Error"})
    private String error;
    private String totalResults;

    public MovieList(){}

    public ArrayList<Movie> getSearch() {
        return search;
    }

    public String getError() {
        return error;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setSearch(ArrayList<Movie> search) {
        this.search = search;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}

package com.leoniedusart.android.movieapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieList {
    @SerializedName(value = "search", alternate = {"Search"})
    private ArrayList<Movie> search;

    public MovieList(){}

    public ArrayList<Movie> getSearch() {
        return search;
    }

    public void setSearch(ArrayList<Movie> search) {
        this.search = search;
    }
}

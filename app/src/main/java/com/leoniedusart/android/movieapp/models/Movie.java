package com.leoniedusart.android.movieapp.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {
    @SerializedName(value = "title", alternate = {"Title"})
    private String title;
    @SerializedName(value = "released", alternate = {"Released"})
    private String released;
    @SerializedName(value = "director", alternate = {"Director"})
    private String director;
    @SerializedName(value = "plot", alternate = {"Plot"})
    private String plot;
    @SerializedName(value = "awards", alternate = {"Awards"})
    private String awards;
    @SerializedName(value = "genre", alternate = {"Genre"})
    private String genre;
    @SerializedName(value = "actors", alternate = {"Actors"})
    private String actors;
    @SerializedName(value = "poster", alternate = {"Poster"})
    private String poster;

    public Movie(){};

    public String getTitle() {
        return title;
    }

    public String getReleased() {
        return released;
    }

    public String getDirector() {
        return director;
    }

    public String getPlot() {
        return plot;
    }

    public String getAwards() {
        return awards;
    }

    public String getGenre() {
        return genre;
    }

    public String getActors() {
        return actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}

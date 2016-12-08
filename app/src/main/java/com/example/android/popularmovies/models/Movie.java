package com.example.android.popularmovies.models;

import java.net.URL;

/**
 * Created by kristenwoodward on 12/7/16.
 */

public class Movie {

    private String mMovieName;
    private URL mPosterUrl;
    private long mPopularity;
    private long mRating;


    public Movie(String movieName, URL posterUrl, long popularity, long rating){
        mMovieName = movieName;
        mPosterUrl = posterUrl;
        mPopularity = popularity;
        mRating = rating;
    }

    public String getMovieName() {
        return mMovieName;
    }

    public URL getPosterUrl() {
        return mPosterUrl;
    }

    public long getPopularity(){
        return mPopularity;
    }

    public long getRating(){
        return mRating;
    }
}

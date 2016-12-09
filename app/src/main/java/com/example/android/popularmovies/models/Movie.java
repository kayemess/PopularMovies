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
    private String mMovieSynopsis;
    private String mReleaseDate;


    public Movie(String movieName, URL posterUrl, long popularity, long rating, String movieSynopsis, String releaseDate){
        mMovieName = movieName;
        mPosterUrl = posterUrl;
        mPopularity = popularity;
        mRating = rating;
        mMovieSynopsis = movieSynopsis;
        mReleaseDate = releaseDate;
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

    public String getMovieSynopsis(){
        return mMovieSynopsis;
    }

    public long getRating(){
        return mRating;
    }

    public String getReleaseDate(){
        return mReleaseDate;
    }
}

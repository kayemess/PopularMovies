package com.example.android.popularmovies.models;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by kristenwoodward on 12/7/16.
 */

public class Movie implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;

    String mMovieName;
    private String mPosterPath;
    private long mPopularity;
    private long mRating;
    private String mMovieSynopsis;
    private String mReleaseDate;
    private String mMovieId;
    private boolean mIsFavorite = false;

    public Movie(String movieName, String posterPath, @Nullable long popularity, long rating, String movieSynopsis, String releaseDate, String movieId){
        mMovieName = movieName;
        mPosterPath = posterPath;
        mPopularity = popularity;
        mRating = rating;
        mMovieSynopsis = movieSynopsis;
        mReleaseDate = releaseDate;
        mMovieId = movieId;
    }

    public String getMovieName() {
        return mMovieName;
    }

    public String getPosterPath() {
        return mPosterPath;
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

    public String getMovieId() { return mMovieId; }

    public void setIsFavorite(boolean isFavorite) {mIsFavorite = isFavorite;}

    public boolean isFavorite() { return mIsFavorite; }
}

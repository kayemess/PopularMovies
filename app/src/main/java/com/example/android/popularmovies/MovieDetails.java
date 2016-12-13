package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by kristenwoodward on 12/9/16.
 */

public class MovieDetails extends AppCompatActivity {

    private String mTitle;
    private String mSynopsis;
    private String mRating;
    private String mPoster;
    private String mReleaseDate;

    public MovieDetails(){
        //default public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        Intent launchDetailsIntent = getIntent();

        if(launchDetailsIntent.hasExtra("movieTitle")){
            mTitle = launchDetailsIntent.getStringExtra("movieTitle");
            TextView movieTitleTV = (TextView) findViewById(R.id.details_movie_title_tv);
            movieTitleTV.setText(mTitle);
        }

        if(launchDetailsIntent.hasExtra("movieRating")){
            mRating = launchDetailsIntent.getStringExtra("movieRating");
            TextView movieRatingTV = (TextView) findViewById(R.id.details_rating_tv);
            movieRatingTV.setText(mRating + getString(R.string.rating_denominator));
        }

        if(launchDetailsIntent.hasExtra("moviePoster")){
            mPoster = launchDetailsIntent.getStringExtra("moviePoster");
            ImageView moviePosterIV = (ImageView) findViewById(R.id.details_poster_iv);
            Picasso.with(this).load(mPoster).into(moviePosterIV);
        }

        if(launchDetailsIntent.hasExtra("movieReleaseDate")){
            mReleaseDate = launchDetailsIntent.getStringExtra("movieReleaseDate");
            TextView releaseDateTV = (TextView) findViewById(R.id.details_release_date_tv);
            releaseDateTV.setText(getString(R.string.released_on) + " " + mReleaseDate);
        }

        if(launchDetailsIntent.hasExtra("movieSynopsis")){
            mSynopsis = launchDetailsIntent.getStringExtra("movieSynopsis");
            TextView movieSynopsisTV = (TextView) findViewById(R.id.details_movie_synopsis_tv);
            movieSynopsisTV.setText(mSynopsis);
        }
    }
}

package com.example.android.popularmovies.adapters;

import android.database.Cursor;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.models.Movie;

/**
 * Created by kristenwoodward on 1/26/17.
 */

public class CursorAdapter {

    public static Movie[] createListOfFavoriteMovies(Cursor cursor){

        Movie currentMovie;

        // indeces for columns of interest
        int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        int synopsisIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS);
        int releaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
        int ratingIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
        int posterIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);

        int numFaves = cursor.getCount();
        Movie[] listOfFavorites = new Movie[numFaves];

        for(int position = 0; position < numFaves; position++){
            cursor.moveToPosition(position);

            String movieTitle = cursor.getString(titleIndex);
            String movieId = cursor.getString(idIndex);
            String movieSynopsis = cursor.getString(synopsisIndex);
            String movieReleaseDate = cursor.getString(releaseDateIndex);
            int movieRating = cursor.getInt(ratingIndex);
            String moviePoster = cursor.getString(posterIndex);

            currentMovie = new Movie(movieTitle,moviePoster,movieRating,movieRating,movieSynopsis,movieReleaseDate,movieId);

            listOfFavorites[position] = currentMovie;

        }

        return listOfFavorites;
    }
}

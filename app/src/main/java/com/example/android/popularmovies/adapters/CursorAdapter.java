package com.example.android.popularmovies.adapters;

import android.database.Cursor;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.models.Movie;

/**
 * Created by kristenwoodward on 1/26/17.
 */

public class CursorAdapter {

    public static Movie[] createListOfFavoriteMovies(Cursor cursor){
        int titleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        int posterIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);
        int ratingIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
        int synopsisIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS);
        int releaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
        int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

        int numFaves = cursor.getCount();
        Movie[] listOfFavorites = new Movie[numFaves];

        for(int position = 0; position < numFaves; position++){
            cursor.moveToPosition(position);

            listOfFavorites[position] = new Movie(
                    cursor.getString(titleIndex),
                    cursor.getString(posterIndex),
                    cursor.getInt(ratingIndex),
                    cursor.getInt(ratingIndex),
                    cursor.getString(synopsisIndex),
                    cursor.getString(releaseDateIndex),
                    cursor.getString(idIndex));
        }

        return listOfFavorites;
    }
}

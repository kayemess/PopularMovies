package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.popularmovies.R;

/**
 * Created by kristenwoodward on 1/26/17.
 */

public class MovieContract {

    // path for content authority, note that string is also hard-coded into manifest
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    // URI for content based on content authority defined above
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // path that can be appended to base URI to retrieve movie data
    public static final String MOVIE_PATH = "movie";

    // inner class that defines the table columns for movie table
    public static final class MovieEntry implements BaseColumns {

        // base content uri used to query movie table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(MOVIE_PATH)
                .build();

        // internal name of movie table
        public static final String TABLE_NAME = "movie";


        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_SYNOPSIS = "synopsis";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "id";

        // inner class that builds uri for a specific movie ID
        public static Uri buildMovieUriWithId(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }
}

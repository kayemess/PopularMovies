package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.popularmovies.R;

/**
 * Created by kristenwoodward on 1/26/17.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String MOVIE_PATH = "movie";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(MOVIE_PATH)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_SYNOPSIS = "synopsis";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "id";

        public static Uri buildMovieUriWithId(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }
    }
}

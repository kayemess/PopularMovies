package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by kristenwoodward on 1/26/17.
 */

public class MovieProvider extends ContentProvider {


    // these codes will be used to match Uris with the content that the user is looking for
    public static final int CODE_MOVIES = 300;
    public static final int CODE_SINGLE_MOVIE = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {

        UriMatcher newUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        newUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.MOVIE_PATH, CODE_MOVIES);
        newUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.MOVIE_PATH + "/#", CODE_SINGLE_MOVIE);

        return newUriMatcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match){
            case CODE_MOVIES:
                returnCursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_SINGLE_MOVIE:
                returnCursor = db.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri = null;

        switch (match){
            case CODE_MOVIES:
                long rowsInserted = db.insert(
                        MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        values);

                if (rowsInserted > 0){
                    returnUri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(values.getAsString(MovieContract.MovieEntry.COLUMN_MOVIE_ID)).build();
                } else throw new SQLException("Failed to insert row into " + uri);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int rowsDeleted;

        switch (match){
            case CODE_MOVIES:
                // if code matches movie table, delete full table
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        null);
                break;

            case CODE_SINGLE_MOVIE:
                // if code matches single movie, get movie ID from Uri and delete single row
                String mSelection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] mSelectionArgs = new String[]{uri.getLastPathSegment()};

                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

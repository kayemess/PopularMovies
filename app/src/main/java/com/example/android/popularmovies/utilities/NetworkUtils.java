package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.R.attr.apiKey;

/**
 * Utilities to communicate with the MovieDB
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_API_URL = "http://api.themoviedb.org/3/movie/";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String BASE_VIDEO_URL = "https://www.youtube.com/watch";

    private final static String API_PARAM = "api_key";
    private final static String API_KEY = "74ed321b4814256e3d029c9005696147"; //TODO: move to build.gradle

    // options: "w92", "w154", "w185", "w342", "w500", "w780", or "original"
    private final static String POSTER_IMAGE_SIZE = "w500";
    private final static String VIDEO_PARAM = "v";

    public static URL buildApiUrl(String filter_order){
        Uri builtURI = Uri.parse(BASE_API_URL).buildUpon()
                .appendEncodedPath(filter_order)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildMovieDetailsApiUrl(String movieId, String query){
        Uri builtURI = Uri.parse(BASE_API_URL).buildUpon()
                .appendPath(movieId)
                .appendEncodedPath(query)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else
                return null;
        }  finally {
            httpURLConnection.disconnect();
        }
    }

    public static URL buildImageURL(String imagePath){
        Uri builtURI = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath(POSTER_IMAGE_SIZE)
                .appendEncodedPath(imagePath)
                .build();

        URL url = null;

        try {
            url = new URL(builtURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildTrailerURL(String videoKey){
        Uri builtUri = Uri.parse(BASE_VIDEO_URL).buildUpon()
                .appendQueryParameter(VIDEO_PARAM,videoKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
}

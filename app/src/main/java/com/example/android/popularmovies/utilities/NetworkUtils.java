package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Utilities to communicate with the MovieDB
 */

public class NetworkUtils {


    private static final String TAG = NetworkUtils.class.getSimpleName();

    // API information
    public static final String BASE_API_URL = "http://api.themoviedb.org/3/movie/";

    // sort params to be appended to base API URL
    final static String SORT_POPULAR = "popular";
    final static String SORT_TOP_RATED = "top_rated";

    // Information for retrieving poster images
    // The API will return a relative path, for example: “/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg”
    // This will need to be concatenated on to the base URL with a parameter for image size, for example:
    // http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    // param for image size
    // options: "w92", "w154", "w185", "w342", "w500", "w780", or "original"
    final static String POSTER_IMAGE_SIZE = "w500";
    //final static String API_KEY =
    final static String API_PARAM = "api_key";

    public static URL buildApiUrl(String filter_order, String apiKey){
        Uri builtURI = Uri.parse(BASE_API_URL).buildUpon()
                .appendEncodedPath(filter_order)
                .appendQueryParameter(API_PARAM, apiKey)
                .build();

        URL url = null;

        try { url = new URL(builtURI.toString()); }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    // drew from Sunshine app code to define getResponseFromHttpURL method

    /**
     * This method reads in a URL and returns
     * @param url
     * @return
     * @throws IOException
     */
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

}

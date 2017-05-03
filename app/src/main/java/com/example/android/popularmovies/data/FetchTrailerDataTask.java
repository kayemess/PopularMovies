package com.example.android.popularmovies.data;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.listeners.OnFetchTrailerDataCompleted;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.TrailerJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by kristenwoodward on 4/24/17.
 */

public class FetchTrailerDataTask extends AsyncTask<String, Void, Trailer[]> {

    OnFetchTrailerDataCompleted mListener;
    Context mContext;
    private Trailer[] mMovieTrailerList;

    public FetchTrailerDataTask(Context context, OnFetchTrailerDataCompleted listener){
        mListener = listener;
        mContext = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Trailer[] doInBackground(String[] movieId) {
        URL trailerApiUrl = NetworkUtils.buildMovieDetailsApiUrl(movieId[0], NetworkUtils.VIDEO_PATH);

        mMovieTrailerList = null;

        try {
            // get JSON of movie results from the api URL, sorted based on the user's preferences
            String jsonMovieTrailerResults
                    = NetworkUtils.getResponseFromHttpUrl(trailerApiUrl);

            // create an array of movie objects using the JSON
            mMovieTrailerList = TrailerJsonUtils.getMovieTrailersFromJson(mContext, jsonMovieTrailerResults);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mMovieTrailerList;
    }

    @Override
    protected void onPostExecute(Trailer[] movieTrailerResults) {
        mListener.OnFetchTrailerDataCompleted(movieTrailerResults);
    }
}

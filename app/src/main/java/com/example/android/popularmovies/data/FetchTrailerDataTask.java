package com.example.android.popularmovies.data;

import android.os.AsyncTask;

import com.example.android.popularmovies.listeners.OnFetchTrailerDataCompleted;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.utilities.NetworkConstants;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.TrailerJsonUtils;

import java.net.URL;

/**
 * Created by kristenwoodward on 4/24/17.
 */

public class FetchTrailerDataTask extends AsyncTask<String, Void, Trailer[]> {
    private OnFetchTrailerDataCompleted mListener;

    public FetchTrailerDataTask(OnFetchTrailerDataCompleted listener){
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Trailer[] doInBackground(String[] movieId) {
        Trailer[] movieTrailerList = null;

        URL trailerApiUrl = NetworkUtils.buildMovieDetailsApiUrl(movieId[0], NetworkConstants.VIDEO_PATH);

        try {
            String jsonMovieTrailerResults = NetworkUtils.getResponseFromHttpUrl(trailerApiUrl);
            movieTrailerList = TrailerJsonUtils.getMovieTrailersFromJson(jsonMovieTrailerResults);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movieTrailerList;
    }

    @Override
    protected void onPostExecute(Trailer[] movieTrailerResults) {
        mListener.OnFetchTrailerDataCompleted(movieTrailerResults);
    }
}

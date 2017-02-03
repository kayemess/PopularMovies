package com.example.android.popularmovies.models;

import android.support.v7.widget.RecyclerView;

/**
 * Created by kristenwoodward on 1/24/17.
 */

public class Trailer {

    private String mTrailerId;
    private String mTrailerKey;
    private String mTrailerName;
    private String mTrailerType;

    public Trailer(String trailerId, String trailerKey, String trailerName, String trailerType){
        mTrailerId = trailerId;
        mTrailerKey = trailerKey;
        mTrailerName = trailerName;
        mTrailerType = trailerType;
    }

    public String getTrailerId() { return mTrailerId; }

    public String getTrailerKey() { return mTrailerKey; }

    public String getTrailerName() { return mTrailerName; }

    public String getTrailerType() { return mTrailerType; }

}

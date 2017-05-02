package com.example.android.popularmovies.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieProvider;
import com.example.android.popularmovies.ui.MovieDetailsActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.CursorAdapter;
import com.example.android.popularmovies.adapters.MovieListAdapter;
import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by kristenwoodward on 1/26/17.
 */

public class MovieListFragment extends Fragment implements MovieAdapter.ListItemClickListener {

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private static final String FILTER_SELECTION = "filterSelection";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private Movie[] mMovieList;

    private TextView mAddFavoritesTv;

    //String[] that corresponds to menu filter options
    public String[] mFilterOptions = {"popular/","top_rated/",""};
    //this refers to the position that should be referenced in mFilterOptions[]
    private int mFilterSelection = 0;

    private ProgressBar mProgressBar;
    private LinearLayout mErrorMessageView;

    private Bundle mState = null;

    private MovieListAdapter mMovieListAdapter;

    public MovieListFragment() {
        //required public constructor
    }

    public MovieListFragment(MovieListAdapter adapter){
        mMovieListAdapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mErrorMessageView = (LinearLayout) rootView.findViewById(R.id.error_message_view);
        mAddFavoritesTv = (TextView) rootView.findViewById(R.id.need_to_favorite_tv);

        // create a GridLayout with two columns
        int screenWidth = getResources().getConfiguration().screenWidthDp;

        GridLayoutManager layoutManager
                    = new GridLayoutManager(getActivity(), 2);
        if(screenWidth > 500){
            layoutManager
                    = new GridLayoutManager(getActivity(), 3);
        }

        // drop RecyclerView into a GridLayout in activity_movie_list.xml, and lets RecyclerView know that all elements
        // will have a fixed size
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mMovieAdapter.setHasStableIds(true);

        mRecyclerView.setAdapter(mMovieAdapter);

        mFilterSelection = getArguments().getInt(FILTER_SELECTION,0);

        new FetchMovieData().execute(mFilterOptions);

        if(savedInstanceState != null){
            Parcelable savedRecyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
        }

        return rootView;

    }

    /**
     * Override onSaveInstanceState method to save state of recyclerview, so that user can navigate back to
     * where they were in the movie poster list in MainActivity
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mState = outState;
        mState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(mMovieListAdapter != null) {
                mMovieListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClickListener(int position, View posterImageView, String transitionName) {

        //convert movie rating long into a string with a rounded number
        String movieRatingString = String.valueOf(Math.round(mMovieList[position].getRating()));

        Intent showMovieDetails = new Intent(getActivity(), MovieDetailsActivity.class);
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,mMovieList[position].getMovieName());
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_RATING,movieRatingString);
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,mMovieList[position].getPosterPath());
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,mMovieList[position].getReleaseDate());
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS,mMovieList[position].getMovieSynopsis());
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_ID,mMovieList[position].getMovieId());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), posterImageView, transitionName);

        startActivityForResult(showMovieDetails, 1, options.toBundle());
    }


    public class FetchMovieData extends AsyncTask<String,Void,Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mAddFavoritesTv.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String[] filterOrder) {
            String filterChoice = mFilterOptions[mFilterSelection];

            mMovieList = null;

            if(filterChoice == NetworkUtils.SORT_POPULAR || filterChoice == NetworkUtils.SORT_TOP_RATED) {

                String apiKey = getString(R.string.api_key);
                URL apiUrl = NetworkUtils.buildApiUrl(filterChoice,apiKey);

                try {
                    // get JSON of movie results from the api URL, sorted based on the user's preferences
                    String jsonMovieResults
                            = NetworkUtils.getResponseFromHttpUrl(apiUrl);

                    // create an array of movie objects using the JSON
                    mMovieList = MovieJsonUtils.getMoviesFromJson(getActivity(), jsonMovieResults);

                    return mMovieList;
                } catch (IOException e) {
                    e.printStackTrace();
                    return mMovieList;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return mMovieList;
                }
            } else {
                Cursor favorites = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);
                mMovieList = CursorAdapter.createListOfFavoriteMovies(favorites);
                return  mMovieList;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieResults) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if(movieResults != null){
                if(movieResults.length == 0 && mFilterSelection == 2) {
                    mAddFavoritesTv.setVisibility(View.VISIBLE);
                } else {
                    mMovieAdapter.setMovieData(movieResults);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                mErrorMessageView.setVisibility(View.VISIBLE);
            }
        }
    }
}

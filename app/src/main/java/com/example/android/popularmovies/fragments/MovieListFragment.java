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

import com.example.android.popularmovies.ui.MovieDetailsActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.CursorAdapter;
import com.example.android.popularmovies.adapters.MovieListAdapter;
import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by kristenwoodward on 1/26/17.
 */

public class MovieListFragment extends Fragment implements MovieAdapter.ListItemClickListener {

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    public static final String MOVIE_LIST_API_PATH = "fragment_api_path";
    private static final int MOVIE_DETAILS_REQUEST_CODE = 1;

    private MovieAdapter mMovieAdapter;
    private MovieListAdapter mMovieListAdapter;
    private Movie[] mMovieList;

    private TextView mFavoritesEmptyContainer;
    private ProgressBar mProgressBar;
    private LinearLayout mErrorMessageView;
    private RecyclerView mRecyclerView;

    public MovieListFragment() {} //required default public constructor

    public MovieListFragment(MovieListAdapter adapter) {
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
        mFavoritesEmptyContainer = (TextView) rootView.findViewById(R.id.need_to_favorite_tv);

        int screenWidth = getResources().getConfiguration().screenWidthDp;

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        if (screenWidth > 500) {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mMovieAdapter.setHasStableIds(true);

        mRecyclerView.setAdapter(mMovieAdapter);

        String movieListApiPath = getArguments().getString(MOVIE_LIST_API_PATH, NetworkUtils.PATH_POPULAR);
        new FetchMovieData(movieListApiPath).execute();

        if (savedInstanceState != null) {
            Parcelable savedRecyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
        }

        return rootView;
    }

    /**
     * Override onSaveInstanceState method to save state of recyclerview, so that user can navigate back to
     * where they were in the movie poster list in MainActivity
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onItemClickListener(int position, View posterImageView, String transitionName) {
        String movieRating = String.valueOf(Math.round(mMovieList[position].getRating()));

        Intent showMovieDetails = new Intent(getActivity(), MovieDetailsActivity.class);
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, mMovieList[position].getMovieName());
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, movieRating);
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, mMovieList[position].getPosterPath());
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mMovieList[position].getReleaseDate());
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, mMovieList[position].getMovieSynopsis());
        showMovieDetails.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieList[position].getMovieId());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), posterImageView, transitionName);

        startActivityForResult(showMovieDetails, MOVIE_DETAILS_REQUEST_CODE, options.toBundle());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // updates movie list in case movie was added/removed from favorites in detail page
        // TODO: add check for whether favorites list has changed
        if (requestCode == MOVIE_DETAILS_REQUEST_CODE) {
            if (mMovieListAdapter != null) {
                mMovieListAdapter.notifyDataSetChanged();
            }
        }
    }

    //TODO: move into separate class and write unit test
    public class FetchMovieData extends AsyncTask<Void, Void, Movie[]> {

        private String mMovieListApiPath;

        FetchMovieData(String movieListApiPath) {
            mMovieListApiPath = movieListApiPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mFavoritesEmptyContainer.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Movie[] doInBackground(Void... params) {
            mMovieList = null;

            switch (mMovieListApiPath) {
                case NetworkUtils.PATH_POPULAR:
                case NetworkUtils.PATH_TOP_RATED:
                    URL apiUrl = NetworkUtils.buildApiUrl(mMovieListApiPath);

                    try {
                        String jsonMovieResults = NetworkUtils.getResponseFromHttpUrl(apiUrl);
                        mMovieList = MovieJsonUtils.getMoviesFromJson(getActivity(), jsonMovieResults);

                        return mMovieList;
                    } catch (Exception e) {
                        e.printStackTrace();

                        return null;
                    }
                case NetworkUtils.PATH_FAVORTIES:
                default:
                    Cursor favorites = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
                    mMovieList = CursorAdapter.createListOfFavoriteMovies(favorites);

                    return mMovieList;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieResults) {
            mProgressBar.setVisibility(View.INVISIBLE);

            if (movieResults != null) {
                if (movieResults.length == 0 && mMovieListApiPath.equals(NetworkUtils.PATH_FAVORTIES)) {
                    mFavoritesEmptyContainer.setVisibility(View.VISIBLE);
                } else {
                    mFavoritesEmptyContainer.setVisibility(View.GONE);
                    mMovieAdapter.setMovieData(movieResults);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                mErrorMessageView.setVisibility(View.VISIBLE);
            }
        }
    }
}


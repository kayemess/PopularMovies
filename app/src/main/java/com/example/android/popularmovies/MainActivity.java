package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;


import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private static final String FILTER_SELECTION = "filterSelection";
    private static final String RECYCLER_VIEW = "recyclerView";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private Movie[] mMovieList;

    //String[] that corresponds to menu filter options
    private String[] mFilterOptions = {"popular/","top_rated/"};
    //this refers to the position that should be referenced in mFilterOptions[]
    private int mFilterSelection = 0;

    private ProgressBar mProgressBar;
    private LinearLayout mErrorMessageView;

    private Bundle mState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mErrorMessageView = (LinearLayout) findViewById(R.id.error_message_view);

        // create a GridLayout with two columns
        GridLayoutManager layoutManager
                = new GridLayoutManager(this,2);

        // drop RecyclerView into a GridLayout in activity_main.xml, and let's RecyclerView know that all elements
        // will have a fixed size
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mMovieAdapter.setHasStableIds(true);

        mRecyclerView.setAdapter(mMovieAdapter);

        new FetchMovieData().execute(mFilterOptions);

        if(savedInstanceState != null){
            Parcelable savedRecyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
        }

    }

    /**
     * Infalte filter options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.most_popular_action){
            // switch mFilter selection to position in mFilterOptions[] that corresponds to most popular filter choice
            mFilterSelection = 0;
            new FetchMovieData().execute(mFilterOptions);
            return true;
        } else if(id == R.id.top_rated_action){
            // switch mFilterSelection to position in mFilterOptions[] that corresponds to top rated choice
            mFilterSelection = 1;
            new FetchMovieData().execute(mFilterOptions);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    /**
     * Override onSaveInstanceState method to save state of recyclerview, so that user can navigate back to
     * where they were in the movie poster list in MainActivity
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    /**
     * If MainActivity has a has a savedInstanceState, use that state when MainActivity regains focus. This takes user
     * to their previous position in the list (before they clicked on a movieDetails).
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            Parcelable savedRecyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
        }
    }

    @Override
    public void onItemClickListener(int position) {

        //convert movie rating long into a string with a rounded number
        String movieRatingString = String.valueOf(Math.round(mMovieList[position].getRating()));

        Intent showMovieDetails = new Intent(MainActivity.this, MovieDetails.class);
        showMovieDetails.putExtra("movieTitle",mMovieList[position].getMovieName());
        showMovieDetails.putExtra("movieRating",movieRatingString);
        showMovieDetails.putExtra("moviePoster",mMovieList[position].getPosterUrl().toString());
        showMovieDetails.putExtra("movieReleaseDate",mMovieList[position].getReleaseDate());
        showMovieDetails.putExtra("movieSynopsis",mMovieList[position].getMovieSynopsis());
        startActivity(showMovieDetails);
    }

    /**
    public void showMovieData(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }
     */

    public class FetchMovieData extends AsyncTask<String,Void,Movie[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            Log.v("main,onPreExecute","preExecute executed");
        }

        @Override
        protected Movie[] doInBackground(String[] filterOrder) {
            String apiKey = getString(R.string.api_key);
            String filterChoice = filterOrder[mFilterSelection];
            URL apiUrl = NetworkUtils.buildApiUrl(filterChoice,apiKey);

            mMovieList = null;

            try {
                // get JSON of movie results from the api URL, sorted based on the user's preferences
                String jsonMovieResults
                        = NetworkUtils.getResponseFromHttpUrl(apiUrl);

                // create an array of movie objects using the JSON
                mMovieList = MovieJsonUtils.getMoviesFromJson(MainActivity.this,jsonMovieResults);

                return mMovieList;
            }
            catch (IOException e) {
                e.printStackTrace();
                return mMovieList;
            } catch (JSONException e) {
                e.printStackTrace();
                return mMovieList;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieResults) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if(movieResults != null){
                mMovieAdapter.setMovieData(movieResults);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else
                mErrorMessageView.setVisibility(View.VISIBLE);

        }
    }
}

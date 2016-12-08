package com.example.android.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.ImageView;

import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.MovieJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView mMoviePosterIv;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // member variables for
        //mMoviePosterIv = (ImageView) findViewById(R.id.movie_poster_iv);
        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        String[] filterOptions = {"popular/","top_rated/"};

        GridLayoutManager layoutManager
                = new GridLayoutManager(this,2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter();

        mRecyclerView.setAdapter(mMovieAdapter);

        new FetchMovieData().execute(filterOptions);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_order, menu);
        return true;
    }

    public class FetchMovieData extends AsyncTask<String,Void,Movie[]>{

        @Override
        protected Movie[] doInBackground(String[] filterOrder) {
            String apiKey = getString(R.string.api_key);
            String filterChoice = filterOrder[0];
            URL apiUrl = NetworkUtils.buildApiUrl(filterChoice,apiKey);

            Movie[] movieResults = null;

            try {
                // get JSON of movie results from the api URL, sorted based on the user's preferences
                String jsonMovieResults
                        = NetworkUtils.getResponseFromHttpUrl(apiUrl);

                // create an array of movie objects using the JSON
                movieResults = MovieJsonUtils.getMoviesFromJson(MainActivity.this,jsonMovieResults);

                return movieResults;
            }
            catch (IOException e) {
                e.printStackTrace();
                return movieResults;
            } catch (JSONException e) {
                e.printStackTrace();
                return movieResults;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieResults) {
            if(movieResults != null){
                mMovieAdapter.setMovieData(movieResults);
            }

        }
    }
}

package com.example.oi156f.popularmovies;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oi156f.popularmovies.adapters.MovieAdapter;
import com.example.oi156f.popularmovies.utilities.MovieUtils;

import java.net.URL;
import java.util.Arrays;

/**
 * Created by oi156f on 8/28/2017.
 * AsyncTask to fetch movies for main movie grid
 */

class FetchMoviesTask extends AsyncTask<Integer, Void, Movie[]> {

    private Activity mActivity;
    private View rootView;
    private GridView moviesGrid;
    private ProgressBar loadingIcon;
    private TextView genericError;

    FetchMoviesTask(Activity activity, View view) {
        mActivity = activity;
        rootView = view;
        moviesGrid = (GridView) rootView.findViewById(R.id.movies_grid);
        loadingIcon = (ProgressBar) rootView.findViewById(R.id.loading_icon);
        genericError = (TextView) rootView.findViewById(R.id.error_generic);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingIcon.setVisibility(View.VISIBLE);
    }

    @Override
    protected Movie[] doInBackground(Integer... params) {
        int sorting = params[0];
        URL movieUrl = MovieUtils.buildUrl(sorting);
        //TODO: if favorite, query to get cursor
        //TODO: convert cursor to Movie[], use MovieUtil

        try {
            String moviesJson = MovieUtils.getResponseFromHttpUrl(movieUrl);
            return MovieUtils.getMoviesFromJson(moviesJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        loadingIcon.setVisibility(View.INVISIBLE);
        if(movies != null) {
            MovieAdapter adapter = new MovieAdapter(mActivity, Arrays.asList(movies));
            moviesGrid.setAdapter(adapter);
        } else {
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        moviesGrid.setVisibility(View.INVISIBLE);
        genericError.setVisibility(View.VISIBLE);
    }
}

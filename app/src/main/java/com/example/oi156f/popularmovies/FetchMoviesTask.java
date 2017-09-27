package com.example.oi156f.popularmovies;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oi156f.popularmovies.adapters.MovieAdapter;
import com.example.oi156f.popularmovies.data.FavoritesContract;
import com.example.oi156f.popularmovies.utilities.MovieUtils;

import java.net.URL;
import java.util.Arrays;

/**
 * Created by Omar on 8/28/2017.
 * AsyncTask to fetch movies for main movie grid
 */

class FetchMoviesTask extends AsyncTask<Integer, Void, Movie[]> {

    private final Activity mActivity;
    private final GridView moviesGrid;
    private final ProgressBar loadingIcon;
    private final TextView genericError;
    private final TextView noFavError;

    FetchMoviesTask(Activity activity, View view) {
        mActivity = activity;
        moviesGrid = (GridView) view.findViewById(R.id.movies_grid);
        loadingIcon = (ProgressBar) view.findViewById(R.id.loading_icon);
        genericError = (TextView) view.findViewById(R.id.error_generic);
        noFavError = (TextView) view.findViewById(R.id.error_no_favorites);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingIcon.setVisibility(View.VISIBLE);
    }

    @Override
    protected Movie[] doInBackground(Integer... params) {
        int sorting = params[0];
        if (sorting == MovieUtils.SORT_FAVORITES) {
            try {
                Cursor cursor = mActivity.getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI, null, null, null, null);
                return MovieUtils.getFavoriteMoviesFromCursor(cursor);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        URL movieUrl = MovieUtils.buildUrl(sorting);

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
        if (movies == null)
            showErrorMessage();
        else if (movies.length == 0)
            showNoFavMessage();
        else {
            MovieAdapter adapter = new MovieAdapter(mActivity, Arrays.asList(movies));
            moviesGrid.setAdapter(adapter);
        }
    }

    private void showErrorMessage() {
        moviesGrid.setVisibility(View.INVISIBLE);
        noFavError.setVisibility(View.INVISIBLE);
        genericError.setVisibility(View.VISIBLE);
    }

    private void showNoFavMessage() {
        moviesGrid.setVisibility(View.INVISIBLE);
        genericError.setVisibility(View.INVISIBLE);
        noFavError.setVisibility(View.VISIBLE);
    }
}

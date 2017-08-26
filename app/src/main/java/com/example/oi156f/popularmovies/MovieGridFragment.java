package com.example.oi156f.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oi156f.popularmovies.utilities.MovieUtils;

import java.net.URL;
import java.util.Arrays;

public class MovieGridFragment extends Fragment {

    private MovieAdapter adapter;
    private GridView moviesGrid;
    private TextView genericError;
    private ProgressBar loadingIcon;

    public MovieGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        moviesGrid = (GridView) rootView.findViewById(R.id.movies_grid);
        genericError = (TextView) rootView.findViewById(R.id.error_generic);
        loadingIcon = (ProgressBar) rootView.findViewById(R.id.loading_icon);
        loadMoviePosters(MovieUtils.SORT_POPULAR);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_popular) {
            loadMoviePosters(MovieUtils.SORT_POPULAR);
            return true;
        } else if(id == R.id.sort_top_rated) {
            loadMoviePosters(MovieUtils.SORT_TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMoviePosters(int sorting) {
        showMoviePostersView();
        new FetchMoviesTask().execute(sorting);
    }

    private void showMoviePostersView() {
        genericError.setVisibility(View.INVISIBLE);
        moviesGrid.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        moviesGrid.setVisibility(View.INVISIBLE);
        genericError.setVisibility(View.VISIBLE);
    }

    public class FetchMoviesTask extends AsyncTask<Integer, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIcon.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(Integer... params) {
            int sorting = params[0];
            URL movieUrl = MovieUtils.buildUrl(sorting);

            try {
                String moviesJson = MovieUtils.getResponseFromHttpUrl(movieUrl);
                Movie[] movies = MovieUtils.getMoviesFromJson(getActivity(), moviesJson);
                return movies;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            loadingIcon.setVisibility(View.INVISIBLE);
            if(movies != null) {
                adapter = new MovieAdapter(getActivity(), Arrays.asList(movies));
                moviesGrid.setAdapter(adapter);
            } else {
                showErrorMessage();
            }
        }
    }
}
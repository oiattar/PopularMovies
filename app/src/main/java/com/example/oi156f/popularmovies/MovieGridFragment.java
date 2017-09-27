package com.example.oi156f.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.oi156f.popularmovies.adapters.MovieAdapter;
import com.example.oi156f.popularmovies.utilities.MovieUtils;

import java.util.ArrayList;

public class MovieGridFragment extends Fragment {

    private View rootView;
    private GridView moviesGrid;
    private TextView genericError;
    private TextView noFavError;
    private int currentSorting;
    private MovieAdapter movieAdapter;

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
        rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        moviesGrid = (GridView) rootView.findViewById(R.id.movies_grid);
        genericError = (TextView) rootView.findViewById(R.id.error_generic);
        noFavError = (TextView) rootView.findViewById(R.id.error_no_favorites);
        if (savedInstanceState != null) {
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList("movieAdapter");
            movieAdapter = new MovieAdapter(getActivity(), movies);
            moviesGrid.setAdapter(movieAdapter);
        } else {
            loadMoviePosters(MovieUtils.SORT_POPULAR);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentSorting == MovieUtils.SORT_FAVORITES)
            loadMoviePosters(MovieUtils.SORT_FAVORITES);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        movieAdapter = (MovieAdapter) moviesGrid.getAdapter();
        outState.putParcelableArrayList("movieAdapter", movieAdapter.getItems());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.sort_popular:
                loadMoviePosters(MovieUtils.SORT_POPULAR);
                return true;
            case R.id.sort_top_rated:
                loadMoviePosters(MovieUtils.SORT_TOP_RATED);
                return true;
            case R.id.sort_favorites:
                loadMoviePosters(MovieUtils.SORT_FAVORITES);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void loadMoviePosters(int sorting) {
        currentSorting = sorting;
        showMoviePostersView();
        new FetchMoviesTask(getActivity(), rootView).execute(sorting);
    }

    private void showMoviePostersView() {
        genericError.setVisibility(View.INVISIBLE);
        noFavError.setVisibility(View.INVISIBLE);
        moviesGrid.setVisibility(View.VISIBLE);
    }
}
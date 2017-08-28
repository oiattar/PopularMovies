package com.example.oi156f.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    private View rootView;
    private GridView moviesGrid;
    private TextView genericError;

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
        loadMoviePosters(MovieUtils.SORT_POPULAR);
        return rootView;
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
        new FetchMoviesTask(getActivity(), rootView).execute(sorting);
    }

    private void showMoviePostersView() {
        genericError.setVisibility(View.INVISIBLE);
        moviesGrid.setVisibility(View.VISIBLE);
    }
}
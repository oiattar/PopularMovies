package com.example.oi156f.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.example.oi156f.popularmovies.utilities.MovieUtils;

import java.net.URL;
import java.util.Arrays;

public class MainActivityFragment extends Fragment {

    private MovieAdapter adapter;
    private GridView moviesGrid;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        moviesGrid = (GridView) rootView.findViewById(R.id.movies_grid);
        new FetchMoviesTask().execute();

        return rootView;
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            URL movieUrl = MovieUtils.buildUrl();

            try {
                String moviesJson = MovieUtils.getResponseFromHttpUrl(movieUrl);
                Log.d("OMAR", moviesJson);
                String[] posterPaths = MovieUtils.getPosterFromJson(getActivity(), moviesJson);
                Log.d("OMAR", posterPaths.toString());
                return posterPaths;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] posters) {
            adapter = new MovieAdapter(getActivity(), Arrays.asList(posters));
            moviesGrid.setAdapter(adapter);
        }
    }
}
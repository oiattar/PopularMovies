package com.example.oi156f.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.oi156f.popularmovies.data.FavoritesContract.*;

import com.example.oi156f.popularmovies.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.movie_title) TextView title;
    @BindView(R.id.movie_image) ImageView image;
    @BindView(R.id.movie_date) TextView date;
    @BindView(R.id.movie_rating) TextView rating;
    @BindView(R.id.movie_synopsis) TextView synopsis;
    @BindView(R.id.favorite_button) ToggleButton favoriteButton;

    private Movie movie = null;

    private Unbinder unbinder;

    private boolean isFavorite;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        favoriteButton.setOnClickListener(this);
        Intent intent = getActivity().getIntent();
        if (savedInstanceState != null) {
            isFavorite = savedInstanceState.getBoolean(MovieUtils.FAVORITES_KEY);
        }
        if(intent.hasExtra(getString(R.string.intent_tag))) {
            movie = intent.getParcelableExtra(getString(R.string.intent_tag));
            new FetchDetailsTask(getActivity(), rootView).execute(movie);
            title.setText(movie.getTitle());
            Picasso.with(getActivity()).load(movie.getPoster()).into(image);
            date.setText(movie.getReleaseDate().substring(0, 4));
            rating.setText(getString(R.string.movie_rating, movie.getRating()));
            synopsis.setText(movie.getOverview());
            favoriteButton.setChecked(isFavorite);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(MovieUtils.FAVORITES_KEY, isFavorite);
    }

    public void addFavorite() {

        if (movie == null) return;

        ContentValues contentValues = new ContentValues();

        contentValues.put(FavoritesEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(FavoritesEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(FavoritesEntry.COLUMN_POSTER_PATH, movie.getPoster());
        contentValues.put(FavoritesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoritesEntry.COLUMN_RUNTIME, movie.getRuntime());
        contentValues.put(FavoritesEntry.COLUMN_RATING, movie.getRating());
        contentValues.put(FavoritesEntry.COLUMN_SYNOPSIS, movie.getOverview());

        Uri uri = getContext().getContentResolver().insert(FavoritesEntry.CONTENT_URI, contentValues);

        if(uri != null) {
            Toast.makeText(getActivity().getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteFavorite() {
        Uri uri = FavoritesEntry.CONTENT_URI;
        String mSelection = FavoritesEntry.COLUMN_MOVIE_ID + "=?";
        String[] mSelectionArgs = new String[]{Integer.toString(movie.getId())};
        int numDeleted = getContext().getContentResolver().delete(uri, mSelection, mSelectionArgs);

        String message = "deleted " + numDeleted + " " + movie.getTitle();

        Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        if (favoriteButton.isChecked()) {
            addFavorite();
            isFavorite = true;
        }
        else if (!(favoriteButton.isChecked())) {
            deleteFavorite();
            isFavorite = false;
        }
    }
}

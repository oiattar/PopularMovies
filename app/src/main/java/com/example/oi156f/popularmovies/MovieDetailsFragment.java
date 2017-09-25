package com.example.oi156f.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsFragment extends Fragment {
    @BindView(R.id.movie_title) TextView title;
    @BindView(R.id.movie_image) ImageView image;
    @BindView(R.id.movie_date) TextView date;
    @BindView(R.id.movie_rating) TextView rating;
    @BindView(R.id.movie_synopsis) TextView synopsis;
    private Unbinder unbinder;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();
        if(intent.hasExtra(getString(R.string.intent_tag))) {
            Movie movie = intent.getParcelableExtra(getString(R.string.intent_tag));
            new FetchDetailsTask(getActivity(), rootView).execute(movie);
            title.setText(movie.getTitle());
            Picasso.with(getActivity()).load(movie.getPoster()).into(image);
            date.setText(movie.getReleaseDate().substring(0, 4));
            rating.setText(getString(R.string.movie_rating, movie.getRating()));
            synopsis.setText(movie.getOverview());
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

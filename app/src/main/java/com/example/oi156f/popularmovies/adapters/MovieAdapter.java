package com.example.oi156f.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.oi156f.popularmovies.Movie;
import com.example.oi156f.popularmovies.MovieDetailsActivity;
import com.example.oi156f.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private final Context context;

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }

        if(movie != null) {
            ImageView posterView = (ImageView) convertView.findViewById(R.id.movie_poster);
            String posterPath = movie.getPoster();
            Picasso.with(context).load(posterPath).into(posterView);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    intent.putExtra(context.getString(R.string.intent_tag), movie);
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
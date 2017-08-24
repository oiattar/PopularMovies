package com.example.oi156f.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by oi156f on 8/24/2017.
 */

class MovieAdapter extends ArrayAdapter<String> {
    private final Context context;

    public MovieAdapter(Activity context, List<String> posters) {
        super(context, 0, posters);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }

        ImageView poster = (ImageView) convertView.findViewById(R.id.movie_image);
        /*ImageView poster;
        if(convertView == null) {
            poster = new ImageView(context);
        } else {
            poster = (ImageView) convertView;
        }*/
        Picasso.with(context).load(url).into(poster);

        return convertView;
    }
}

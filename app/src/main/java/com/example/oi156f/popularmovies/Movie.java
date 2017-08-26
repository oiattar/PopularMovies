package com.example.oi156f.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oi156f on 8/25/2017.
 */

public class Movie implements Parcelable {

    int id;
    String title;
    String poster;
    String overview;
    int rating;
    String releaseDate;

    public Movie() {}

    public Movie(int id, String title, String poster, String overview, int rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel source) {
        id = source.readInt();
        title = source.readString();
        poster = source.readString();
        overview = source.readString();
        rating = source.readInt();
        releaseDate = source.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeInt(rating);
        dest.writeString(releaseDate);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

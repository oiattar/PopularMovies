package com.example.oi156f.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String poster;
    private String overview;
    private double rating;
    private int runtime;
    private String releaseDate;
    private Trailer[] trailers;
    private Review[] reviews;

    public Movie(int id, String title, String poster, String overview, double rating, int runtime, String releaseDate) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.rating = rating;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
    }

    public Movie() {}

    private Movie(Parcel source) {
        id = source.readInt();
        title = source.readString();
        poster = source.readString();
        overview = source.readString();
        rating = source.readDouble();
        runtime = source.readInt();
        releaseDate = source.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getTitle() {
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

    String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    int getRuntime() {
        return runtime;
    }

    void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    Trailer[] getTrailers() {
        return trailers;
    }

    void setTrailers(Trailer[] trailers) {
        this.trailers = trailers;
    }

    Review[] getReviews() {
        return reviews;
    }

    void setReviews(Review[] reviews) {
        this.reviews = reviews;
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
        dest.writeDouble(rating);
        dest.writeInt(runtime);
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

    public class Trailer {
        private String name;
        private String path;

        public Trailer() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public class Review {
        private String author;
        private String content;

        public Review() {}

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}

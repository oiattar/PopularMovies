package com.example.oi156f.popularmovies.utilities;

import android.database.Cursor;
import android.net.Uri;

import com.example.oi156f.popularmovies.BuildConfig;
import com.example.oi156f.popularmovies.Movie;
import com.example.oi156f.popularmovies.Movie.*;
import com.example.oi156f.popularmovies.data.FavoritesContract.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public final class MovieUtils {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String POPULAR_MOVIES = "popular";
    private static final String TOP_RATED_MOVIES = "top_rated";
    private static final String TRAILERS_URL = "/videos";
    private static final String REVIEWS_URL = "/reviews";
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185";
    private static final String BACKDROP_SIZE = "w500";

    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static final String API_PARAM = "api_key";

    public static final int SORT_POPULAR = 0;
    public static final int SORT_TOP_RATED = 1;
    public static final int SORT_FAVORITES = 2;
    public static final int TRAILERS = 3;
    public static final int REVIEWS = 4;
    public static final int ALL = 5;

    public static final String FAVORITES_KEY = "favorites";

    /**
     * Builds the URL used to talk to the movie database
     *
     * @param sorting specifies sorting method
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(int sorting) {
        String baseUrl;
        switch(sorting) {
            case(SORT_POPULAR):
                baseUrl = BASE_URL + POPULAR_MOVIES;
                break;
            case(SORT_TOP_RATED):
                baseUrl = BASE_URL + TOP_RATED_MOVIES;
                break;
            default:
                baseUrl = BASE_URL + POPULAR_MOVIES;
        }
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildDetailsUrl(int id, int type) {
        String baseUrl;
        switch(type) {
            case(TRAILERS):
                baseUrl = BASE_URL + Integer.toString(id) + TRAILERS_URL;
                break;
            case(REVIEWS):
                baseUrl = BASE_URL + Integer.toString(id) + REVIEWS_URL;
                break;
            default:
                baseUrl = BASE_URL + Integer.toString(id);
        }

        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String buildImageUrl(String path, String size) {
        return IMAGE_URL + size + path;
    }

    public static Movie[] getMoviesFromJson(String moviesJsonStr)
            throws JSONException {

        final String RESULTS = "results";
        final String ID = "id";
        final String TITLE = "original_title";
        final String POSTER = "poster_path";
        final String BACKDROP = "backdrop_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String RATING = "vote_average";
        final String ERROR = "cod";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (moviesJson.has(ERROR)) {
            int errorCode = moviesJson.getInt(ERROR);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray jMoviesArray = moviesJson.getJSONArray(RESULTS);

        Movie[] movies = new Movie[jMoviesArray.length()];

        for(int i = 0; i < jMoviesArray.length(); i++) {
            JSONObject jMovie = jMoviesArray.getJSONObject(i);
            Movie movie = new Movie();
            movie.setId(jMovie.getInt(ID));
            movie.setTitle(jMovie.getString(TITLE));
            movie.setOverview(jMovie.getString(OVERVIEW));
            movie.setPoster(buildImageUrl(jMovie.getString(POSTER), POSTER_SIZE));
            movie.setBackdrop(buildImageUrl(jMovie.getString(BACKDROP), BACKDROP_SIZE));
            movie.setRating(jMovie.getDouble(RATING));
            movie.setReleaseDate(jMovie.getString(RELEASE_DATE));
            movies[i] = movie;
        }

        return movies;
    }

    public static Trailer[] getTrailersFromJson(String trailersJsonStr)
            throws JSONException {

        final String RESULTS = "results";
        final String NAME = "name";
        final String SITE = "site";
        final String TYPE = "type";
        final String KEY = "key";
        final String ERROR = "cod";

        final String YOUTUBE = "YouTube";
        final String TRAILER = "Trailer";

        JSONObject trailersJson = new JSONObject(trailersJsonStr);

        /* Is there an error? */
        if (trailersJson.has(ERROR)) {
            int errorCode = trailersJson.getInt(ERROR);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray jTrailersArray = trailersJson.getJSONArray(RESULTS);

        Trailer[] temp = new Trailer[jTrailersArray.length()];
        int trailerCount = 0;

        for(int i = 0; i < jTrailersArray.length(); i++) {
            JSONObject jTrailer = jTrailersArray.getJSONObject(i);
            String site = jTrailer.getString(SITE);
            String name = jTrailer.getString(NAME);
            String type = jTrailer.getString(TYPE);
            String key = jTrailer.getString(KEY);

            if(site.equals(YOUTUBE) && type.equals(TRAILER)) {
                Trailer trailer = new Movie().new Trailer();
                trailer.setName(name);
                trailer.setPath(BASE_YOUTUBE_URL + key);
                temp[i] = trailer;
                trailerCount++;
            } else
                temp[i] = null;
        }

        Trailer[] trailers = new Trailer[trailerCount];
        int pos = 0;
        for (Trailer aTemp : temp) {
            if (aTemp != null) {
                trailers[pos] = aTemp;
                pos++;
            }
        }

        return trailers;
    }

    public static Review[] getReviewsFromJson(String reviewsJsonStr)
            throws JSONException {

        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String ERROR = "cod";

        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);

        /* Is there an error? */
        if (reviewsJson.has(ERROR)) {
            int errorCode = reviewsJson.getInt(ERROR);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray jReviewsArray = reviewsJson.getJSONArray(RESULTS);

        Review[] reviews = new Review[jReviewsArray.length()];

        for(int i = 0; i < jReviewsArray.length(); i++) {
            JSONObject jReview = jReviewsArray.getJSONObject(i);
            Review review = new Movie().new Review();
            review.setAuthor(jReview.getString(AUTHOR));
            review.setContent(jReview.getString(CONTENT));
            reviews[i] = review;
        }

        return reviews;
    }

    public static Movie[] getFavoriteMoviesFromCursor(Cursor cursor) {
        ArrayList<Movie> favoriteMovies = new ArrayList<>();

        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_TITLE));
            int id = cursor.getInt(cursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_ID));
            String poster = cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_POSTER_PATH));
            String backdrop = cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_BACKDROP_PATH));
            String synopsis = cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_SYNOPSIS));
            double rating = cursor.getDouble(cursor.getColumnIndex(FavoritesEntry.COLUMN_RATING));
            int runtime = cursor.getInt(cursor.getColumnIndex(FavoritesEntry.COLUMN_RUNTIME));
            String releaseDate = cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_RELEASE_DATE));

            Movie movie = new Movie(id, title, poster, backdrop, synopsis, rating, runtime, releaseDate);

            favoriteMovies.add(movie);
        }

        return favoriteMovies.toArray(new Movie[favoriteMovies.size()]);
    }

    public static int getRuntimeFromJson(String detailsJsonStr)
        throws JSONException {

        final String RUNTIME = "runtime";
        final String ERROR = "cod";

        JSONObject detailsJson = new JSONObject(detailsJsonStr);

        /* Is there an error? */
        if (detailsJson.has(ERROR)) {
            int errorCode = detailsJson.getInt(ERROR);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return -1;
                default:
                    /* Server probably down */
                    return -1;
            }
        }

        return detailsJson.getInt(RUNTIME);
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

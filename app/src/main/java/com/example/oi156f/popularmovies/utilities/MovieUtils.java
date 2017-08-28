package com.example.oi156f.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.oi156f.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class MovieUtils {

    private static final String TAG = MovieUtils.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String POPULAR_MOVIES = "/movie/popular";
    private static final String TOP_RATED_MOVIES = "/movie/top_rated";
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    private static final String API_KEY = "7b1339c449a41e9cd8437a0cfec5930a";
    private static final String API_PARAM = "api_key";

    public static final int SORT_POPULAR = 0;
    public static final int SORT_TOP_RATED = 1;

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

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    private static String buildImageUrl(String poster) {
        return IMAGE_URL + poster;
    }

    public static Movie[] getMoviesFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        final String RESULTS = "results";
        final String ID = "id";
        final String TITLE = "original_title";
        final String POSTER = "poster_path";
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
            movie.setPoster(buildImageUrl(jMovie.getString(POSTER)));
            movie.setRating(jMovie.getDouble(RATING));
            movie.setReleaseDate(jMovie.getString(RELEASE_DATE));
            movies[i] = movie;
        }

        return movies;
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

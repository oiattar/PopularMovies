package com.example.oi156f.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by oi156f on 8/24/2017.
 */

public final class MovieUtils {

    private static final String TAG = MovieUtils.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org/3";

    private static final String POPULAR_MOVIES = "/movie/popular";

    private static final String TOP_RATED_MOVIES = "/movie/top_rated";

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    private static final String API_KEY = "";

    private static final String API_PARAM = "api_key";

    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL + POPULAR_MOVIES).buildUpon()
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

    public static String buildImageUrl(String poster) {
        String url = IMAGE_URL + poster;
        return url;
    }

    public static String[] getPosterFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        final String RESULTS = "results";
        final String POSTER = "poster_path";
        final String ERROR = "cod";

        String[] posterPaths = null;

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

        JSONArray moviesArray = moviesJson.getJSONArray(RESULTS);

        posterPaths = new String[moviesArray.length()];

        for(int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            String path = movie.getString(POSTER);
            posterPaths[i] = buildImageUrl(path);
        }

        return posterPaths;
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

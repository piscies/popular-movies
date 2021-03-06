package br.com.marcosaraiva.popularmovies.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import br.com.marcosaraiva.popularmovies.BuildConfig;

/**
 * This class handles Network features.
 * The method "getResponseFromHttpUrl" was copied from Udacity Exercises.
 */

public final class NetworkUtilities {
    private static final String ERROR_TAG = "NETWORK_UTILITIES";

    public static final String MOVIEDB_IMAGE_780_URL = "http://image.tmdb.org/t/p/w780/";
    public static final String MOVIEDB_IMAGE_500_URL = "http://image.tmdb.org/t/p/w500/";
    private static final String MOVIEDB_API_MOSTPOPULAR_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String MOVIEDB_API_TOPRATED_URL = "https://api.themoviedb.org/3/movie/top_rated";

    private static final String MOVIEDB_API_MOVIEDETAIL_URL = "https://api.themoviedb.org/3/movie/%s";
    private static final String MOVIEDB_API_MOVIEDETAIL_TRAILERS_URL = MOVIEDB_API_MOVIEDETAIL_URL + "/videos";
    private static final String MOVIEDB_API_MOVIEDETAIL_REVIEWS_URL = MOVIEDB_API_MOVIEDETAIL_URL + "/reviews";

    private static final String APIKEY_PARAM = "api_key";

    private static URL buildFinalMovieDbURL(String urlString){
        //Creates an Uri for the MovieDb call
        Uri movieDbBuiltUri = Uri.parse(urlString).buildUpon()
                .appendQueryParameter(APIKEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        //Now creates an URL based on the created Uri
        URL finalMovieDbURL = null;

        try {
            finalMovieDbURL = new URL(movieDbBuiltUri.toString());
        } catch (MalformedURLException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }

        return finalMovieDbURL;
    }

    public static URL buildMovieDbReviewURL(long movieId) {

        String urlString = String.format(MOVIEDB_API_MOVIEDETAIL_REVIEWS_URL, movieId);

        return buildFinalMovieDbURL(urlString);
    }

    public static URL buildMovieDbTrailerURL(long movieId) {

        String urlString = String.format(MOVIEDB_API_MOVIEDETAIL_TRAILERS_URL, movieId);

        return buildFinalMovieDbURL(urlString);
    }

    public static URL buildMovieDbDetailURL(long movieId) {

        String urlString = String.format(MOVIEDB_API_MOVIEDETAIL_URL, movieId);

        return buildFinalMovieDbURL(urlString);
    }

    public static URL buildMovieDbQueryURL(@MovieDisplayMode int displayMode) {
        //This decides how the result will be sorted.
        String chosenURLBasedOnSortBy = "";

        switch (displayMode) {
            case MovieDisplayMode.MOST_POPULAR:
                chosenURLBasedOnSortBy = MOVIEDB_API_MOSTPOPULAR_URL;
                break;
            case MovieDisplayMode.HIGHEST_RATED:
                chosenURLBasedOnSortBy = MOVIEDB_API_TOPRATED_URL;
                break;
            default:
                throw new IllegalArgumentException(String.format("No URL to generate for displayMode '%s'", displayMode));
        }

        return buildFinalMovieDbURL(chosenURLBasedOnSortBy);
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

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
 * This class handles Network functionalities.
 * The method "getResponseFromHttpUrl" was copied from Udacity Exercises.
 */

public final class NetworkUtilities {
    private static final String ERROR_TAG = "NETWORK_UTILITIES";

    private static final String MOVIEDB_API_DESCOVERMOVIE_URL = "https://api.themoviedb.org/3/discover/movie";
    public static final String MOVIEDB_IMAGE_500_URL = "http://image.tmdb.org/t/p/w500/";

    private static final String SORTBY_PARAM = "sort_by";
    private static final String SORTBY_POPULARITY = "popularity.desc";
    private static final String SORTBY_RATE = "vote_average.desc";

    private static final String APIKEY_PARAM = "api_key";

    public static URL buildMovieDbQueryURL(MovieSortByEnum sortBy)
    {
        //This decides how the result will be sorted.
        String decidedSortBy = "";

        switch(sortBy)
        {
            case MostPopular:
                decidedSortBy = SORTBY_POPULARITY;
                break;
            case HighestRated:
                decidedSortBy = SORTBY_RATE;
        }

        //Creates an Uri for the MovieDb call
        Uri movieDbBuiltUri = Uri.parse(MOVIEDB_API_DESCOVERMOVIE_URL).buildUpon()
                .appendQueryParameter(SORTBY_PARAM,decidedSortBy)
                .appendQueryParameter(APIKEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        //Now creates an URL based on the created Uri
        URL finalMovieDbURL = null;

        try
        {
            finalMovieDbURL = new URL(movieDbBuiltUri.toString());
        }
        catch(MalformedURLException e)
        {
            Log.e(ERROR_TAG, e.getMessage());
        }

        return finalMovieDbURL;
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

package br.com.marcosaraiva.popularmovies.AsyncTasks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

import br.com.marcosaraiva.popularmovies.Database.PopularMoviesContract;
import br.com.marcosaraiva.popularmovies.Model.Movie;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDbUtilities;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDisplayMode;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;
import br.com.marcosaraiva.popularmovies.Utilities.PopularMoviesUtilities;

/**
 * Created by marco on 27/02/2018.
 * Async task to fetch movie details from MovieDb
 */

public class FetchMovieDetailFromMoviesDb_Task extends AsyncTask<Long, Void, Movie> {

    private final String ERROR_TAG = "MOVIEDETAIL_TASK_ERROR";
    public static final String TASK_NAME = "FETCH_MOVIEDETAIL_TASK";

    private AsyncTaskExtendedInterface extendedInterface;

    public FetchMovieDetailFromMoviesDb_Task(AsyncTaskExtendedInterface pExtendedInterface) {
        this.extendedInterface = pExtendedInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Movie doInBackground(Long... params) {

        long movieId = params[0];
        Movie movieWithDetails;

        //Gets the correct URL to be called
        URL movieDbApiCallURL = NetworkUtilities.buildMovieDbDetailURL(movieId);

        //If there was a problem during URL creation...
        if (movieDbApiCallURL == null)
            return null;

        try {
            String jsonMoviesRawResponse = NetworkUtilities
                    .getResponseFromHttpUrl(movieDbApiCallURL);

            movieWithDetails = MovieDbUtilities
                    .getMovieDetailsFromAPIJSONResponse(jsonMoviesRawResponse);
        } catch (RuntimeException e) {
            Log.e(ERROR_TAG, e.getMessage());
            return null;
        } catch (java.io.IOException e) {
            Log.e(ERROR_TAG, e.getMessage());
            return null;
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            return null;
        }

        return movieWithDetails;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        extendedInterface.onTaskCompleted(movie, TASK_NAME);
    }
}

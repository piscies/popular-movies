package br.com.marcosaraiva.popularmovies.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

import br.com.marcosaraiva.popularmovies.Model.Trailer;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDbUtilities;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;

/**
 * Created by marco on 21/02/2018.
 * Async task to fetch trailers from MovieDb for a given movie
 */

public class FetchTrailersFromMoviesDb_Task extends AsyncTask<Long, Void, List<Trailer>> {
    private final String ERROR_TAG = "TRAILER_TASK_ERROR";
    public static final String TASK_NAME = "FETCH_TRAILER_TASK";

    private final AsyncTaskExtendedInterface extendedInterface;

    public FetchTrailersFromMoviesDb_Task(AsyncTaskExtendedInterface pExtendedInterface){
        this.extendedInterface = pExtendedInterface;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Trailer> doInBackground(Long... params) {
        long movieId;

        //If there are no parameters, calls the SortyBy Popularity by default
        if (params.length > 0)
            movieId = params[0];
        else //If no movieId was given...
            return null;

        //Gets the correct URL to be called
        URL movieDbApiCallURL = NetworkUtilities.buildMovieDbTrailerURL(movieId);

        //If there was a problem during URL creation...
        if (movieDbApiCallURL == null)
            return null;

        try {
            String jsonTrailersRawResponse = NetworkUtilities
                    .getResponseFromHttpUrl(movieDbApiCallURL);

            return MovieDbUtilities
                    .getListOfTrailersFromAPIJSONResponse(jsonTrailersRawResponse);
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
    }

    @Override
    protected void onPostExecute(List<Trailer> trailerList) {

        extendedInterface.onTaskCompleted(trailerList, TASK_NAME);
    }
}

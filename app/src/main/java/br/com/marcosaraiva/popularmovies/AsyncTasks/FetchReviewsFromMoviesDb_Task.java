package br.com.marcosaraiva.popularmovies.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

import br.com.marcosaraiva.popularmovies.Model.Review;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDbUtilities;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;

/**
 * Created by marco on 22/02/2018.
 * Async task to fetch reviews from MovieDb for a given movie
 */

public class FetchReviewsFromMoviesDb_Task extends AsyncTask<Long, Void, List<Review>> {
    private final String ERROR_TAG = "REVIEW_TASK_ERROR";
    public static final String TASK_NAME = "FETCH_REVIEW_TASK";

    private final AsyncTaskExtendedInterface extendedInterface;

    public FetchReviewsFromMoviesDb_Task(AsyncTaskExtendedInterface pExtendedInterface) {
        this.extendedInterface = pExtendedInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Review> doInBackground(Long... params) {
        long movieId;

        //If there are no parameters, calls the SortyBy Popularity by default
        if (params.length > 0)
            movieId = params[0];
        else //If no movieId was given...
            return null;

        //Gets the correct URL to be called
        URL movieDbApiCallURL = NetworkUtilities.buildMovieDbReviewURL(movieId);

        //If there was a problem during URL creation...
        if (movieDbApiCallURL == null)
            return null;

        try {
            String jsonTrailersRawResponse = NetworkUtilities
                    .getResponseFromHttpUrl(movieDbApiCallURL);

            return MovieDbUtilities
                    .getListOfReviewsFromAPIJSONResponse(jsonTrailersRawResponse);
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
    protected void onPostExecute(List<Review> reviewList) {

        extendedInterface.onTaskCompleted(reviewList, TASK_NAME);
    }
}

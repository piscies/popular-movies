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
 * Created by marco on 25/02/2018.
 * Async task to fetch movies from MovieDb
 */

public class FetchMoviesFromMoviesDb_Task extends AsyncTask<Integer, Void, List<Movie>> {

    private final String ERROR_TAG = "MOVIE_TASK_ERROR";
    public static final String TASK_NAME = "FETCH_MOVIE_TASK";

    private AsyncTaskExtendedInterface extendedInterface;

    Context taskContext;

    public FetchMoviesFromMoviesDb_Task(AsyncTaskExtendedInterface pExtendedInterface, Context context) {
        this.extendedInterface = pExtendedInterface;
        taskContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(Integer... params) {

        @MovieDisplayMode int displayModeParameter = params[0];
        List<Movie> returnedMovieList;
        List<Movie> favoriteMoviesList;

        //Gets favorite movies from db. This will always be called because we'll mark
        //favorite movies in the main screen.
        Uri favoriteMoviesUri = PopularMoviesContract.FavoriteMovieEntry.CONTENT_URI;
        Cursor cursor = taskContext.getContentResolver().query(favoriteMoviesUri, null, null,
                null, null);

        favoriteMoviesList = PopularMoviesUtilities.getMovieListFromCursor(cursor);


        //MovieDb API Fetch
        if(displayModeParameter != MovieDisplayMode.SHOW_FAVORITES) {

            //Gets the correct URL to be called
            URL movieDbApiCallURL = NetworkUtilities.buildMovieDbQueryURL(displayModeParameter);

            //If there was a problem during URL creation...
            if (movieDbApiCallURL == null)
                return null;

            try {
                String jsonMoviesRawResponse = NetworkUtilities
                        .getResponseFromHttpUrl(movieDbApiCallURL);

                returnedMovieList = MovieDbUtilities
                        .getListOfMoviesFromAPIJSONResponse(jsonMoviesRawResponse);
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

            //Check which movie is a favorite
            for(Movie m: favoriteMoviesList){
                if(returnedMovieList.contains(m)){
                    int movieIndex = returnedMovieList.indexOf(m);

                    //Sets favorite details!
                    returnedMovieList.get(movieIndex).setFavorite(true);
                    returnedMovieList.get(movieIndex).setRuntime(m.getRuntime());
                }
            }
        }
        else{
            returnedMovieList = favoriteMoviesList;

            //Movies returned from the database will always be favorites
            for(Movie m: returnedMovieList)
                m.setFavorite(true);
        }

        return returnedMovieList;
    }

    @Override
    protected void onPostExecute(List<Movie> movieList) {
        extendedInterface.onTaskCompleted(movieList, TASK_NAME);
    }
}

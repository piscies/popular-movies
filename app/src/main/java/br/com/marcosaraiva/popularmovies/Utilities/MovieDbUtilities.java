package br.com.marcosaraiva.popularmovies.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMException;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;

import br.com.marcosaraiva.popularmovies.Model.Movie;

/**
 * This class is used to translate data from MovieDb API to our model.
 */

public final class MovieDbUtilities {
    public static List<Movie> getListOfMoviesFromAPIJSONResponse(String movieDbJSONResponse) throws JSONException, RuntimeException
    {
        //Error JSON
        String MDB_STATUSCODE = "status_code";
        String MDB_STATUSMESSAGE = "status_message";

        //Success JSON
        String MDB_RESULTS = "results";
        String MDB_RESULTS_TITLE = "title";
        String MDB_RESULTS_OVERVIEW = "overview";
        String MDB_RESULTS_POSTER = "poster_path";
        String MDB_RESULTS_VOTE_AVERAGE = "vote_average";
        String MDB_RESULTS_RELEASE_DATE = "release_date";

        //Reads the returned JSON and converts it to a JSONObject.
        JSONObject movieDbJSONObject = new JSONObject(movieDbJSONResponse);

        //If anything went wrong in the MovieDb API Call
        if(movieDbJSONObject.has(MDB_STATUSCODE))
        {
            throw new RuntimeException(movieDbJSONObject.getString(MDB_STATUSMESSAGE));
        }
        else //If the API Call was successful
        {
            //Final list to be returned
            List<Movie> returnedMovieList = new ArrayList<>();

            //Gets the results in the page returned by the MovieDb API
            JSONArray jsonMovieList = movieDbJSONObject.getJSONArray(MDB_RESULTS);

            //Iterates through each returned movie in JSON and converts to a Model Movie.
            for(int i = 0; i < jsonMovieList.length(); i++)
            {
                //Gets a single movie in the JSON Array
                JSONObject singleJSONMovie = jsonMovieList.getJSONObject(i);

                //Instantiates the movie that will be added to the final list
                Movie singleModelMovie = new Movie();

                //Fills the movie with data
                singleModelMovie.setTitle(singleJSONMovie.getString(MDB_RESULTS_TITLE));
                singleModelMovie.setOverview(singleJSONMovie.getString(MDB_RESULTS_OVERVIEW));
                singleModelMovie.setPosterRelativePath(singleJSONMovie.getString(MDB_RESULTS_POSTER));
                singleModelMovie.setVoteAverage(singleJSONMovie.getDouble(MDB_RESULTS_VOTE_AVERAGE));
                singleModelMovie.setReleaseDate(singleJSONMovie.getString(MDB_RESULTS_RELEASE_DATE));

                //Adds the movie to the final list
                returnedMovieList.add(singleModelMovie);
            }

            return returnedMovieList;
        }
    }
}

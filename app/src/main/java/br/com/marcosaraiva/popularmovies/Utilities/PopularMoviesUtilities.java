package br.com.marcosaraiva.popularmovies.Utilities;

import android.database.Cursor;

import br.com.marcosaraiva.popularmovies.Database.PopularMoviesContract;
import br.com.marcosaraiva.popularmovies.Model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 24/02/2018.
 * Helper class that features general app utilities
 */

public class PopularMoviesUtilities {
    //Details for the favorites Content Provider
    public static final String[] FAVORITE_MOVIES_PROJECTION = {
            PopularMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
            PopularMoviesContract.FavoriteMovieEntry.COLUMN_TITLE,
            PopularMoviesContract.FavoriteMovieEntry.COLUMN_OVERVIEW,
            PopularMoviesContract.FavoriteMovieEntry.COLUMN_POSTER_PATH,
            PopularMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,
            PopularMoviesContract.FavoriteMovieEntry.COLUMN_RUNTIME,
            PopularMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE
    };

    private static final int MOVIE_ID_INDEX = 1;
    private static final int TITLE_INDEX = 2;
    private static final int OVERVIEW_INDEX = 3;
    private static final int POSTER_PATH_INDEX = 4;
    private static final int RELEASE_DATE_INDEX = 5;
    private static final int RUNTIME_INDEX = 6;
    private static final int VOTE_AVERAGE_INDEX = 7;

    public static List<Movie> getMovieListFromCursor(Cursor cursor){
        List<Movie> movieList = new ArrayList();

        if(cursor.moveToFirst()){
            do {
                Movie movie = new Movie();

                movie.setMovieId(cursor.getLong(MOVIE_ID_INDEX));
                movie.setTitle(cursor.getString(TITLE_INDEX));
                movie.setOverview(cursor.getString(OVERVIEW_INDEX));
                movie.setPosterRelativePath(cursor.getString(POSTER_PATH_INDEX));
                movie.setReleaseDate(cursor.getString(RELEASE_DATE_INDEX));
                movie.setRuntime(cursor.getInt(RUNTIME_INDEX));
                movie.setVoteAverage(cursor.getFloat(VOTE_AVERAGE_INDEX));

                movieList.add(movie);

            }while(cursor.moveToNext());
        }

        return movieList;
    }
}

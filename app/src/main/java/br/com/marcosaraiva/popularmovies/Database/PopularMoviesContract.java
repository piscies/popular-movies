package br.com.marcosaraiva.popularmovies.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by marco on 22/02/2018.
 * Database contract for the Popular Movies DB
 */

public class PopularMoviesContract {
    //URI
    public static final String CONTENT_AUTHORITY = "br.com.marcosaraiva.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Paths
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteMovieEntry implements BaseColumns{
        //Uri for Favorites
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        //Table and table columns
        public static final String TABLE_NAME = "favoriteMovie";

        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_RUNTIME = "runtime";
    }
}

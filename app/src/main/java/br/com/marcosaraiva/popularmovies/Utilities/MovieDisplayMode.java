package br.com.marcosaraiva.popularmovies.Utilities;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static br.com.marcosaraiva.popularmovies.Utilities.MovieDisplayMode.HIGHEST_RATED;
import static br.com.marcosaraiva.popularmovies.Utilities.MovieDisplayMode.MOST_POPULAR;
import static br.com.marcosaraiva.popularmovies.Utilities.MovieDisplayMode.SHOW_FAVORITES;

/**
 * This was created to act as an Enum variable for the Sort By feature.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({MOST_POPULAR, HIGHEST_RATED, SHOW_FAVORITES})

public @interface MovieDisplayMode {
    int MOST_POPULAR = 0;
    int HIGHEST_RATED = 1;
    int SHOW_FAVORITES = 2;
}


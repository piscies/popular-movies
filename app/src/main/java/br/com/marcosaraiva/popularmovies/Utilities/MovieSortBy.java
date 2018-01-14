package br.com.marcosaraiva.popularmovies.Utilities;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static br.com.marcosaraiva.popularmovies.Utilities.MovieSortBy.HIGHEST_RATED;
import static br.com.marcosaraiva.popularmovies.Utilities.MovieSortBy.MOST_POPULAR;

/**
 * Created by marco on 14/01/2018.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({MOST_POPULAR, HIGHEST_RATED})

public @interface MovieSortBy {
    int MOST_POPULAR = 0;
    int HIGHEST_RATED = 1;
}


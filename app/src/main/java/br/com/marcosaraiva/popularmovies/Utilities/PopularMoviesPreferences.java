package br.com.marcosaraiva.popularmovies.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by marco on 24/02/2018.
 * Helper class to deal with Shared Preferences
 */

public class PopularMoviesPreferences {
    private static final String PREFERENCE_SORTBY = "PREF_SORTBY";

    public static void setSortBy(@MovieSortBy int newSortBy, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(PREFERENCE_SORTBY, newSortBy);
        editor.apply();
    }

    public static int getSortBy(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        //Default value will be "Most Popular" for the Sort By.
        return preferences.getInt(PREFERENCE_SORTBY, MovieSortBy.MOST_POPULAR);
    }
}

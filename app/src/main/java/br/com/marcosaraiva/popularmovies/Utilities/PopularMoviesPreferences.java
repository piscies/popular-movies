package br.com.marcosaraiva.popularmovies.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by marco on 24/02/2018.
 * Helper class to deal with Shared Preferences
 */

public class PopularMoviesPreferences {
    private static final String PREFERENCE_DISPLAYMODE = "PREF_DISPLAYMODE";

    public static void setDisplayMode(@MovieDisplayMode int newDisplayMode, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(PREFERENCE_DISPLAYMODE, newDisplayMode);
        editor.apply();
    }

    public static int getDisplayMode(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        //Default value will be "Sort by Most Popular" for the Display Mode.
        return preferences.getInt(PREFERENCE_DISPLAYMODE, MovieDisplayMode.MOST_POPULAR);
    }
}

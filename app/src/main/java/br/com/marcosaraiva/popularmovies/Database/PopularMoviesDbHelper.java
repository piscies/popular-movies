package br.com.marcosaraiva.popularmovies.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.marcosaraiva.popularmovies.Database.PopularMoviesContract.FavoriteMovieEntry;

/**
 * Created by marco on 22/02/2018.
 * Helper class for Popular Movies DB
 */

public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 1;

    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITES_TABLE =

                "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" +
                        FavoriteMovieEntry._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteMovieEntry.COLUMN_MOVIE_ID       + " INTEGER NOT NULL, "  +
                        FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL,"  +
                        FavoriteMovieEntry.COLUMN_OVERVIEW   + " TEXT NOT NULL, "  +
                        FavoriteMovieEntry.COLUMN_POSTER_PATH   + " TEXT NOT NULL, "  +
                        FavoriteMovieEntry.COLUMN_VOTE_AVERAGE   + " REAL NOT NULL, "  +
                        FavoriteMovieEntry.COLUMN_RELEASE_DATE   + " INTEGER NOT NULL, "  +
                        FavoriteMovieEntry.COLUMN_RUNTIME + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

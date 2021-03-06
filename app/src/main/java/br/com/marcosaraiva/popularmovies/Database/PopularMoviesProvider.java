package br.com.marcosaraiva.popularmovies.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by marco on 24/02/2018.
 * Content Provider for the Popular Movies app.
 */

public class PopularMoviesProvider extends ContentProvider {

    private static final int CODE_FAVORITE_MOVIES = 101;
    private static final int CODE_FAVORITE_MOVIES_SINGLE = 102;

    private final UriMatcher uriMatcher = buildUriMatcher();
    private PopularMoviesDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PATH_FAVORITES, CODE_FAVORITE_MOVIES);
        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PATH_FAVORITES + "/#", CODE_FAVORITE_MOVIES_SINGLE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new PopularMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch(uriMatcher.match(uri)){
            case CODE_FAVORITE_MOVIES:
                cursor = dbHelper.getReadableDatabase().query(
                        PopularMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri.toString());
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("GetType is not implemented yet");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        switch(uriMatcher.match(uri)){
            case CODE_FAVORITE_MOVIES:
                dbHelper.getWritableDatabase().insert(
                        PopularMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        null,
                        contentValues);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri for insertion: " + uri.toString());
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] whereArgs) {
        int nRowsDeleted;

        switch(uriMatcher.match(uri)){
            case CODE_FAVORITE_MOVIES_SINGLE:
                nRowsDeleted = dbHelper.getWritableDatabase().delete(PopularMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        PopularMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{ uri.getLastPathSegment() });
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri for deletion: " + uri.toString());
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return nRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Update is not implemented yet");
    }
}

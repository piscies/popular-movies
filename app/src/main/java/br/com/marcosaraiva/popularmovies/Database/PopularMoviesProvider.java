package br.com.marcosaraiva.popularmovies.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.xml.transform.URIResolver;

/**
 * Created by marco on 24/02/2018.
 * Content Provider for the Popular Movies app.
 */

public class PopularMoviesProvider extends ContentProvider {

    public static final int CODE_FAVORITE_MOVIES = 101;

    private UriMatcher uriMatcher = buildUriMatcher();
    private PopularMoviesDbHelper dbHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(PopularMoviesContract.CONTENT_AUTHORITY, PopularMoviesContract.PATH_FAVORITES, CODE_FAVORITE_MOVIES);

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
                throw new UnsupportedOperationException("Unknown uri: " + uri.toString());
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Delete is not implemented yet");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Update is not implemented yet");
    }
}
